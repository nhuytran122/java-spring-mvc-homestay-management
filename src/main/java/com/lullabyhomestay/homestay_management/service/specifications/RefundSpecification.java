package com.lullabyhomestay.homestay_management.service.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Refund;
import com.lullabyhomestay.homestay_management.domain.Refund_;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.RefundType;

public class RefundSpecification {
    public static Specification<Refund> refundDateBetween(LocalDateTime fromTime, LocalDateTime toTime) {
        return BaseSpecifications.between(Refund_.CREATED_AT, fromTime, toTime);
    }

    public static Specification<Refund> statusEqual(PaymentStatus paymentStatus) {
        return BaseSpecifications.equal(Refund_.STATUS, paymentStatus);
    }

    public static Specification<Refund> typeEqual(RefundType refundType) {
        return BaseSpecifications.equal(Refund_.REFUND_TYPE, refundType);
    }
}
