package com.ebmi.std.querysets.dao.mapper;

import java.io.UnsupportedEncodingException;

import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractRowMapper<T> implements RowMapper<T> {

	protected String convertStr(String str) {
		if (str == null) return null;
		
		try {
			return new String(str.getBytes("CP1252"), "GBK").trim();
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	/**
	 * 是否需要转换医院
	 * @param hospital
	 * @return
	 */
	protected boolean isNeedConvertHosp(String hospital) {
		String reg="^\\+?[1-9][0-9]*$";
		
		if (hospital == null) return false;
		return hospital.matches(reg);
	}
	
	public static void main(String[] args) {
//		String str = "99914";
//		System.out.println(isNeedConvertHosp(str));
	}
}
