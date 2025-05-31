package com.lullabyhomestay.homestay_management.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingCompletionScheduler {

    private final BookingService bookingService;
    private final CustomerService customerService;

    @Value("${booking.complete.buffer.hours}")
    private int bufferHours;

    @Scheduled(fixedDelay = 1800000) // 30'
    public void completeBookingsAfterCheckOut() {
        List<Booking> confirmedBookings = bookingService.getListBookingByStatus(BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            // // Thêm thời gian đệm để tránh COMPLETE quá sớm (cho trường hợp khách book
            // thêm
            // giờ)
            LocalDateTime bufferCheckOutTime = booking.getCheckOut().plusHours(bufferHours);
            // Chỉ COMPLETE nếu NOW > checkOutTime + buffer
            if (LocalDateTime.now().isAfter(bufferCheckOutTime)) {
                booking.setStatus(BookingStatus.COMPLETED);
                bookingService.handleSaveBooking(booking);
                // UPDATE điểm khi booking COMPLETE
                customerService.updateRewardPointsAndCustomerType(
                        booking.getCustomer().getCustomerId(),
                        booking.getTotalAmount());
                bookingService.handleSaveBooking(booking);
            }
        }
    }
}
