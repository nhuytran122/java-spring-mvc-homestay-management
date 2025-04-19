package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ObjectType {
    BOOKING("Đơn đặt phòng"),
    PAYMENT("Thanh toán"),
    BOOKING_SERVICE("Đơn đặt dịch vụ"),
    BOOKING_EXTENSION("Đơn gia hạn đặt phòng");

    private String displayName;
}
