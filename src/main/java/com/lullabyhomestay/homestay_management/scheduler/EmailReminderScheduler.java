package com.lullabyhomestay.homestay_management.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailReminderScheduler {

    private final BookingService bookingService;
    private final EmailService emailService;

    // @Scheduled(fixedDelay = 900000) // 15'
    @Scheduled(fixedDelay = 15000) // to demo
    public void sendReminderEmails() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
        // Lấy all các booking đã CONFIRMED và check-in sắp đến trong vòng 30
        // phút
        List<Booking> bookings = bookingService.findBookingsToSendReminder(now, thirtyMinutesLater);
        for (Booking booking : bookings) {
            try {
                emailService.sendCheckInReminderEmail(booking);
                booking.setHasSentReminder(true);
                bookingService.handleSaveBooking(booking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
