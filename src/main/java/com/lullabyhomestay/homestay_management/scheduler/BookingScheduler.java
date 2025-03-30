package com.lullabyhomestay.homestay_management.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.service.BookingExtraService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;
    private final RoomStatusHistoryService roomStatusHistoryService;

    @Value("${booking.pending.timeout.minutes}")
    private int timeoutMinutes;

    @Scheduled(fixedDelayString = "${booking.pending.timeout.milliseconds}")
    public void cancelPendingBookings() {
        List<Booking> pendingBookings = bookingService.getListBookingByStatus(BookingStatus.PENDING);
        for (Booking booking : pendingBookings) {
            if (booking.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(timeoutMinutes))) {
                Long bookingID = booking.getBookingID();
                booking.setStatus(BookingStatus.CANCELLED);
                bookingService.handleSaveBooking(booking);

                // Xóa các dữ liệu liên quan đến Booking: lịch trình
                roomStatusHistoryService.deleteByBookingID(bookingID);
            }
        }
    }
}