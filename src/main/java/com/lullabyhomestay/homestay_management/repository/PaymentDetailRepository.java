package com.lullabyhomestay.homestay_management.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.PaymentDetail;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    PaymentDetail save(PaymentDetail paymentDetail);

    Boolean existsByBookingService_BookingServiceId(Long bookingServiceId);

    Boolean existsByBookingExtension_ExtensionId(Long extensionId);

    @Query("SELECT pd.paymentPurpose, SUM(pd.finalAmount) FROM PaymentDetail pd " +
            "JOIN pd.payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY pd.paymentPurpose")
    List<Object[]> getPaymentAmountByPurpose(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
                SELECT SUM(pd.finalAmount)
                FROM PaymentDetail pd
                JOIN pd.payment p
                JOIN p.booking b
                JOIN b.room r
                JOIN r.branch br
                WHERE p.status = 'COMPLETED'
                  AND p.paymentDate >= :start AND p.paymentDate < :end
                  AND (:branchId IS NULL OR br.branchId = :branchId)
                  AND (:purpose IS NULL OR pd.paymentPurpose = :purpose)
            """)
    BigDecimal sumRevenue(
            @Param("branchId") Long branchId,
            @Param("purpose") PaymentPurpose purpose,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
