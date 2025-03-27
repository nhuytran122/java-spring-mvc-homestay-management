package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment save(Payment payment);

}