package com.ebmi.std.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

	/**
	 * 格式化日期
	 * @param date 日期对象
	 * @return String 日期字符串
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat f = new SimpleDateFormat(format);
		String sDate = f.format(date);
		return sDate;
	}
	
	//某年某月的最后一天
	public static int getLastDate(String year, String month){
		int lastDate = 0;
		try{
			SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyyMM");
			Calendar calendar = Calendar.getInstance();  
			Date d = simpleFormate.parse(year+month);
			calendar.setTime(d);
			lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}catch(Exception e){
			e.printStackTrace();
		}
		return lastDate;
	}
	
}