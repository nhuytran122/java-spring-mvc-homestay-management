package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.BookingServices_;
import com.lullabyhomestay.homestay_management.domain.Service_;
import com.lullabyhomestay.homestay_management.utils.BookingServiceStatus;

public class BookingServiceSpecification {
    public static Specification<BookingServices> serviceNameLike(String keyword) {
        return BaseSpecifications.likeJoin(BookingServices_.SERVICE, Service_.SERVICE_NAME, keyword);
    }

    public static Specification<BookingServices> isAdditionalEqual(Boolean isAdditional) {
        return BaseSpecifications.equalJoin(
                BookingServices_.SERVICE,
                Service_.IS_PREPAID,
                isAdditional);
    }

    public static Specification<BookingServices> statusEqual(BookingServiceStatus status) {
        return BaseSpecifications.equal(BookingServices_.STATUS, status);
    }
}
