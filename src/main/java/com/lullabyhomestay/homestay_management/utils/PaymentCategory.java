package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentCategory {
    ROOM_DEPOSIT("Trả cọc tiền phòng"),
    ROOM_BALANCE("Thanh toán phần còn lại tiền phòng"),
    ROOM_FULL("Trả full tiền phòng"),
    INITIAL_SERVICE("Thanh toán dịch vụ ban đầu"),
    EXTRA_SERVICE("Thanh toán dịch vụ thêm"),
    EXTRA_HOUR("Thanh toán giờ thuê thêm");

    private String displayName;
}
