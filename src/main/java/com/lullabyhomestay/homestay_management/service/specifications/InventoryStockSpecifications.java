package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Branch_;
import com.lullabyhomestay.homestay_management.domain.InventoryItem_;
import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryStock_;

public class InventoryStockSpecifications {
    public static Specification<InventoryStock> nameItemLike(String keyword) {
        return BaseSpecifications.likeJoin(InventoryStock_.INVENTORY_ITEM, InventoryItem_.ITEM_NAME, keyword);
    }

    public static Specification<InventoryStock> hasBranch(Long branchID) {
        return BaseSpecifications.equalJoin(InventoryStock_.BRANCH, Branch_.BRANCH_ID,
                branchID);
    }
}
