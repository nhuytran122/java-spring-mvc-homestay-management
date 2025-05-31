package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingExtensionRepository;
import com.lullabyhomestay.homestay_management.repository.RoomPricingRepository;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingExtensionService {
    private final BookingExtensionRepository bookingExtensionRepository;
    private final RoomPricingRepository roomPricingRepository;

    @Transactional
    public BookingExtension handleBookingExtensions(BookingExtension bookingExtension) {
        return bookingExtensionRepository.save(bookingExtension);
    }

    public BookingExtension getLatestBookingExtensionByBookingId(Long bookingId) {
        return bookingExtensionRepository
                .findFirstByBooking_BookingIdOrderByCreatedAtDesc(bookingId)
                .orElseThrow(() -> new NotFoundException("Gia hạn thuê phòng"));
    }

    public List<BookingExtension> findAllByPaymentDetailIsNull() {
        return bookingExtensionRepository.findAllByPaymentDetailIsNull();
    }

    @Transactional
    public void deleteByExtensionId(Long extensionId) {
        bookingExtensionRepository.deleteByExtensionId(extensionId);
    }

    public void deleteLatestExtensionByBookingId(Long bookingId) {
        bookingExtensionRepository.findFirstByBooking_BookingIdOrderByCreatedAtDesc(bookingId)
                .ifPresent(latest -> bookingExtensionRepository.deleteByExtensionId(latest.getExtensionId()));
    }

    public BookingExtension getBookingExtensionById(Long id) {
        return bookingExtensionRepository.findByExtensionId(id)
                .orElseThrow(() -> new NotFoundException("Gia hạn thuê phòng"));
    }

    public Long countAllByPaymentDetailIsNull() {
        return bookingExtensionRepository.countByPaymentDetailIsNull();
    }

    @Transactional
    public void deleteAllByPaymentDetailIsNull() {
        bookingExtensionRepository.deleteAllByPaymentDetailIsNull();
    }

    public Double calculateRawTotalAmountBookingExtension(BookingExtension bookingExtension) {
        Booking booking = bookingExtension.getBooking();
        RoomType roomType = booking.getRoom().getRoomType();
        Optional<RoomPricing> roomPricingOpt = roomPricingRepository
                .findFirstByRoomType_RoomTypeIdAndIsDefaultTrue(roomType.getRoomTypeId());
        ;
        if (!roomPricingOpt.isPresent()) {
            throw new NotFoundException("Chính sách giá phòng");
        }
        RoomPricing roomPricing = roomPricingOpt.get();
        Double extraHourPrice = roomPricing.getExtraHourPrice();
        boolean isDorm = roomType.getName().toUpperCase().contains("DORM");
        int guestCount = booking.getGuestCount();

        double adjustedPrice = isDorm ? extraHourPrice * guestCount : extraHourPrice;

        return adjustedPrice * bookingExtension.getExtendedHours();
    }

    public Double calculateFinalExtensionAmount(BookingExtension bookingExtension) {
        Double rawPrice = calculateRawTotalAmountBookingExtension(bookingExtension);
        Double discount = DiscountUtil.calculateDiscountAmount(rawPrice, bookingExtension.getBooking().getCustomer());
        return rawPrice - discount;
    }

}
