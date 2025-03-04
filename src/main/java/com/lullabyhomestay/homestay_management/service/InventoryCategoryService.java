package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.InventoryCategory;
import com.lullabyhomestay.homestay_management.repository.InventoryCategoryRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryItemRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InventoryCategoryService {
    private final InventoryCategoryRepository inventoryCategoryRepository;
    private final InventoryItemRepository itemRepository;

    public List<InventoryCategory> getAllInventoryCategories() {
        return this.inventoryCategoryRepository.findAll();
    }

    public Optional<InventoryCategory> getInventoryCategoryByID(long id) {
        return this.inventoryCategoryRepository.findByCategoryID(id);
    }

    public InventoryCategory handleSaveInventoryCategory(InventoryCategory amenityCategory) {
        return this.inventoryCategoryRepository.save(amenityCategory);
    }

    public Page<InventoryCategory> searchInventoryCategories(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.isEmpty())
                ? inventoryCategoryRepository.findByCategoryNameContainingIgnoreCase(keyword, pageable)
                : inventoryCategoryRepository.findAll(pageable);
    }

    public boolean canDeleteCategory(long categoryID) {
        boolean hasItem = itemRepository.existsByInventoryCategory_CategoryID(categoryID);
        return !hasItem;
    }

    @Transactional
    public void deleteByCategoryID(long categoryID) {
        if (canDeleteCategory(categoryID)) {
            this.inventoryCategoryRepository.deleteByCategoryID(categoryID);
        }
    }
}