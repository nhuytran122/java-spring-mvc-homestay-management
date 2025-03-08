package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.CustomerType_;
import com.lullabyhomestay.homestay_management.domain.Customer_;

public class CustomerSpecifications {
    public static Specification<Customer> hasType(Long typeID) {
        return BaseSpecifications.equalJoin(Customer_.CUSTOMER_TYPE, CustomerType_.CUSTOMER_TYPE_ID, typeID);
    }

    public static Specification<Customer> isLocked(Boolean isLocked) {
        return BaseSpecifications.equal(Customer_.IS_LOCKED, isLocked);
    }

    public static Specification<Customer> nameLike(String keyword) {
        return BaseSpecifications.like(Customer_.FULL_NAME, keyword);
    }

    public static Specification<Customer> addressLike(String keyword) {
        return BaseSpecifications.like(Customer_.ADDRESS, keyword);
    }

    public static Specification<Customer> emailEqual(String keyword) {
        return BaseSpecifications.equal(Customer_.EMAIL, keyword);
    }

    public static Specification<Customer> phoneEqual(String keyword) {
        return BaseSpecifications.equal(Customer_.PHONE, keyword);
    }
}
