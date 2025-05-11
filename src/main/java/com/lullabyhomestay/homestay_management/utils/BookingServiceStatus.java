package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingServiceStatus {
    PENDING("Đang chờ"),
    IN_PROGRESS("Đang phục vụ"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy");

    private String displayName;
}
