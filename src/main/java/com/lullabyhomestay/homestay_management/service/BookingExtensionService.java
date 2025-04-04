package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingExtensionRepository;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingExtensionService {
    private final BookingExtensionRepository bookingExtensionRepository;
    private final BookingService bookingService;

    public List<BookingExtension> getListBookingExtensionByBookingID(Long bookingID) {
        return bookingExtensionRepository.findByBooking_BookingID(bookingID);
    }

    @Transactional
    public BookingExtension handleBookingExtensions(BookingExtension bookingExtension) {

        Booking currentBooking = bookingExtension.getBooking();
        // Update giá
        Double rawExtensionPrice = bookingExtension.getTotalAmount();
        Double discountAmount = DiscountUtil.calculateDiscountAmount(rawExtensionPrice,
                bookingExtension.getBooking().getCustomer());
        Double amountExtensionPrice = rawExtensionPrice - discountAmount;

        currentBooking.setTotalAmount(currentBooking.getTotalAmount() + amountExtensionPrice);
        bookingService.handleSaveBooking(currentBooking);

        // roomStatusHistoryService.handleBookingExtensions(bookingExtension);
        return bookingExtensionRepository.save(bookingExtension);
    }

    public BookingExtension getLatestBookingExtensionByBookingID(Long bookingID) {
        Optional<BookingExtension> bookingExtensionOpt = bookingExtensionRepository
                .findFirstByBooking_BookingIDOrderByCreatedAtDesc(bookingID);
        if (!bookingExtensionOpt.isPresent()) {
            throw new NotFoundException("Gia hạn thuê phòng");
        }
        return bookingExtensionOpt.get();
    }

    public List<BookingExtension> findAllByPaymentDetailIsNull() {
        return bookingExtensionRepository.findAllByPaymentDetailIsNull();
    }

    public boolean canExtendBooking(Long bookingID) {
        Optional<BookingExtension> lastExtension = bookingExtensionRepository
                .findFirstByBooking_BookingIDOrderByCreatedAtDesc(bookingID);
        if (!lastExtension.isPresent()) {
            return true;
        }
        if (lastExtension.get().getPaymentDetail() == null)
            return false;
        return true;
    }

    @Transactional
    public void deleteByExtensionID(Long extensionID) {
        bookingExtensionRepository.deleteByExtensionID(extensionID);
    }

}
