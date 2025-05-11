package com.lullabyhomestay.homestay_management.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SplitTimeRangeConverter {

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static String[] splitTimeRange(String timeRange) {
        if (timeRange == null || timeRange.trim().isEmpty()) {
            return null;
        }
        String[] times = timeRange.split(" - ");
        return times.length == 2 ? times : null;
    }

    // Xét str theo định dạng có thể có ngày và giờ or chỉ ngày
    private static LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = dateTimeStr.contains(":") ? FORMATTER_DATETIME : FORMATTER_DATE;

        try {
            if (formatter == FORMATTER_DATETIME) {
                return LocalDateTime.parse(dateTimeStr.trim(), FORMATTER_DATETIME);
            } else {
                LocalDate date = LocalDate.parse(dateTimeStr.trim(), FORMATTER_DATE);
                return date.atStartOfDay();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date/time format: " + dateTimeStr, e);
        }
    }

    public static LocalDateTime parseStartDate(String timeRange) {
        String[] times = splitTimeRange(timeRange);
        return (times != null) ? parseDateTime(times[0]) : null;
    }

    public static LocalDateTime parseEndDate(String timeRange) {
        String[] times = splitTimeRange(timeRange);
        if (times != null) {
            try {
                LocalDateTime endDateTime = parseDateTime(times[1]);
                return endDateTime.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid end date: " + times[1], e);
            }
        }
        return null;
    }

    public static LocalDate parseStartLocalDate(String timeRange) {
        LocalDateTime startDateTime = parseStartDate(timeRange);
        return (startDateTime != null) ? startDateTime.toLocalDate() : null;
    }

    public static LocalDate parseEndLocalDate(String timeRange) {
        LocalDateTime endDateTime = parseEndDate(timeRange);
        return (endDateTime != null) ? endDateTime.toLocalDate() : null;
    }
}
