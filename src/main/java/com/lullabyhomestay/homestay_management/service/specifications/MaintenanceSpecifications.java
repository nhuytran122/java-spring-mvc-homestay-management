package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Branch_;
import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;
import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest_;
import com.lullabyhomestay.homestay_management.utils.MaintenanceStatus;

public class MaintenanceSpecifications {
    // public static Specification<MaintenanceRequest> hasBranch(Long branchID) {
    // return BaseSpecifications.equalJoinTwoLevels(
    // MaintenanceRequest_.ROOM, // Mức 1: room
    // Room_.BRANCH, // Mức 2: branch
    // Branch_.BRANCH_ID, // Field cuối: branchID
    // branchID // Giá trị so sánh
    // );
    // }
    public static Specification<MaintenanceRequest> hasBranch(Long branchID) {
        return BaseSpecifications.equalJoin(MaintenanceRequest_.BRANCH, Branch_.BRANCH_ID, branchID);
    }

    public static Specification<MaintenanceRequest> descriptionLike(String keyword) {
        return BaseSpecifications.like(MaintenanceRequest_.DESCRIPTION, keyword);
    }

    public static Specification<MaintenanceRequest> statusEqual(MaintenanceStatus status) {
        return BaseSpecifications.equal(MaintenanceRequest_.STATUS, status);
    }
}
