package com.ebmi.std.interfaceapi;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class Test {

	public static void main(String[] args) {
//	
//		Map<String, String> map = new LinkedHashMap<String, String>();
//		map.put("parm0", "230403197611280546");
//		map.put("parm1", "孙艳辉");
//		map.put("parm2", "A00640561");
//		try {
//			//PbaseinfoApi.testflag=true;
//			EbaoResultEntity res  = PbaseinfoApi.getPbaseinfo(map);
//			System.out.println("res>>>>>>>>>>>>>>>>"+res);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
  System.out.println(formatDate(getYearFirst(2015), "yyyyMMdd"));
	}
	

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	
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


}
