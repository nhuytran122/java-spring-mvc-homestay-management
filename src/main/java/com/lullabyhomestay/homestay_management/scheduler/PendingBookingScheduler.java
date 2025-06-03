package com.lullabyhomestay.homestay_management.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.service.BookingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PendingBookingScheduler {

    private final BookingService bookingService;

    @Value("${booking.pending.timeout.minutes}")
    private int timeoutMinutes;

    @Scheduled(fixedDelayString = "${booking.pending.timeout.milliseconds}")
    public void cancelPendingBookings() {
        List<Booking> pendingBookings = bookingService.findPendingBookingsBefore(timeoutMinutes);
        for (Booking booking : pendingBookings) {
            Long bookingID = booking.getBookingId();
            bookingService.cancelBooking(bookingID);
        }
    }
}
