package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BranchRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;

    public List<Branch> getAllBranches() {
        return this.branchRepository.findAll();
    }

    public Page<Branch> searchBranches(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.isEmpty())
                ? branchRepository.findByBranchNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
                        keyword, keyword, pageable)
                : branchRepository
                        .findAll(pageable);
    }

    public void handleSaveBranch(Branch branch) {
        this.branchRepository.save(branch);
    }

    public Branch getBranchById(long branchId) {
        Optional<Branch> branchOpt = branchRepository.findByBranchId(branchId);
        if (!branchOpt.isPresent()) {
            throw new NotFoundException("Chi nhánh");
        }
        return branchOpt.get();
    }

    public boolean canDeleteBranch(long branchId) {
        boolean hasRooms = roomRepository.existsByBranch_BranchId(branchId);
        boolean hasTransactions = inventoryTransactionRepository.existsByBranch_BranchId(branchId);
        boolean hasInventoryStock = inventoryStockRepository.existsByBranch_BranchId(branchId);
        boolean hasMaintenance = maintenanceRequestRepository.existsByBranch_BranchId(branchId);
        return !(hasRooms || hasTransactions || hasInventoryStock || hasMaintenance);
    }

    @Transactional
    public void deleteByBranchId(long branchId) {
        if (!canDeleteBranch(branchId)) {
            throw new CannotDeleteException("Chi nhánh");
        }
        this.branchRepository.deleteByBranchId(branchId);
    }
}
