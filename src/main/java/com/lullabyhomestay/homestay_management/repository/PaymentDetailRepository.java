package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.PaymentDetail;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    PaymentDetail save(PaymentDetail paymentDetail);

    Boolean existsByBookingService_BookingServiceID(Long bookingServiceID);

    Boolean existsByBookingExtension_ExtensionID(Long extensionID);
}
