package com.lazydsr.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * UtilDateTime
 * PROJECT_NAME: lazy-utils
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.time
 * Created by Lazy on 2017/7/9 22:17
 * Version: 1.0
 * Info: 日期时间工具类的升级版
 */
public class UtilDateTime {
    private static SimpleDateFormat sdf = null;
    private static Calendar calendar = Calendar.getInstance();
    public static String DATE_FORMATE = "yyyy-MM-dd";
    public static String TIME_FORMATE = "HH-mm-ss";
    public static String DATE_TIME_FORMATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 判断是否为闰年
     *
     * @param year 年  数字
     * @return true or false
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? true : false;
    }

    /**
     * 判断自定义日期和自定义格式是否为当月第一天
     *
     * @param dateStr     自定义日期字符串
     * @param dateFormate 自定义日期格式
     * @return true or false
     */
    public static boolean isFirstDayOfMonth(String dateStr, String dateFormate) {
        return isFirstDayOfMonth(getDateByStr(dateStr, dateFormate));
    }

    /**
     * 判断是否是当月第一天
     *
     * @param date 日期对象
     * @return true or false
     */
    public static boolean isFirstDayOfMonth(Date date) {
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
            return true;
        else
            return false;
    }

    /**
     * 判断自定义日期和自定义格式是否为当月最后一天
     *
     * @param dateStr     自定义日期字符串
     * @param dateFormate 自定义日期格式
     * @return true or false
     */
    public static boolean isLastDayOfMonth(String dateStr, String dateFormate) {
        return isLastDayOfMonth(getDateByStr(dateStr, dateFormate));
    }

    /**
     * 判断是否是当月最后一天
     *
     * @param date 日期对象
     * @return true or false
     */
    public static boolean isLastDayOfMonth(Date date) {
        calendar.setTime(date);
        if (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
            return true;
        else
            return false;
    }

    /**
     * 判断自定义日期和自定义格式是否为当年第一天
     *
     * @param dateStr     自定义日期字符串
     * @param dateFormate 自定义日期格式
     * @return true or false
     */
    public static boolean isFirstDayOfYear(String dateStr, String dateFormate) {
        return isFirstDayOfYear(getDateByStr(dateStr, dateFormate));
    }

    /**
     * 判断是否是当年第一天
     *
     * @param date 日期对象
     * @return true or false
     */
    public static boolean isFirstDayOfYear(Date date) {
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_YEAR) == 1)
            return true;
        else
            return false;
    }

    /**
     * 判断自定义日期和自定义格式是否为当年最后一天
     *
     * @param dateStr     自定义日期字符串
     * @param dateFormate 自定义日期格式
     * @return true or false
     */
    public static boolean isLastDayOfYear(String dateStr, String dateFormate) {
        return isLastDayOfYear(getDateByStr(dateStr, dateFormate));
    }

    /**
     * 判断是否是当年最后一天
     *
     * @param date 日期对象
     * @return true or false
     */
    public static boolean isLastDayOfYear(Date date) {
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
            return true;
        else
            return false;
    }

    /**
     * 获取当前日期的下一天的日期
     *
     * @return 计算后的日期
     */
    public static Date getNextDay() {
        return getNextOrPrevDay(new Date(), 1);
    }

    /**
     * 获取当前日期的前一天的日期
     *
     * @return 计算后的日期
     */
    public static Date getPrevDay() {
        return getNextOrPrevDay(new Date(), -1);
    }

    /**
     * 获取当前日期的n天的日期 n可为负数
     *
     * @param date 指定日期
     * @param days 天数 可为负数
     * @return 计算后的日期
     */
    public static Date getNextOrPrevDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 根据日期字符串获取日期对象
     *
     * @param dateStr 日期字符串  yyyy-MM-dd HH:mm:ss
     * @return 日期对象
     */
    public static Date getDateByStr(String dateStr) {
        Date date = getDateByStr(dateStr, DATE_TIME_FORMATE);
        return date;
    }

    /**
     * 根据日期字符串和日期格式获取日期对象
     *
     * @param dateStr      日期字符串
     * @param formatString 字符串格式
     * @return 日期对象
     */
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

    /**
     * 获取当前时间
     *
     * @return 时间字符串
     */
    public static String getCurrTime() {
        return getDateTimeByDate(new Date(), TIME_FORMATE);
    }


    /**
     * 获取当前日期
     *
     * @return 日期字符串
     */
    public static String getCurrDate() {
        return getDateTimeByDate(new Date(), DATE_FORMATE);
    }


    /**
     * 获取当前日期和时间
     *
     * @return 日期时间字符串
     */
    public static String getCurrDateTime() {
        return getCurrDateTime(DATE_TIME_FORMATE);
    }

    /**
     * 获取指定格式的当前日期和时间字符串
     *
     * @param datetimeFormate 日期时间格式
     * @return 自定义格式的日期时间字符串
     */

    public static String getCurrDateTime(String datetimeFormate) {
        return getDateTimeByDate(new Date(), datetimeFormate);
    }

    /**
     * 根据日期对象和日期格式获取日期字符串
     *
     * @param date            日期对象
     * @param datetimeFormate 日期时间格式
     * @return 所需日期时间字符串
     */
    public static String getDateTimeByDate(Date date, String datetimeFormate) {
        try {
            sdf = new SimpleDateFormat(datetimeFormate);
        } catch (Exception e) {
            return "时间字符串错误";
        }

        return sdf.format(date);
    }

    /**
     * 获取两个日期时间的差值,包括年月日
     *
     * @param beforeTime 前一个时间
     * @param afterTime  后一个时间
     * @return 时间差值, 单位为毫秒(ms)
     */
    public static long getDiffDateTime(Date beforeTime, Date afterTime) {
        try {
            long diff = afterTime.getTime() - beforeTime.getTime();
            return diff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取当前年
     *
     * @return 年
     */
    public static int getCurrYear() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return 月
     */
    public static int getCurrMonth() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日
     *
     * @return 日
     */
    public static int getCurrDay() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     *
     * @return 小时
     */
    public static int getCurrHour() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前分钟
     *
     * @return 分钟
     */
    public static int getCurrMinute() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒
     *
     * @return 秒
     */
    public static int getCurrSecond() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.SECOND);
    }

}
