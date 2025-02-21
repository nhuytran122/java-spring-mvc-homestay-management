package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.InventoryStock;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    boolean existsByBranch_BranchID(long branchID);
    
}
