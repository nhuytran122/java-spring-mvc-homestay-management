package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.InventoryItem;
import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.exception.InventoryException;
import com.lullabyhomestay.homestay_management.repository.InventoryItemRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InventoryTransactionService {
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryStockRepository stockRepository;
    private final InventoryItemRepository itemRepository;

    @Transactional
    public void handleSaveTransaction(InventoryTransaction transaction) {
        this.transactionRepository.save(transaction);
        this.handleChangeStock(transaction);
    }

    public boolean handleChangeStock(InventoryTransaction transaction) {
        Optional<InventoryItem> currentItem = this.itemRepository
                .findByItemID(transaction.getInventoryItem().getItemID());
        if (!currentItem.isPresent()) {
            throw new InventoryException("Không tìm thấy đồ dùng");
        }

        Optional<InventoryStock> currentStockOpt = stockRepository.findByInventoryItem_ItemIDAndBranch_BranchID(
                currentItem.get().getItemID(), transaction.getBranch().getBranchID());
        InventoryStock currentStock;
        if (!currentStockOpt.isPresent()) {
            if (transaction.getTransactionType() == TransactionType.EXPORT) {
                throw new InventoryException(
                        "Hiện không có " + transaction.getInventoryItem().getItemName() + " trong kho.");
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
                        "Số lượng xuất vượt quá tồn kho.");
            }
        }
        currentStock.setQuantity(newQuantity);
        this.stockRepository.save(currentStock); // cập nhật
        return true;
    }
}