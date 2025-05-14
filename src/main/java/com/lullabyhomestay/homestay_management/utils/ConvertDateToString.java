package com.lullabyhomestay.homestay_management.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertDateToString {
    public static String getFormattedDateTime(LocalDateTime date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return date.format(formatter);
        }
        return "";
    }

    public static String getFormattedLocalDate(LocalDate date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatter);
        }
        return "";
    }
}
