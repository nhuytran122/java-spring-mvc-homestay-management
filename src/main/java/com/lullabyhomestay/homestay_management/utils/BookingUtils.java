package com.lullabyhomestay.homestay_management.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;

public class BookingUtils {

    public static void mapAndSetCustomerToBooking(Booking booking, CustomerDTO customerDTO, ModelMapper mapper) {
        if (customerDTO != null) {
            booking.setCustomer(mapper.map(customerDTO, Customer.class));
        }
    }

    public static void validateBooking(Booking booking, CustomerDTO customerDTO) {
        if (booking == null) {
            throw new IllegalArgumentException("Lịch đặt phòng không tồn tại");
        }
        if (customerDTO == null) {
            throw new IllegalArgumentException("Vui lòng đăng nhập để xử dụng chức năng này");
        }
        if (!booking.getCustomer().getCustomerID().equals(customerDTO.getCustomerID())) {
            throw new AccessDeniedException("Lịch đặt phòng này không thuộc quyền truy cập của bạn");
        }
    }
}