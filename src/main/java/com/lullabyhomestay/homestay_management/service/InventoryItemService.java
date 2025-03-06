package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.InventoryItem;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.InventoryItemRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.service.specifications.InventoryItemSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InventoryItemService {
    private final InventoryItemRepository itemRepository;
    private final InventoryStockRepository stockRepository;
    private final InventoryTransactionRepository transactionRepository;

    public List<InventoryItem> getAllItems() {
        return this.itemRepository.findAll();
    }

    public Page<InventoryItem> getAllItems(Pageable pageable) {
        return this.itemRepository.findAll(pageable);
    }

    public Page<InventoryItem> searchItems(String keyword, Long categoryID,
            int page, String sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(sortOrder) ? Sort.by("Price").ascending()
                        : "desc".equals(sortOrder) ? Sort.by("Price").descending() : Sort.unsorted());
        if ((keyword == null || keyword.isEmpty()) && categoryID == null)
            return itemRepository
                    .findAll(pageable);
        Specification<InventoryItem> spec = Specification.where(InventoryItemSpecifications.hasCategory(categoryID))
                .and(InventoryItemSpecifications.nameLike(keyword));
        return itemRepository.findAll(spec, pageable);
    }

    public void handleSaveInventoryItem(InventoryItem amenity) {
        this.itemRepository.save(amenity);
    }

    public InventoryItem getInventoryItemByID(long itemID) {
        Optional<InventoryItem> itemOpt = itemRepository.findByItemID(itemID);
        if (!itemOpt.isPresent()) {
            throw new NotFoundException("Đồ dùng");
        }
        return itemOpt.get();
    }

    @Transactional
    public void deleteByInventoryItemID(long itemID) {
        this.itemRepository.deleteByItemID(itemID);
    }

    public boolean canDeleteItem(long itemID) {
        boolean hasStock = stockRepository.existsByInventoryItem_ItemID(itemID);
        boolean hasTransaction = transactionRepository.existsByInventoryItem_ItemID(itemID);
        return !(hasStock || hasTransaction);
    }

    @Transactional
    public void deleteByItemID(long itemID) {
        if (canDeleteItem(itemID)) {
            this.itemRepository.deleteByItemID(itemID);
        }
    }
}
