package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.InventoryStock;

@Repository
public interface InventoryStockRepository
        extends JpaRepository<InventoryStock, Long>, JpaSpecificationExecutor<InventoryStock> {

    boolean existsByBranch_BranchID(long branchID);

    boolean existsByInventoryItem_ItemID(long itemID);

    Page<InventoryStock> findAll(Pageable page);

    InventoryStock save(InventoryStock stock);

    Page<InventoryStock> findAll(Specification<InventoryStock> spec, Pageable page);

    Optional<InventoryStock> findByInventoryItem_ItemIDAndBranch_BranchID(long itemID, long branchID);
}
