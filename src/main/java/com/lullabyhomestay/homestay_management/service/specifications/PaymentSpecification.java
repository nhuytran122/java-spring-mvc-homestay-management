package com.lullabyhomestay.homestay_management.service.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.Payment_;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

public class PaymentSpecification {
    public static Specification<Payment> paymentDateBetween(LocalDateTime fromTime, LocalDateTime toTime) {
        return BaseSpecifications.between(Payment_.PAYMENT_DATE, fromTime, toTime);
    }

    public static Specification<Payment> statusEqual(PaymentStatus paymentStatus) {
        return BaseSpecifications.equal(Payment_.STATUS, paymentStatus);
    }

    public static Specification<Payment> typeEqual(PaymentType paymentType) {
        return BaseSpecifications.equal(Payment_.PAYMENT_TYPE, paymentType);
    }
}
