package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.repository.BranchRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryStockRepository inventoryStockRepository;

    public List<Branch> getAllBranches() {
        return this.branchRepository.findAll();
    }

    public Page<Branch> searchBranches(Optional<String> keyword, Pageable pageable) {
        return (keyword.isPresent())
                ? branchRepository.findByBranchNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword.get(),
                        keyword.get(), pageable)
                : branchRepository.findAll(pageable);
    }

    public void handleSaveBranch(Branch branch) {
        this.branchRepository.save(branch);
    }

    public Optional<Branch> getBranchByID(long branchID) {
        return this.branchRepository.findByBranchID(branchID);
    }

    public boolean canDeleteBranch(long branchID) {
        boolean hasRooms = roomRepository.existsByBranch_BranchID(branchID);
        boolean hasTransactions = inventoryTransactionRepository.existsByBranch_BranchID(branchID);
        boolean hasInventoryStock = inventoryStockRepository.existsByBranch_BranchID(branchID);
        return !(hasRooms || hasTransactions || hasInventoryStock);
    }

    @Transactional
    public void deleteByBranchID(long branchID) {
        if (canDeleteBranch(branchID)) {
            this.branchRepository.deleteByBranchID(branchID);
        }
    }
}
