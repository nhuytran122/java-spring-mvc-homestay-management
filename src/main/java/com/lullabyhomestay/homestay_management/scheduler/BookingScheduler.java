package com.lullabyhomestay.homestay_management.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lullabyhomestay.homestay_management.domain.ActionLog;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.service.ActionLogService;
import com.lullabyhomestay.homestay_management.service.BookingExtensionService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.utils.ActionType;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;
import com.lullabyhomestay.homestay_management.utils.ObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final BookingExtensionService bookingExtensionService;
    private final CustomerService customerService;
    private final ActionLogService actionLogService;

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

            ActionLog actionLog = new ActionLog();
            actionLog.setActionType(ActionType.CANCEL_BOOKING);
            actionLog.setObjectID(bookingID);
            actionLog.setObjectType(ObjectType.BOOKING);
            actionLogService.handleSaveLog(actionLog);
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
        // List<BookingExtension> extensionsToDelete =
        // bookingExtensionService.findAllByPaymentDetailIsNull();

        // for (BookingExtension extension : extensionsToDelete) {
        // // Xóa BookingExtension
        // bookingExtensionService.deleteByExtensionID(extension.getExtensionID());

        // ActionLog actionLog = new ActionLog();
        // actionLog.setActionType(ActionType.DELETE_BOOKING_EXTENSION);
        // actionLog.setObjectID(extension.getExtensionID());
        // actionLog.setObjectType(ObjectType.BOOKING_EXTENSION);
        // actionLogService.handleSaveLog(actionLog);
        // }

        Long count = bookingExtensionService.countAllByPaymentDetailIsNull();

        if (count > 0) {
            bookingExtensionService.deleteAllByPaymentDetailIsNull();
        }
    }
}