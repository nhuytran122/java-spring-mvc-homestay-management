package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionType {
    CREATE_BOOKING("Tạo đơn đặt phòng"),
    CANCEL_BOOKING("Hủy đơn đặt phòng"),
    CREATE_BOOKING_EXTENSION("Tạo gia hạn đặt phòng"),
    DELETE_BOOKING_EXTENSION("Xóa gia hạn đặt phòng"),
    CREATE_BOOKING_SERVICE("Tạo đơn đặt dịch vụ"),
    UPDATE_BOOKING_SERVICE("Cập nhật đơn đặt dịch vụ"),
    DELETE_BOOKING_SERVICE("Xóa đơn đặt dịch vụ");
    // CONFIRM_PAYMENT("Xác nhận thanh toán");

    private String displayName;
}
