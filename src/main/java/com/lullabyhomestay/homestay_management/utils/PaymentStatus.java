package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {
    PENDING("Đang chờ"),
    COMPLETED("Hoàn thành"),
    FAILED("Thất bại"),
    REFUNDED("Đã hoàn tiền"),
    PENDING_REFUND("Đang chờ hoàn tiền");

    private String displayName;
}
