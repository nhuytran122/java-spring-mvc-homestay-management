package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.SplitTimeRangeConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchBookingCriteriaDTO {
    private String keyword;
    private String timeRange = "";
    private Long branchId;
    private Long roomTypeId;
    private String status;
    private Long customerId;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchId != null) {
            extraParams.append("&branchId=").append(branchId);
        }
        if (roomTypeId != null) {
            extraParams.append("&roomTypeId=").append(roomTypeId);
        }
        if (customerId != null) {
            extraParams.append("&customerId=").append(customerId);
        }
        if (status != null) {
            extraParams.append("&status=").append(status);
        }
        if (timeRange != null) {
            extraParams.append("&timeRange=").append(URLEncoder.encode(timeRange, StandardCharsets.UTF_8));
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        return extraParams.toString();
    }

    public LocalDateTime getFromTime() {
        return SplitTimeRangeConverter.parseStartDate(timeRange);
    }

    public LocalDateTime getToTime() {
        return SplitTimeRangeConverter.parseEndDate(timeRange);
    }
}