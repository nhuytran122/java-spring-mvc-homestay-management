package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.repository.InventoryStockRepository;
import com.lullabyhomestay.homestay_management.service.specifications.InventoryStockSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InventoryStockService {
    private final InventoryStockRepository stockRepository;

    public Page<InventoryStock> searchStocks(String keyword, Long branchID,
            int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);

        if ((keyword == null || keyword.isEmpty()) && branchID == null)
            return stockRepository
                    .findAll(pageable);
        Specification<InventoryStock> spec = Specification.where(InventoryStockSpecifications.hasBranch(branchID))
                .and(InventoryStockSpecifications.nameItemLike(keyword));
        return stockRepository.findAll(spec, pageable);
    }

    public void handleSaveInventoryStock(InventoryStock stock) {
        this.stockRepository.save(stock);
    }

    public Optional<InventoryStock> findStockByItemIDAndBranchID(long itemID, long branchID) {
        Optional<InventoryStock> stockOpt = stockRepository.findByInventoryItem_ItemIDAndBranch_BranchID(itemID,
                branchID);
        return stockOpt;
    }
}
