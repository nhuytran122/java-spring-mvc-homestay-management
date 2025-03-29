package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchPaymentCriteriaDTO {
    private String keyword;
    private String timeRange = "";
    private String status;
    private String type;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (type != null) {
            extraParams.append("&type=").append(type);
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
