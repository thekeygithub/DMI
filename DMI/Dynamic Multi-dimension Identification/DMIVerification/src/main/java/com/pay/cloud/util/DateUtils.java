package com.pay.cloud.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class DateUtils {
    /**
     * 根据一定格式的字符串，得到日期对象
     *
     * @param str    日期字符串
     * @param format 格式
     * @return 日期
     */
    public static Date strToDate(String str, String format) {
        Date dateTime = null;
        try {
            if (!(str == null || str.equals(""))) {
                SimpleDateFormat formater = new SimpleDateFormat(format);
                dateTime = formater.parse(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 将日期转换成字符串
     *
     * @param date   日期对象
     * @param format 格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String format) {
        String str = "";
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            str = formatter.format(date);
        }
        return str;
    }

    /**
     * 按照一定格式得到当天日期
     *
     * @param format 格式
     * @return 日期字符串
     */
    public static String getNowDate(String format) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(new Date());
        //int effd = -1;
        Date enddate = nowTime.getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(enddate);
    }

    /**
     * 按照一定格式，得到当月的第一天日期
     *
     * @param format 格式
     * @return 日期字符串
     */
    @SuppressWarnings("static-access")
    public static String getNowMonthFirstDay(String format) {
        GregorianCalendar date = new GregorianCalendar();
        date.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat df = new SimpleDateFormat(format);
        date.set(date.get(date.YEAR), date.get(date.MONTH), date.get(date.DATE));
        return df.format(date.getTime());
    }

    /**
     * 按照一定格式，得到前一天日期
     *
     * @param format 格式
     * @return 日期字符串
     */
    public static String getYesterdayDate(String format) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(new Date());
        int effd = -1;
        nowTime.add(Calendar.DATE, effd);
        Date enddate = nowTime.getTime();

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(enddate);
    }

    /**
     * 按照一定格式，得到明天日期
     *
     * @param format 格式
     * @return 日期字符串
     */
    public static String getTomorrowDate(String format) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(new Date());
        int effd = 1;
        nowTime.add(Calendar.DATE, effd);
        Date enddate = nowTime.getTime();

        // Date dt  = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(enddate);
    }

    /**
     * 按照一定格式，得到上个月的今天日期
     *
     * @param format 格式
     * @return 日期字符串
     */
    @SuppressWarnings("static-access")
    public static String getLastMon(String format) {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);
        gc.add(2, -1);
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
        return df.format(gc.getTime());
    }

    /**
     * 日期加天数
     *
     * @param date    日期
     * @param addDays 增加天数
     * @return 日期
     */
    public static Date dateAddDays(Date date, int addDays) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDays);
        return cal.getTime();
    }

    /**
     * 日期加分钟
     *
     * @param date    日期
     * @param minutes 增加分钟
     * @return 日期
     */
    public static Date dateAddMinutess(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    /**
     * 日期加月数
     *
     * @param date      日期
     * @param addMonths 增加月数
     * @return 日期
     */
    public static Date dateAddMonths(Date date, int addMonths) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, addMonths);
        return cal.getTime();
    }

    /**
     * 日期加星期数
     *
     * @param date     日期
     * @param addWeeks 增加星期数
     * @return 日期
     */
    public static Date dateAddWeeks(Date date, int addWeeks) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, addWeeks);
        return cal.getTime();
    }

    /**
     * 日期加年数
     *
     * @param date     日期
     * @param addYears 增加年数
     * @return 日期
     */
    public static Date dateAddYears(Date date, int addYears) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, addYears);
        return cal.getTime();
    }

    /**
     * 日期相差天数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 天数
     */
    public static int getDateLength(Date beginDate, Date endDate) {
        int length = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24));
        length++;
        return length;
    }

    /**
     * 日期相差秒数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 天数
     */
    public static int getSecondByDate(Date beginDate, Date endDate) {
        return (int) ((endDate.getTime() - beginDate.getTime()) / 1000);
    }

    /**
     * 今天还有多少秒
     *
     * @return
     */
    public static int getDaySurplusSecond() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int SSS = cal.get(Calendar.MILLISECOND);
        //时分秒（毫秒数）
        int millisecond = hour * 60 * 60 + minute * 60 + second;
        int daobanye = 24 * 60 * 60;
        int tobanye = daobanye - millisecond;
        return tobanye;
    }

    /**
     * 获取当月最后一天
     * @param format
     * @return
     */
    public static String getLastDayOfMonth(String format) {
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }
}
