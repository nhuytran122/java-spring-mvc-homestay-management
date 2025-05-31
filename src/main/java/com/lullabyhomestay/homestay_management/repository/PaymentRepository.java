package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lullabyhomestay.homestay_management.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

        Payment save(Payment payment);

        Page<Payment> findAll(Pageable page);

        Page<Payment> findAll(Specification<Payment> spec, Pageable page);

        @EntityGraph(attributePaths = { "paymentDetails", "booking" })
        Optional<Payment> findByPaymentId(Long paymentId);

        @EntityGraph(attributePaths = { "paymentDetails", "booking" })
        Optional<Payment> findByBooking_BookingId(Long bookingId);

        @Query("SELECT SUM(p.totalAmount) FROM Payment p " +
                        "WHERE p.status = 'COMPLETED' " +
                        "AND p.paymentDate BETWEEN :startDate AND :endDate")
        Double getTotalPaymentAmount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}