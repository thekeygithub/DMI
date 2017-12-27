package com.dhcc.ts.yyxg;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import net.sf.json.JSONObject;

public class TsUtil {


	private TsUtil() {}
	
	/**
	 * 将obj输出为json格式数据,并关闭流
	 * 
	 * @param out 标准输出流
	 * @param obj 待输出内容
	 */
	public static void outputJson(PrintWriter out, Object obj) {
		
		out.print(JSONObject.fromObject(obj));
		out.flush();
		out.close();
	}
	
	/**
	 * 将obj输出为json格式数据,输出流未关闭
	 * 
	 * @param out 标准输出流
	 * @param obj 待输出内容
	 */
	public static void outputJsonNotClose(PrintWriter out, Object obj) {
		
		out.print(JSONObject.fromObject(obj));
		out.flush();
	}
	
	//计算两个日期相差年数 
	/**
	 * endDate - startDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int yearDateDiff(Date startDate,Date endDate){  
	   Calendar calBegin = Calendar.getInstance(); //获取日历实例  
	   Calendar calEnd = Calendar.getInstance();  
	   calBegin.setTime(startDate);
	   calEnd.setTime(endDate);  
	   return calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);  
	}
	
	/**
	 * @param date
	 * @param type
	 * Calendar.YEAR=1 
	 * Calendar.MONTH=2
	 * Calendar.DATE=5
	 * Calendar.HOUR=10
	 * Calendar.MINUTE=12 
	 * Calendar.SECOND=13 
	 * Calendar.MILLISECOND=14
	 * @param num
	 * @return
	 */
	public static Date calculateDate(Date date, int type, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, num);
		return calendar.getTime();
	}
	
	public static int random() {
		int max=5;
		int min=2;
		
		return new Random().nextInt(max)%(max-min+1) + min;
	}

	public static void main(String[] args) {
		System.out.println(calculateDate(new Date(), Calendar.DATE, 1));
		for(int i = 0 ; i<10; i++)
			System.out.println(random());
	}
}
