package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefundType {
    FULL("Hoàn tiền 100%"),
    PARTIAL_70("Hoàn tiền 70%"),
    PARTIAL_30("Hoàn tiền 30%");

    private String displayName;
}
