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

    public InventoryTransaction getTransactionById(Long transactionId) {
        Optional<InventoryTransaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
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

        if ((criteria.getKeyword() == null || criteria.getKeyword().isEmpty()) && criteria.getBranchId() == null
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
                .where(InventoryTransactionSpecifications.hasBranch(criteria.getBranchId()))
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
                .findByItemId(transaction.getInventoryItem().getItemId());

        Optional<InventoryStock> currentStockOpt = stockRepository.findByInventoryItem_ItemIdAndBranch_BranchId(
                currentItem.get().getItemId(), transaction.getBranch().getBranchId());
        InventoryStock currentStock;
        if (!currentStockOpt.isPresent()) {
            if (transaction.getTransactionType() == TransactionType.EXPORT) {
                return false;
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
                return false;
            }
        }
        currentStock.setQuantity(newQuantity);
        this.stockRepository.save(currentStock); // cập nhật
        return true;
    }

    public boolean canUpdateTransaction(Long transactionId) {
        InventoryTransaction transaction = this.getTransactionById(transactionId);
        LocalDateTime transactionDate = transaction.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        long minutesDifference = ChronoUnit.MINUTES.between(transactionDate, now);
        return minutesDifference <= 2 * 60;
    }

    @Transactional
    public void updateTransaction(InventoryTransaction updatedTransaction, int oldQuantity) {
        if (!this.canUpdateTransaction(updatedTransaction.getTransactionId())) {
            throw new IllegalStateException("Không thể cập nhật giao dịch vì đã quá thời gian cho phép");
        }
        InventoryTransaction existingTransaction = transactionRepository.findById(updatedTransaction.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Giao dịch kho", "Giao dịch không tồn tại"));
        int newQuantity = updatedTransaction.getQuantity();

        existingTransaction.setQuantity(newQuantity);

        transactionRepository.save(existingTransaction);
        updateStockWithDifference(existingTransaction, newQuantity - oldQuantity);

    }

    private void updateStockWithDifference(InventoryTransaction transaction, int quantityDifference) {
        InventoryItem currentItem = itemRepository.findById(transaction.getInventoryItem().getItemId()).get();

        InventoryStock currentStock = stockRepository
                .findByInventoryItem_ItemIdAndBranch_BranchId(currentItem.getItemId(),
                        transaction.getBranch().getBranchId())
                .get();

        int newStockQuantity;
        if (transaction.getTransactionType() == TransactionType.IMPORT) {
            newStockQuantity = currentStock.getQuantity() + quantityDifference;
        } else {
            newStockQuantity = currentStock.getQuantity() - quantityDifference;
            if (newStockQuantity < 0) {
                return;
            }
        }
        currentStock.setQuantity(newStockQuantity);
        stockRepository.save(currentStock);
    }
}