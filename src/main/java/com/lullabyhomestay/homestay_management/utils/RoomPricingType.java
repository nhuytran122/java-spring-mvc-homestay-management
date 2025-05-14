package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomPricingType {
    HOURLY,
    OVERNIGHT,
    DAILY,
    DEFAULT,
    OTHER,
    MIXED
}
