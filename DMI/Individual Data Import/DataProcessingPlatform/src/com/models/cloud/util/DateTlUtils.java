package com.models.cloud.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Description: 时间转换类（多线程可能出现内存溢出）
 * @ClassName: DateThreadLocalUtils 
 * @author: zhengping.hu
 * @date: 2015年11月23日 上午9:47:13
 */
public class DateTlUtils {
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>(); 
 
    public static DateFormat getDateFormat(String date_format)   
    {  
        DateFormat df = threadLocal.get();
        if(df==null){
            df = new SimpleDateFormat(date_format);  
            threadLocal.set(df);  
        }  
        return df;  
    }  

	/**
	 * 
	 * @Description: java.util.Date时间转字符串
	 * @Title: dts
	 * @param date java.util.Date
	 * @param pattern
	 * [1] yyyy-MM-dd（英文简写（默认）如：2010-12-01）<br>
     * [2] yyyy-MM-dd HH:mm:ss（英文全称 如：2010-12-01 23:15:06）<br>
     * [3] yyyyMMdd（英文简写（默认）如：20101201）<br>
     * [4] yyyyMMddHHmmss（英文全称 如：20101201231506）<br>
     * [5] yyyy-MM-dd HH:mm:ss.S(精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S)<br>
     * [6] yyyy年MM月dd日（中文简写 如：2010年12月01日）<br>
     * [7] yyyy年MM月dd日  HH时mm分ss秒(中文全称 如：2010年12月01日 23时15分06秒)<br>
     * [8] yyyy年MM月dd日  HH时mm分ss秒SSS毫秒(精确到毫秒的完整中文时间)<br>
     * [9] yyyy<br>
     * [10]HHmmss
	 * @return: String
	 */
	public static String dts(Date date,int pattern) {
		return getDateFormat(getPattern(pattern)).format(date);
	}

	/**
	 * 
	 * @Description: 系统当前java.util.Date时间转字符串
	 * @Title: ndts
	 * @param pattern
	 * [1] yyyy-MM-dd（英文简写（默认）如：2010-12-01）<br>
     * [2] yyyy-MM-dd HH:mm:ss（英文全称 如：2010-12-01 23:15:06）<br>
     * [3] yyyyMMdd（英文简写（默认）如：20101201）<br>
     * [4] yyyyMMddHHmmss（英文全称 如：20101201231506）<br>
     * [5] yyyy-MM-dd HH:mm:ss.S(精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S)<br>
     * [6] yyyy年MM月dd日（中文简写 如：2010年12月01日）<br>
     * [7] yyyy年MM月dd日  HH时mm分ss秒(中文全称 如：2010年12月01日 23时15分06秒)<br>
     * [8] yyyy年MM月dd日  HH时mm分ss秒SSS毫秒(精确到毫秒的完整中文时间)<br>
     * [9] yyyy<br>
     * [10]HHmmss
	 * @return: String
	 */
	public static String ndts(int pattern) {
		return getDateFormat(getPattern(pattern)).format(new Date());
	}

	/**
	 * 
	 * @Description: 时间类型字符串转java.util.Date时间
	 * @Title: std
	 * @param String 时间类型字符串
	 * @param pattern
	 * [1] yyyy-MM-dd（英文简写（默认）如：2010-12-01）<br>
     * [2] yyyy-MM-dd HH:mm:ss（英文全称 如：2010-12-01 23:15:06）<br>
     * [3] yyyyMMdd（英文简写（默认）如：20101201）<br>
     * [4] yyyyMMddHHmmss（英文全称 如：20101201231506）<br>
     * [5] yyyy-MM-dd HH:mm:ss.S(精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S)<br>
     * [6] yyyy年MM月dd日（中文简写 如：2010年12月01日）<br>
     * [7] yyyy年MM月dd日  HH时mm分ss秒(中文全称 如：2010年12月01日 23时15分06秒)<br>
     * [8] yyyy年MM月dd日  HH时mm分ss秒SSS毫秒(精确到毫秒的完整中文时间)<br>
     * [9] yyyy<br>
     * [10]HHmmss
	 * @return: Date java.util.Date
	 */
	public static Date std(String date,int pattern) throws ParseException{
	    return getDateFormat(getPattern(pattern)).parse(date);
	} 
	/**
	 * 
	 * @Description: 时间类型字符串转java.sql.Timestamp时间
	 * @Title: std
	 * @param String 时间类型字符串
	 * @param pattern
	 * [1] yyyy-MM-dd（英文简写（默认）如：2010-12-01）<br>
     * [2] yyyy-MM-dd HH:mm:ss（英文全称 如：2010-12-01 23:15:06）<br>
     * [3] yyyyMMdd（英文简写（默认）如：20101201）<br>
     * [4] yyyyMMddHHmmss（英文全称 如：20101201231506）<br>
     * [5] yyyy-MM-dd HH:mm:ss.S(精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S)<br>
     * [6] yyyy年MM月dd日（中文简写 如：2010年12月01日）<br>
     * [7] yyyy年MM月dd日  HH时mm分ss秒(中文全称 如：2010年12月01日 23时15分06秒)<br>
     * [8] yyyy年MM月dd日  HH时mm分ss秒SSS毫秒(精确到毫秒的完整中文时间)<br>
     * [9] yyyy<br>
     * [10]HHmmss
	 * @return: Date java.sql.Timestamp
	 */
	public static Timestamp stt(String date,int pattern) throws ParseException{
		return new Timestamp(getDateFormat(getPattern(pattern)).parse(date).getTime());
	}
	/**
	 * 
	 * @Description: 时间类型字符串转java.sql.Date时间
	 * @Title: std
	 * @param String 时间类型字符串
	 * @param pattern
	 * [1] yyyy-MM-dd（英文简写（默认）如：2010-12-01）<br>
     * [2] yyyy-MM-dd HH:mm:ss（英文全称 如：2010-12-01 23:15:06）<br>
     * [3] yyyyMMdd（英文简写（默认）如：20101201）<br>
     * [4] yyyyMMddHHmmss（英文全称 如：20101201231506）<br>
     * [5] yyyy-MM-dd HH:mm:ss.S(精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S)<br>
     * [6] yyyy年MM月dd日（中文简写 如：2010年12月01日）<br>
     * [7] yyyy年MM月dd日  HH时mm分ss秒(中文全称 如：2010年12月01日 23时15分06秒)<br>
     * [8] yyyy年MM月dd日  HH时mm分ss秒SSS毫秒(精确到毫秒的完整中文时间)<br>
     * [9] yyyy<br>
     * [10]HHmmss
	 * @return: Date java.sql.Date
	 */
	public static java.sql.Date stsd(String date,int pattern) throws ParseException{
		return new java.sql.Date(getDateFormat(getPattern(pattern)).parse(date).getTime());
	}
	
	/**
	 * 
	 * @Description: java.util.Date时间转java.sql.Timestamp
	 * @Title: dtt 
	 * @param date java.util.Date时间
	 * @return
	 * @throws ParseException java.sql.Timestamp
	 */
	public static java.sql.Timestamp dtt(java.util.Date date){
		return new Timestamp(date.getTime());
	}
	
	/**
	 * 
	 * @Description: java.sql.Timestamp时间转java.util.Date
	 * @Title: ttd 
	 * @param timestamp java.sql.Timestamp时间
	 * @return
	 * @throws ParseException java.util.Date
	 */
	public static java.util.Date ttd(java.sql.Timestamp timestamp){
		return new java.util.Date(timestamp.getTime());  
	}
	
	/**
	 * 
	 * @Description: java.sql.Date时间转java.util.Date
	 * @Title: sdtd 
	 * @param sdate java.sql.Date时间
	 * @return java.util.Date
	 */
	public static java.util.Date sdtd(java.sql.Date sdate) {
		return (java.util.Date)sdate;  
	}
	
	/**
	 * 
	 * @Description: 获取转换类型
	 * @Title: getPattern 
	 * @param num 时间类型编号从1到10
	 * @return: String
	 */
	private static String getPattern(int num){
		String pattern="";
		switch (num) {
		case 1:
			pattern="yyyy-MM-dd";
			break;
		case 2:
			pattern="yyyy-MM-dd HH:mm:ss";
			break;
		case 3:
			pattern="yyyyMMdd";
			break;
		case 4:
			pattern="yyyyMMddHHmmss";
			break;
		case 5:
			pattern="yyyy-MM-dd HH:mm:ss.S";
			break;
		case 6:
			pattern="yyyy年MM月dd日";
			break;
		case 7:
			pattern="yyyy年MM月dd日  HH时mm分ss秒";
			break;
		case 8:
			pattern="yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
			break;
		case 9:
			pattern="yyyy";
			break;
		case 10:
			pattern="HHmmss";
			break;
		default:
			pattern="";
			break;
		}
		return pattern;
	}
}
