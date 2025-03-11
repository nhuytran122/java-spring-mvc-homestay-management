package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Branch_;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomType_;
import com.lullabyhomestay.homestay_management.domain.Room_;

public class RoomSpecifications {
    public static Specification<Room> hasBranch(Long branchID) {
        return BaseSpecifications.equalJoin(Room_.BRANCH, Branch_.BRANCH_ID, branchID);
    }

    public static Specification<Room> hasRoomType(Long roomTypeID) {
        return BaseSpecifications.equalJoin(Room_.ROOM_TYPE, RoomType_.ROOM_TYPE_ID, roomTypeID);
    }
}
