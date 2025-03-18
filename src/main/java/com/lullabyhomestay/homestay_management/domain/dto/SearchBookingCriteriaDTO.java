package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String sort;
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
        if (sort != null) {
            extraParams.append("&sort=").append(sort);
        }
        return extraParams.toString();
    }

    public LocalDateTime getFromTime() {
        if (timeRange == null || timeRange.trim().isEmpty()) {
            return null;
        }
        String[] times = timeRange.split(" - ");
        if (times.length == 2) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(times[0].trim(), formatter);
                return date.atStartOfDay();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid start date: " + times[0], e);
            }
        }
        return null;
    }

    public LocalDateTime getToTime() {
        if (timeRange == null || timeRange.trim().isEmpty()) {
            return null;
        }
        String[] times = timeRange.split(" - ");
        if (times.length == 2) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(times[1].trim(), formatter);
                return date.atTime(23, 59, 59, 999_000_000);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid end date: " + times[1], e);
            }
        }
        return null;
    }
}