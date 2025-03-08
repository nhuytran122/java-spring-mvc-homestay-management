package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {
    BOOKED("Đã đặt phòng"),
    CANCELLED("Đã hủy"),
    COMPLETED("Đã hoàn tất");

    private String displayName;
}