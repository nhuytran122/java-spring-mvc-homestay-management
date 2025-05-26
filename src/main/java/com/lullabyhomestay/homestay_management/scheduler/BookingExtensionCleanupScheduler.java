package com.lullabyhomestay.homestay_management.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.service.BookingExtensionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingExtensionCleanupScheduler {

    private final BookingExtensionService bookingExtensionService;

    @Scheduled(fixedDelayString = "${booking.pending.timeout.milliseconds}")
    public void deleteBookingExtensionWithoutPayment() {
        Long count = bookingExtensionService.countAllByPaymentDetailIsNull();
        if (count > 0) {
            bookingExtensionService.deleteAllByPaymentDetailIsNull();
        }
    }
}
