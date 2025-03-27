package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentPurpose {
    ROOM_BOOKING("Thanh toán tiền phòng ban đầu"),
    PREPAID_SERVICE("Thanh toán dịch vụ ban đầu"),
    ADDITIONAL_SERVICE("Thanh toán dịch vụ phát sinh"),
    EXTENDED_HOURS("Thanh toán giờ thuê thêm");

    private String description;
}
