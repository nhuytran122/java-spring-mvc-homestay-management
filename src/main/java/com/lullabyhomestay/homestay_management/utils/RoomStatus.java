package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomStatus {
    BUSY("Đã đặt", "busy", true),
    CLEANING("Dọn dẹp", "cleaning", false),
    EXTENDED("Gia hạn", "extended", true);

    private final String description;
    private final String cssClass;
    private final boolean hasLink;
}