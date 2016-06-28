package com.xiye.sclibrary.utils;

import android.content.Context;

import com.xiye.sclibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static String getDateTime() {
        String dateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        dateTime = sdf.format(new Date());
        return dateTime;
    }

    public static Date convertTimeToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String getDate() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());
        return date;
    }

    public static String getTime() {
        String time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(new Date());
        return time;
    }

    public static String getWeek(Context context) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String weekDay = "";
        switch (day) {
            case Calendar.SUNDAY:
                weekDay = context.getString(R.string.weekday_sunday);
                break;
            case Calendar.MONDAY:
                weekDay = context.getString(R.string.weekday_monday);
                break;
            case Calendar.TUESDAY:
                weekDay = context.getString(R.string.weekday_tuesday);
                break;
            case Calendar.WEDNESDAY:
                weekDay = context.getString(R.string.weekday_wednesday);
                break;
            case Calendar.THURSDAY:
                weekDay = context.getString(R.string.weekday_thursday);
                break;
            case Calendar.FRIDAY:
                weekDay = context.getString(R.string.weekday_friday);
                break;
            case Calendar.SATURDAY:
                weekDay = context.getString(R.string.weekday_saturday);
                break;

        }
        return weekDay;
    }

    public static String getYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getMonth() {
        return String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    public static String getDay() {
        return String
                .valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static String getHour() {
        return String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    public static String getMin() {
        return String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
    }

    public static String getSec() {
        return String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
    }

    public static boolean isToday(String time) {
        boolean isToday = false;
        Date date = convertTimeToDate(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        if (getDate().equals(dateStr)) {
            isToday = true;
        }
        return isToday;
    }

}
