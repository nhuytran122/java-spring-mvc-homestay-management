package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.PaymentDetail;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    PaymentDetail save(PaymentDetail paymentDetail);

    Boolean existsByBookingService_BookingServiceID(Long bookingServiceID);

    Boolean existsByBookingExtension_ExtensionID(Long extensionID);

    @Query("SELECT pd.paymentPurpose, SUM(pd.finalAmount) FROM PaymentDetail pd " +
            "JOIN pd.payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY pd.paymentPurpose")
    List<Object[]> getPaymentAmountByPurpose(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
