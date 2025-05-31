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

    Optional<BookingExtension> findFirstByBooking_BookingIdOrderByCreatedAtDesc(Long bookingId);

    @Query("""
                SELECT be
                FROM BookingExtension be
                WHERE be.extensionId NOT IN (
                    SELECT pd.bookingExtension.extensionId
                    FROM PaymentDetail pd
                    WHERE pd.bookingExtension IS NOT NULL
                )
            """)
    List<BookingExtension> findAllByPaymentDetailIsNull();

    Long countByPaymentDetailIsNull();

    @Modifying
    @Query("DELETE FROM BookingExtension b WHERE b.paymentDetail IS NULL")
    void deleteAllByPaymentDetailIsNull();

    void deleteByExtensionId(Long extensionId);

    Optional<BookingExtension> findByExtensionId(Long id);

}
