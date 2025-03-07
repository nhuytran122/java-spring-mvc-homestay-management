package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    IMPORT("Nhập kho"),
    EXPORT("Xuất kho");

    private String displayName;
}