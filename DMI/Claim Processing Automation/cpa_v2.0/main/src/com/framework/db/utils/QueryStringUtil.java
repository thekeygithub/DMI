package com.framework.db.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaowang
 *
 */
public class QueryStringUtil {
	
	public static String queryStringLike(String values) {
		return "%" + values + "%";
	}

	public static String dateFormat(String date, String format) {
		if(date == null || "".equals(date))
			return "";
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
		if(date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
