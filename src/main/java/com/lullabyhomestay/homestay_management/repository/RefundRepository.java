package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long>, JpaSpecificationExecutor<Refund> {
        Page<Refund> findAll(Pageable page);

        Page<Refund> findAll(Specification<Refund> spec, Pageable page);

        @Query("SELECT SUM(r.payment.totalAmount) FROM Refund r " +
                        "WHERE r.status = 'REFUNDED' " +
                        "AND r.createdAt BETWEEN :startDate AND :endDate")
        Double getTotalRefundedAmount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT pd.paymentPurpose, SUM(r.refundAmount) FROM Refund r " +
                        "JOIN r.payment p JOIN p.paymentDetails pd " +
                        "WHERE r.status = 'REFUNDED' " +
                        "AND r.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY pd.paymentPurpose")
        List<Object[]> getRefundedAmountByPurpose(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}
