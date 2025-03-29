package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lullabyhomestay.homestay_management.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Payment save(Payment payment);

    Page<Payment> findAll(Pageable page);

    Page<Payment> findAll(Specification<Payment> spec, Pageable page);

    Optional<Payment> findByPaymentID(Long paymentID);

}