package com.lazy.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * Coding......
 * Created by D.SR on 2017/3/10.
 * Info:升级为@Link:UtilDateTime.class
 */
@Deprecated
public class UtilTime {
    static SimpleDateFormat sdf = null;
    static Calendar now = Calendar.getInstance();

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? true : false;
    }

    public static Date getDateByStr(String dateStr) {
        Date date = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateByStr(String dateStr, String formatString) {
        Date date = null;
        sdf = new SimpleDateFormat(formatString);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrTime() {
        sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCurrDate() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrDate(String dateFormate) {
        try {
            sdf = new SimpleDateFormat(dateFormate);
        } catch (Exception e) {
            return "日期格式字符串错误";
        }
        return sdf.format(new Date());
    }

    public static String getCurrDateTime() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCurrDateTime(String timeFormate) {
        try {
            sdf = new SimpleDateFormat(timeFormate);
        } catch (Exception e) {
            return "时间字符串错误";
        }

        return sdf.format(new Date());
    }

    public static String getCurrYear() {
        return now.get(Calendar.YEAR) + "";
    }

    public static String getCurrMonth() {
        return now.get(Calendar.MONTH) + 1 + "";
    }

    public static String getCurrDay() {
        return now.get(Calendar.DAY_OF_MONTH) + "";
    }

    public static String getCurrHour() {
        return now.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getCurrMinute() {
        return now.get(Calendar.MINUTE) + "";
    }

    public static String getCurrSecond() {
        return now.get(Calendar.SECOND) + "";
    }

}
