package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomStatus {
    BUSY("Đã được đặt"),
    MAINTENANCE("Đang bảo trì"),
    CLEANING("Đang dọn dẹp"),
    EXTENDED("Đã gia hạn");

    private String description;
}
