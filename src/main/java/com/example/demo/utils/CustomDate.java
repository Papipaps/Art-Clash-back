package com.example.demo.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomDate {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
}
