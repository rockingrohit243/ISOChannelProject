package com.example.isochannelproject.de13;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static void main(String[] args) {
        getDate();
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String currentDate = sdf.format(new Date());
        return  currentDate;
    }
}