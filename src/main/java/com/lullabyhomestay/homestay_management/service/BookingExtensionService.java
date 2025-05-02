package com.lullabyhomestay.homestay_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingExtensionRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingExtensionService {
    private final BookingExtensionRepository bookingExtensionRepository;

    public List<BookingExtension> getListBookingExtensionByBookingID(Long bookingID) {
        return bookingExtensionRepository.findByBooking_BookingID(bookingID);
    }

    @Transactional
    public BookingExtension handleBookingExtensions(BookingExtension bookingExtension) {
        return bookingExtensionRepository.save(bookingExtension);
    }

    public BookingExtension getLatestBookingExtensionByBookingID(Long bookingID) {
        return bookingExtensionRepository
                .findFirstByBooking_BookingIDOrderByCreatedAtDesc(bookingID)
                .orElseThrow(() -> new NotFoundException("Gia hạn thuê phòng"));
    }

    public List<BookingExtension> findAllByPaymentDetailIsNull() {
        return bookingExtensionRepository.findAllByPaymentDetailIsNull();
    }

    @Transactional
    public void deleteByExtensionID(Long extensionID) {
        bookingExtensionRepository.deleteByExtensionID(extensionID);
    }

    public void deleteLatestExtensionByBookingID(Long bookingID) {
        bookingExtensionRepository.findFirstByBooking_BookingIDOrderByCreatedAtDesc(bookingID)
                .ifPresent(latest -> bookingExtensionRepository.deleteByExtensionID(latest.getExtensionID()));
    }

    public BookingExtension getBookingExtensionByID(Long id) {
        return bookingExtensionRepository.findByExtensionID(id)
                .orElseThrow(() -> new NotFoundException("Gia hạn thuê phòng"));
    }

    public Long countAllByPaymentDetailIsNull() {
        return bookingExtensionRepository.countByPaymentDetailIsNull();
    }

    @Transactional
    public void deleteAllByPaymentDetailIsNull() {
        bookingExtensionRepository.deleteAllByPaymentDetailIsNull();
    }

}
