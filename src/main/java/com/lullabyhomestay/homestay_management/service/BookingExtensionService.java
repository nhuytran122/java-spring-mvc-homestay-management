package com.lullabyhomestay.homestay_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.repository.BookingExtensionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingExtensionService {
    private final BookingExtensionRepository bookingExtensionRepository;

    public List<BookingExtension> getListBookingExtensionByBookingID(Long bookingID) {
        return bookingExtensionRepository.findByBooking_BookingID(bookingID);
    }
}
