package com.lullabyhomestay.homestay_management.domain.dto.statistics;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.ReportType;
import com.lullabyhomestay.homestay_management.utils.SplitTimeRangeConverter;

import lombok.Data;

@Data
public class RevenueStatisticsCriteriaDTO {
    private Long branchID;
    private PaymentPurpose purpose;
    private ReportType type = ReportType.DAILY;
    private String timeRange;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (purpose != null) {
            extraParams.append("&purpose=").append(purpose);
        }
        if (type != null) {
            extraParams.append("&type=").append(type);
        }
        if (timeRange != null) {
            extraParams.append("&timeRange=").append(URLEncoder.encode(timeRange, StandardCharsets.UTF_8));
        }
        return extraParams.toString();
    }

    public LocalDate getStartDate() {
        return SplitTimeRangeConverter.parseStartLocalDate(timeRange);
    }

    public LocalDate getEndDate() {
        return SplitTimeRangeConverter.parseEndLocalDate(timeRange);
    }
}
