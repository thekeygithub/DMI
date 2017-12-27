package com.aghit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	
	public static String dateFormat(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date new_date = null;
		try {
			new_date = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat(new_date, format);
	}
	
	public static String dateFormat(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}