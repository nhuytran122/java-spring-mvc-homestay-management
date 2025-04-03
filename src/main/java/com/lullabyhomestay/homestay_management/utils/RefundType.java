package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefundType {
    FULL("Hoàn tiền 100%", "Nếu hủy phòng trước thời gian check-in 7 ngày hoặc sớm hơn."),
    PARTIAL_70("Hoàn tiền 70%", "Nếu hủy phòng trước thời gian check-in trong vòng từ 3 đến 7 ngày."),
    PARTIAL_30("Hoàn tiền 30%", "Nếu hủy phòng trong vòng 3 ngày trước khi check-in.");

    private String displayName;
    private String descriptionPolicy;
}
