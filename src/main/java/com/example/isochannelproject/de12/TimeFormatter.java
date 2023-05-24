package com.example.isochannelproject.de12;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {
    public static void main(String[] args) {

        getTime();

    }
    public static String getTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String currentTime = sdf.format(new Date());
        return currentTime;
    }
}