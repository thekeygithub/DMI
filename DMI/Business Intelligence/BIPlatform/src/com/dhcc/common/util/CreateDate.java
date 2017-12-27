package com.dhcc.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateDate {

    
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss"; //设置格式
    private static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
    /**
     * 获得当前时间
     * @return
     */
    public static String getDateString(){
        Date date = new Date();
        return sdf.format(date);
    }
    
    public static String getDateString(Calendar time){
    	Date date=time.getTime();
        return sdf.format(date);
    }
    
    public static String getDateString(Date date){
        return sdf.format(date);
    }
    
    /**
     * 得到当前时间的小时数，返回一个字符串。
     * 如当前时间为12：40则返回12
     * @param date
     * @return
     */
    public static String getDateHHString(){
    	
    	String FORMATS = "HH"; //设置格式
    	Date date = new Date();
    	SimpleDateFormat sdfs = new SimpleDateFormat(FORMATS);
        return sdfs.format(date);
    }
    
    public static String getDateString(String str){
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return sdf.format(date);
    }
    
    public static Date getDate(String str){
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
    		e.printStackTrace();
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return date;
    }
    
    public static Date getDate(Date date){
        Date d = null;
        try {
            d =  sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }
    
    public static Date getDate(){
        Date d = new Date();
        try {
            d =  sdf.parse(sdf.format(d));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }
    
    /**
     * 得到当前 月的天数
     * @return
     */
    public static String getCurrentTime(){
    	
    	SimpleDateFormat ct_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	Date ct_date = new Date();
		String ct_time = ct_sdf.format(ct_date); 
		getDateString(ct_time);
    
        return getDateString(ct_time);
    }
    
    
    
    
    public static void main(String[] args) {
		String str = "";
    	str = getDateHHString();
    	System.out.println(str);
	}


}
