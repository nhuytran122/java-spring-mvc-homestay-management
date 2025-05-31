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
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
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

    public Page<InventoryItem> searchItems(String keyword, Long categoryId,
            int page, String sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(sortOrder) ? Sort.by("Price").ascending()
                        : "desc".equals(sortOrder) ? Sort.by("Price").descending() : Sort.unsorted());
        if ((keyword == null || keyword.isEmpty()) && categoryId == null)
            return itemRepository
                    .findAll(pageable);
        Specification<InventoryItem> spec = Specification.where(InventoryItemSpecifications.hasCategory(categoryId))
                .and(InventoryItemSpecifications.nameLike(keyword));
        return itemRepository.findAll(spec, pageable);
    }

    public void handleSaveInventoryItem(InventoryItem amenity) {
        this.itemRepository.save(amenity);
    }

    public InventoryItem getInventoryItemById(long itemId) {
        Optional<InventoryItem> itemOpt = itemRepository.findByItemId(itemId);
        if (!itemOpt.isPresent()) {
            throw new NotFoundException("Đồ dùng");
        }
        return itemOpt.get();
    }

    @Transactional
    public void deleteByInventoryItemId(long itemId) {
        this.itemRepository.deleteByItemId(itemId);
    }

    public boolean canDeleteItem(long itemId) {
        boolean hasStock = stockRepository.existsByInventoryItem_ItemId(itemId);
        boolean hasTransaction = transactionRepository.existsByInventoryItem_ItemId(itemId);
        return !(hasStock || hasTransaction);
    }

    @Transactional
    public void deleteByItemId(long itemId) {
        if (!canDeleteItem(itemId)) {
            throw new CannotDeleteException("Đồ dùng");
        }
        this.itemRepository.deleteByItemId(itemId);

    }
}
