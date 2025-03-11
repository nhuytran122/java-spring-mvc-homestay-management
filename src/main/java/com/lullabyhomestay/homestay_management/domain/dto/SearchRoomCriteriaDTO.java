package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchRoomCriteriaDTO {
    private Long roomTypeID;
    private Long branchID;
    private String timeRange;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (roomTypeID != null) {
            extraParams.append("&roomTypeID=").append(roomTypeID);
        }
        if (timeRange != null && !timeRange.isEmpty()) {
            extraParams.append("&timeRange=").append(timeRange);
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return LocalDateTime.parse(times[0].trim(), formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid start time: " + times[0], e);
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return LocalDateTime.parse(times[1].trim(), formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid end time: " + times[1], e);
            }
        }
        return null;
    }
}
