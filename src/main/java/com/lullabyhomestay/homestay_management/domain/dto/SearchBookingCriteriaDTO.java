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
    private Long branchID;
    private Long roomTypeID;
    private String status;
    private Long customerID;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (roomTypeID != null) {
            extraParams.append("&roomTypeID=").append(roomTypeID);
        }
        if (customerID != null) {
            extraParams.append("&customerID=").append(customerID);
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