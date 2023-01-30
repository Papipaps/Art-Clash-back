package com.example.demo.utils;

import java.time.LocalDateTime;

public class CustomDateUtils {

    public static CustomDate getDateWithTime(LocalDateTime createdDate) {
        int year = createdDate.getYear();
        int month = createdDate.getMonthValue();
        int day = createdDate.getDayOfMonth();
        int hour = createdDate.getHour();
        int minute = createdDate.getMinute();
        int second = createdDate.getSecond();
        return CustomDate.builder()
                .day(day)
                .month(month)
                .year(year)
                .hour(hour)
                .minute(minute)
                .second(second).build();
    }
}
