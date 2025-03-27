package com.lullabyhomestay.homestay_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingExtension;

@Repository
public interface BookingExtensionRepository extends JpaRepository<BookingExtension, Long> {
    BookingExtension save(BookingExtension bookingExtension);

    List<BookingExtension> findByBooking_BookingID(Long bookingID);

    @Query("SELECT be FROM BookingExtension be WHERE be.booking.bookingID = :bookingID " +
            "AND NOT EXISTS (SELECT pd FROM PaymentDetail pd WHERE pd.bookingExtension.extensionID = be.extensionID)")
    List<BookingExtension> findBookingExtensionWithoutPaymentDetail(@Param("bookingID") Long bookingID);

}
