package com.lullabyhomestay.homestay_management.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SplitTimeRangeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static String[] splitTimeRange(String timeRange) {
        if (timeRange == null || timeRange.trim().isEmpty()) {
            return null;
        }
        String[] times = timeRange.split(" - ");
        return times.length == 2 ? times : null;
    }

    public static LocalDateTime parseStartDate(String timeRange) {
        String[] times = splitTimeRange(timeRange);
        if (times != null) {
            try {
                LocalDate date = LocalDate.parse(times[0].trim(), FORMATTER);
                return date.atStartOfDay();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid start date: " + times[0], e);
            }
        }
        return null;
    }

    public static LocalDateTime parseEndDate(String timeRange) {
        String[] times = splitTimeRange(timeRange);
        if (times != null) {
            try {
                LocalDate date = LocalDate.parse(times[1].trim(), FORMATTER);
                return date.atTime(23, 59, 59, 999_000_000);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid end date: " + times[1], e);
            }
        }
        return null;
    }
}
