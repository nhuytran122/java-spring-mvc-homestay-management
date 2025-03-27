package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {
    CASH("Tiền mặt"),
    TRANSFER("Chuyển khoản");

    private String displayName;
}
