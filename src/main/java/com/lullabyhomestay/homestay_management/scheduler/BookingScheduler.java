package com.lullabyhomestay.homestay_management.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.service.BookingExtensionService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.EmailService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final BookingExtensionService bookingExtensionService;
    private final CustomerService customerService;
    private final EmailService emailService;

    @Value("${booking.pending.timeout.minutes}")
    private int timeoutMinutes;

    @Value("${booking.complete.buffer.hours}")
    private int bufferHours;

    @Scheduled(fixedDelayString = "${booking.pending.timeout.milliseconds}")
    public void cancelPendingBookings() {
        List<Booking> pendingBookings = bookingService.findPendingBookingsBefore(timeoutMinutes);
        for (Booking booking : pendingBookings) {
            Long bookingID = booking.getBookingID();
            booking.setStatus(BookingStatus.CANCELLED);
            bookingService.handleSaveBooking(booking);

            // Xóa các dữ liệu liên quan đến Booking: lịch trình
            roomStatusHistoryService.deleteByBookingID(bookingID);
        }
    }

    @Scheduled(fixedDelay = 1800000) // 30'
    public void completeBookingsAfterCheckOut() {
        List<Booking> confirmedBookings = bookingService.getListBookingByStatus(BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            LocalDateTime checkOutTime = booking.getCheckOut();
            // Thêm thời gian đệm để tránh COMPLETE quá sớm (cho trường hợp khách book thêm
            // giờ)
            LocalDateTime bufferCheckOutTime = checkOutTime.plusHours(bufferHours);

            // Chỉ COMPLETE nếu NOW > checkOutTime + buffer
            if (LocalDateTime.now().isAfter(bufferCheckOutTime)) {
                booking.setStatus(BookingStatus.COMPLETED);
                bookingService.handleSaveBooking(booking);

                // UPDATE điểm khi booking COMPLETE
                customerService.updateRewardPointsAndCustomerType(
                        booking.getCustomer().getCustomerID(),
                        booking.getTotalAmount());
                bookingService.handleSaveBooking(booking);
            }
        }
    }

    @Scheduled(fixedDelayString = "${booking.pending.timeout.milliseconds}")
    public void deleteBookingExtensionWithoutPayment() {
        Long count = bookingExtensionService.countAllByPaymentDetailIsNull();

        if (count > 0) {
            bookingExtensionService.deleteAllByPaymentDetailIsNull();
        }
    }

    // @Scheduled(fixedDelay = 3000)
    @Scheduled(fixedDelay = 300000)
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
                e.getMessage();
            }

        }
    }
}