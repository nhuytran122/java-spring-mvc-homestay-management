package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Page<InventoryItem> findAll(Pageable page);

    Optional<InventoryItem> findByItemId(long itemId);

    InventoryItem save(InventoryItem item);

    void deleteByItemId(long itemId);

    Page<InventoryItem> findAll(Specification<InventoryItem> spec, Pageable page);

    boolean existsByInventoryCategory_CategoryId(long categoryId);
}
