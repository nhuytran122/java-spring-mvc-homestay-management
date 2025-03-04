package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.InventoryCategory_;
import com.lullabyhomestay.homestay_management.domain.InventoryItem;
import com.lullabyhomestay.homestay_management.domain.InventoryItem_;

public class InventoryItemSpecifications {
    public static Specification<InventoryItem> nameLike(String keyword) {
        return BaseSpecifications.like(InventoryItem_.ITEM_NAME, keyword);
    }

    public static Specification<InventoryItem> hasCategory(Long categoryID) {
        return BaseSpecifications.equalJoin(InventoryItem_.INVENTORY_CATEGORY, InventoryCategory_.CATEGORY_ID, categoryID);
    }
}
