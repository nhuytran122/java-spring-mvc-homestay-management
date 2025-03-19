package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.InventoryItem;
import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.domain.dto.SearchTransactionCriterialDTO;
import com.lullabyhomestay.homestay_management.exception.InventoryException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.InventoryItemRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.service.specifications.InventoryTransactionSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InventoryTransactionService {
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryStockRepository stockRepository;
    private final InventoryItemRepository itemRepository;

    public InventoryTransaction getTransactionByID(Long transactionID) {
        Optional<InventoryTransaction> transactionOpt = transactionRepository.findByTransactionID(transactionID);
        if (!transactionOpt.isPresent()) {
            throw new NotFoundException("Giao dịch");
        }
        return transactionOpt.get();
    }

    public Page<InventoryTransaction> searchTransactions(SearchTransactionCriterialDTO criteria,
            int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CreatedAt").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CreatedAt").descending() : Sort.unsorted());

        if ((criteria.getKeyword() == null || criteria.getKeyword().isEmpty()) && criteria.getBranchID() == null
                && (criteria.getTransactionType() == null || criteria.getTransactionType().isEmpty()))
            return transactionRepository
                    .findAll(pageable);
        TransactionType typeEnum = null;
        if (criteria.getTransactionType() != null && !criteria.getTransactionType().isEmpty()) {
            try {
                typeEnum = TransactionType.valueOf(criteria.getTransactionType().toUpperCase());
            } catch (IllegalArgumentException e) {
                typeEnum = null;
            }
        }
        Specification<InventoryTransaction> spec = Specification
                .where(InventoryTransactionSpecifications.hasBranch(criteria.getBranchID()))
                .and(InventoryTransactionSpecifications.nameItemLike(criteria.getKeyword()))
                .and(InventoryTransactionSpecifications.transactionTypeEqual(typeEnum));
        return transactionRepository.findAll(spec, pageable);
    }

    @Transactional
    public void handleSaveTransaction(InventoryTransaction transaction) {
        this.transactionRepository.save(transaction);
        this.handleChangeStock(transaction);
    }

    private boolean handleChangeStock(InventoryTransaction transaction) {
        Optional<InventoryItem> currentItem = this.itemRepository
                .findByItemID(transaction.getInventoryItem().getItemID());
        if (!currentItem.isPresent()) {
            throw new InventoryException("Không tìm thấy đồ dùng", transaction);
        }

        Optional<InventoryStock> currentStockOpt = stockRepository.findByInventoryItem_ItemIDAndBranch_BranchID(
                currentItem.get().getItemID(), transaction.getBranch().getBranchID());
        InventoryStock currentStock;
        if (!currentStockOpt.isPresent()) {
            if (transaction.getTransactionType() == TransactionType.EXPORT) {
                throw new InventoryException(
                        "Hiện không có đồ dùng này trong kho.", transaction);
            }
            currentStock = new InventoryStock();
            currentStock.setQuantity(0);
            currentStock.setBranch(transaction.getBranch());
            currentStock.setInventoryItem(currentItem.get());
            this.stockRepository.save(currentStock); // tạo mới
        } else {
            currentStock = currentStockOpt.get();
        }

        int newQuantity;
        if (transaction.getTransactionType() == TransactionType.IMPORT) {
            newQuantity = currentStock.getQuantity() + transaction.getQuantity();
        } else {
            newQuantity = currentStock.getQuantity() - transaction.getQuantity();
            if (newQuantity < 0) {
                throw new InventoryException(
                        "Số lượng xuất vượt quá tồn kho.", transaction);
            }
        }
        currentStock.setQuantity(newQuantity);
        this.stockRepository.save(currentStock); // cập nhật
        return true;
    }

    public boolean canUpdateTransaction(Long transactionID) {
        InventoryTransaction transaction = this.getTransactionByID(transactionID);
        LocalDateTime transactionDate = transaction.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        long minutesDifference = ChronoUnit.MINUTES.between(transactionDate, now);
        return minutesDifference <= 2 * 60;
    }

    @Transactional
    public void updateTransaction(InventoryTransaction updatedTransaction, int oldQuantity) {
        if (this.canUpdateTransaction(updatedTransaction.getTransactionID())) {
            InventoryTransaction existingTransaction = transactionRepository
                    .findById(updatedTransaction.getTransactionID())
                    .get();
            int newQuantity = updatedTransaction.getQuantity();

            existingTransaction.setQuantity(newQuantity);

            transactionRepository.save(existingTransaction);
            updateStockWithDifference(existingTransaction, newQuantity - oldQuantity);
        }
    }

    private void updateStockWithDifference(InventoryTransaction transaction, int quantityDifference) {
        InventoryItem currentItem = itemRepository.findById(transaction.getInventoryItem().getItemID()).get();

        InventoryStock currentStock = stockRepository
                .findByInventoryItem_ItemIDAndBranch_BranchID(currentItem.getItemID(),
                        transaction.getBranch().getBranchID())
                .get();

        int newStockQuantity;
        if (transaction.getTransactionType() == TransactionType.IMPORT) {
            newStockQuantity = currentStock.getQuantity() + quantityDifference;
        } else {
            newStockQuantity = currentStock.getQuantity() - quantityDifference;
            if (newStockQuantity < 0) {
                throw new InventoryException("Số lượng xuất vượt quá tồn kho.", transaction);
            }
        }
        currentStock.setQuantity(newStockQuantity);
        stockRepository.save(currentStock);
    }
}