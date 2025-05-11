package com.lullabyhomestay.homestay_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    DAILY("Theo ngày"),
    MONTHLY("Theo tháng"),
    YEARLY("Theo năm");

    private String displayName;
}