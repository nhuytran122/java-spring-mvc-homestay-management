package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long>, JpaSpecificationExecutor<InventoryTransaction> {
    Page<InventoryTransaction> findAll(Pageable page);

    Page<InventoryTransaction> findAll(Specification<InventoryTransaction> spec, Pageable page);

    boolean existsByBranch_BranchID(long branchID);

    boolean existsByInventoryItem_ItemID(long itemID);

    InventoryTransaction save(InventoryTransaction transaction);

    Optional<InventoryTransaction> findByTransactionID(Long transactionID);

    boolean existsByEmployee_EmployeeID(long employeeID);

}
