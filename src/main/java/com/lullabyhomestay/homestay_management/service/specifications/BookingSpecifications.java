package com.lullabyhomestay.homestay_management.service.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Branch_;
import com.lullabyhomestay.homestay_management.domain.Customer_;
import com.lullabyhomestay.homestay_management.domain.RoomType_;
import com.lullabyhomestay.homestay_management.domain.Room_;
import com.lullabyhomestay.homestay_management.domain.User_;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Booking_;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

public class BookingSpecifications {
    public static Specification<Booking> hasBranch(Long branchID) {
        return BaseSpecifications.equalJoinTwoLevels(
                Booking_.ROOM, // Mức 1: room
                Room_.BRANCH, // Mức 2: branch
                Branch_.BRANCH_ID, // Field cuối: branchID
                branchID // Giá trị so sánh
        );
    }

    public static Specification<Booking> hasRoomType(Long roomTypeID) {
        return BaseSpecifications.equalJoinTwoLevels(
                Booking_.ROOM,
                Room_.ROOM_TYPE,
                RoomType_.ROOM_TYPE_ID,
                roomTypeID);
    }

    public static Specification<Booking> customerNameLike(String keyword) {
        return BaseSpecifications.likeJoinTwoLevels(Booking_.CUSTOMER, Customer_.USER, User_.FULL_NAME, keyword);
    }

    public static Specification<Booking> statusEqual(BookingStatus status) {
        return BaseSpecifications.equal(Booking_.STATUS, status);
    }

    public static Specification<Booking> hasCustomer(Long customerID) {
        return BaseSpecifications.equalJoin(Booking_.CUSTOMER, Customer_.CUSTOMER_ID, customerID);
    }

    public static Specification<Booking> checkInBetween(LocalDateTime fromTime, LocalDateTime toTime) {
        return BaseSpecifications.between(Booking_.CHECK_IN, fromTime, toTime);
    }
}
