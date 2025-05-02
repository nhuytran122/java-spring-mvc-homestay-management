package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingExtension;

@Repository
public interface BookingExtensionRepository extends JpaRepository<BookingExtension, Long> {
    BookingExtension save(BookingExtension bookingExtension);

    List<BookingExtension> findByBooking_BookingID(Long bookingID);

    Optional<BookingExtension> findFirstByBooking_BookingIDOrderByCreatedAtDesc(Long bookingID);

    @Query("""
                SELECT be
                FROM BookingExtension be
                WHERE be.extensionID NOT IN (
                    SELECT pd.bookingExtension.extensionID
                    FROM PaymentDetail pd
                    WHERE pd.bookingExtension IS NOT NULL
                )
            """)
    List<BookingExtension> findAllByPaymentDetailIsNull();

    Long countByPaymentDetailIsNull();

    @Modifying
    @Query("DELETE FROM BookingExtension b WHERE b.paymentDetail IS NULL")
    void deleteAllByPaymentDetailIsNull();

    void deleteByExtensionID(Long extensionID);

    Optional<BookingExtension> findByExtensionID(Long id);

}
