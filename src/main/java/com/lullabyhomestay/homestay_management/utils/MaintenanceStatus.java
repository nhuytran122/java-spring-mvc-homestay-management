package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MaintenanceStatus {
    PENDING("Chờ xử lý"),
    IN_PROGRESS("Đang xử lý"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy"),
    ON_HOLD("Tạm hoãn");

    private String displayName;
}