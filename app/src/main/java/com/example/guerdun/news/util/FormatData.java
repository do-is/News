package com.example.guerdun.news.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by guerdun on 16/10/16 016.
 */

public class FormatData {

    public static String format(int year, int month, int date) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);

        Date dates = new Date(calendar.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(dates);
    }


}
