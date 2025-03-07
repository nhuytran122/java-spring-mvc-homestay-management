package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Branch_;
import com.lullabyhomestay.homestay_management.domain.InventoryItem_;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction_;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

public class InventoryTransactionSpecifications {
    public static Specification<InventoryTransaction> nameItemLike(String keyword) {
        return BaseSpecifications.likeJoin(InventoryTransaction_.INVENTORY_ITEM, InventoryItem_.ITEM_NAME, keyword);
    }

    public static Specification<InventoryTransaction> hasBranch(Long branchID) {
        return BaseSpecifications.equalJoin(InventoryTransaction_.BRANCH, Branch_.BRANCH_ID,
                branchID);
    }

    public static Specification<InventoryTransaction> transactionTypeEqual(TransactionType type) {
        return BaseSpecifications.equal(InventoryTransaction_.TRANSACTION_TYPE, type);
    }
}
