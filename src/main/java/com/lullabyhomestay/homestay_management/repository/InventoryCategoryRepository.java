package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.InventoryCategory;

@Repository
public interface InventoryCategoryRepository extends JpaRepository<InventoryCategory, Long> {
    List<InventoryCategory> findAll();

    Page<InventoryCategory> findAll(Pageable page);

    Page<InventoryCategory> findByCategoryNameContainingIgnoreCase(String keyword, Pageable page);

    Optional<InventoryCategory> findByCategoryID(long categoryID);

    InventoryCategory save(InventoryCategory category);

    void deleteByCategoryID(long id);

}
