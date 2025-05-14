package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.utils.RoomPricingType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingPriceDTO {
    private Long roomPricingID;
    private double totalPrice;
    private RoomPricingType pricingType;
    private double totalHours;
    private double totalDays;
    private double totalNights;
}
