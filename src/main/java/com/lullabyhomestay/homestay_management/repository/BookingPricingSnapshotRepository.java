package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingPricingSnapshot;

@Repository
public interface BookingPricingSnapshotRepository extends JpaRepository<BookingPricingSnapshot, Long> {
    void deleteByBooking_BookingID(Long bookingID);
}
