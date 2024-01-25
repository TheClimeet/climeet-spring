package com.climeet.climeet_backend.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeConverter {

    public static String convertToDisplayTime(LocalDateTime dateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, currentTime);
        long hours = ChronoUnit.HOURS.between(dateTime, currentTime);

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}