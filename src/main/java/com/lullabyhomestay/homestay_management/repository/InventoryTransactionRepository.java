package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    boolean existsByBranch_BranchID(long branchID);
    
}
