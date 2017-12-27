package com.dhcc.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static long toTime() {
		return new Date().getTime();
	}

	public static long toTime(String date) {
		date=date.trim();
		try {
			if (date.length() >= 19)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
			if (date.length() == 16)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date).getTime();
			if (date.length() == 13)
				return new SimpleDateFormat("yyyy-MM-dd HH").parse(date).getTime();
			if (date.length() == 10)
				return new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
			if (date.length() == 7)
				return new SimpleDateFormat("yyyy-MM").parse(date).getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String toDate(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
	/**
	 * @描述：只能使用yyyy-MM,返回当月天数
	 * @作者：SZ
	 * @时间：2015-7-3 上午01:32:06
	 * @param yearAndMonth
	 * @return
	 */
	public static int getMonthLastDay(String yearAndMonth){
		int year = Integer.parseInt(yearAndMonth.substring(0,4));
		int month = Integer.parseInt(yearAndMonth.substring(5,7));
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		int maxDate = a.getActualMaximum(Calendar.DATE);
		return maxDate;
	}
	
	/**
	 * @描述：只能使用yyyy-MM,返回当月最后一天的毫秒数
	 * @作者：SZ
	 * @时间：2015-7-3 上午01:31:46
	 * @param yearAndMonth
	 * @return
	 */
	public static long getMaxDate(String yearAndMonth){
		int year = Integer.parseInt(yearAndMonth.substring(0,4));
		int month = Integer.parseInt(yearAndMonth.substring(5,7));
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		int maxDate = a.getActualMaximum(Calendar.DATE);
		
		String yearAndDay = yearAndMonth+"-"+maxDate+" 23:59:59";
		return toTime(yearAndDay);
	}
	
	/**
	 * @描述：只能使用yyyy-MM,返回当月第一天的毫秒数
	 * @作者：SZ
	 * @时间：2015-7-3 上午01:30:18
	 * @param yearAndMonth
	 * @return
	 */
	public static long getMinDate(String yearAndMonth){
		String yearAndDay = yearAndMonth+"-01";
		return toTime(yearAndDay);
	}
	
	public static Date getDate(String pattern, String dateStr) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
