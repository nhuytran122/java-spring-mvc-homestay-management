package com.lullabyhomestay.homestay_management.domain.dto;

import java.util.List;

import com.lullabyhomestay.homestay_management.domain.BookingServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BookingServiceRequestDTO {
    private Long bookingID;
    private List<BookingServices> services;
}
