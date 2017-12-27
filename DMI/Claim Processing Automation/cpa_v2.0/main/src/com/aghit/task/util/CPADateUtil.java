package com.aghit.task.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 * @author Administrator
 *
 */
public class CPADateUtil {
	
	/**
	 * 获取一天的开始时间00:00:00:000
	 * @param dt 设置的时间
	 * @return
	 */
	public static Date getDateFirst(Date dt){
		Calendar ca = Calendar.getInstance();
		ca.setTime(dt);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
	
	/**
	 * 获取一天的结束时间23:59:59:999
	 * @param dt 设置的时间
	 * @return
	 */
	public static Date getDateLast(Date dt){
		Calendar ca = Calendar.getInstance();
		ca.setTime(dt);
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		ca.set(Calendar.MILLISECOND, 999);
		return ca.getTime();
	}
	
	/**
	 * 
	 * @param dt 日期
	 * @param fmt 格式字符串
	 * @return
	 */
	public static String date2String(Date dt, String fmt){
		if(dt == null ) return "";
		return new SimpleDateFormat(fmt).format(dt);
	}
	
	/**
	 * 计算年龄,返回月份，按实际月份计算，小于一个月按0月计算
	 * @param birthDay
	 * @param clmDay
	 * @return
	 */
	public static int calcAge(Date birthDay, Date clmDay){
		
		//若存在空数值，返回-1
		if(birthDay == null || clmDay == null) {
			return -1;
		}
		//年龄
		int age = 0;
		try {
			Calendar firstDay = Calendar.getInstance();
			firstDay.setTime(birthDay);
			Calendar secondDay = Calendar.getInstance();
			secondDay.setTime(clmDay);
			if (secondDay.equals(firstDay)) {
				return 0;
			}
			if (secondDay.get(Calendar.YEAR) > firstDay.get(Calendar.YEAR)) {
				age = (secondDay.get(Calendar.YEAR) - firstDay.get(Calendar.YEAR))* 12
						+ secondDay.get(Calendar.MONTH) - firstDay.get(Calendar.MONTH);
			} else {
				age = secondDay.get(Calendar.MONTH) - firstDay.get(Calendar.MONTH);
			}
			if(1 == age){
				int day = (secondDay.get(Calendar.YEAR) - firstDay.get(Calendar.YEAR))* 365
						+ secondDay.get(Calendar.DAY_OF_YEAR) - firstDay.get(Calendar.DAY_OF_YEAR);
                if (day <= 30 ){
                	age = 0;
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return age;
	} 
	
	/**
	 * 计算日期差值，返回天数
	 * @param lastDay
	 * @param fistDay
	 * @return
	 */
	public static int calDayDiff(Date lastDay, Date fistDay){
		//若存在空数值，返回-1
		if(lastDay == null || fistDay == null) {
			return -1;
		}
		int day = 0;
		try {
			Calendar firstDay = Calendar.getInstance();
			firstDay.setTime(fistDay);
			Calendar secondDay = Calendar.getInstance();
			secondDay.setTime(lastDay);
			day = (secondDay.get(Calendar.YEAR) - firstDay.get(Calendar.YEAR))* 365
					+ secondDay.get(Calendar.DAY_OF_YEAR) - firstDay.get(Calendar.DAY_OF_YEAR);
			//出院日期小于入院日期，返回-1
			if (day < 0){
				day = -1;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}
	
	public static void main(String[] args) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		ca.set(Calendar.YEAR, 2014);
		ca.set(Calendar.MONTH, 11);
		ca.set(Calendar.DAY_OF_MONTH, 31);
		
		Calendar ca2 = Calendar.getInstance();
		ca2.set(Calendar.HOUR_OF_DAY, 0);
		ca2.set(Calendar.MINUTE, 0);
		ca2.set(Calendar.SECOND, 0);
		ca2.set(Calendar.MILLISECOND, 0);
		ca2.set(Calendar.YEAR, 2015);
		ca2.set(Calendar.MONTH, 0);
		ca2.set(Calendar.DAY_OF_MONTH, 1);

//		System.out.println(calcAge(ca.getTime(),ca2.getTime()));
//		System.out.println(date2String(new Date(),"yyyyMMdd"));
//		System.out.println(calDayDiff(ca2.getTime(),ca.getTime()));
		
		System.out.println("-1.0".contains("-1"));
	}
	
	
}
