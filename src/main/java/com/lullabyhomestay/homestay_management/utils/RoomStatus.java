package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomStatus {
    BUSY("Đã đặt", "busy", true),
    CLEANING("Dọn dẹp", "cleaning", false),
    TEMP_EXTENSION("Tạm giữ phòng", "temp", false);

    private final String description;
    private final String cssClass;
    private final boolean hasLink;
}