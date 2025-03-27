package com.lullabyhomestay.homestay_management.utils;

import com.lullabyhomestay.homestay_management.domain.Customer;

public class DiscountUtil {
    public static double calculateDiscountAmount(double originalPrice, Customer customer) {
        if (customer == null || customer.getCustomerType() == null) {
            return 0.0;
        }
        double discountRate = customer.getCustomerType().getDiscountRate();
        return (discountRate > 0) ? originalPrice * discountRate / 100 : 0.0;
    }
}
