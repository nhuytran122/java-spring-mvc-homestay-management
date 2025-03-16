package com.lullabyhomestay.homestay_management.service;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingExtraService {
    private final BookingServiceRepository bookingServiceRepo;
    private final BookingService bookingService;

    public BookingServices handleSaveBookingServiceExtra(BookingServices bookingService) {
        Booking currentBooking = this.bookingService.getBookingByID(bookingService.getBooking().getBookingID());
        Double totalAmount = currentBooking.getTotalAmount() + bookingService.getTotalPrice();
        currentBooking.setTotalAmount(totalAmount);
        this.bookingService.handleSaveBooking(currentBooking);
        return this.bookingServiceRepo.save(bookingService);
    }
}
