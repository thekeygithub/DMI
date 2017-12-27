package com.dhcc.common.util;


/**
 * 字符串的工具类
 * @author GYR
 *
 */
public class StringUtil {
	/***
	 * 判断字符串是否为空
	 */
	public static boolean isNullOrEmpty(String str)
	{
		return null == str || str.equals("");
	}
	
	/***
	 * 检查一个字符串是否是正整数
	 */
	public static boolean isNumeric(String number)
	{
		if (isNullOrEmpty(number)) return false;

		for (int i = 0; i < number.length(); i++)
		{
			char c = number.charAt(i);
			if (Character.isDigit(c)) continue;
			return false;
		}

		return (number.length() <= 11);
	}
	
	/***
	 * 检查一个字符串是否是正长整数
	 */
	public static boolean isLong(String number)
	{
		if (isNullOrEmpty(number)) return false;

		for (int i = 0; i < number.length(); i++)
		{
			char c = number.charAt(i);
			if (Character.isDigit(c)) continue;
			return false;
		}

		return (number.length() <= 19);
	}
	
	/***
	 * 检查一个字符串是否是整数
	 */
	public static boolean isInteger(String number)
	{
		if (isNullOrEmpty(number)) return false;

		int i=0;
		if(number.charAt(i)=='-')
		{
			i++;
		}
		
		for (; i < number.length(); i++)
		{
			char c = number.charAt(i);
			if (Character.isDigit(c)) continue;
			return false;
		}

		return (number.length() <= 11);
	}

	/***
	 * 检查一个字符串是否是浮点数
	 */
	public static boolean isFloatNumber(String str)
	{
		if (isNullOrEmpty(str)) return false;
		int count = 0;
		int i=0;
		if(str.charAt(i)=='-')
		{
			i++;
		}
		for (; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (c == '.') count++;
			else if (!Character.isDigit(c)) return false;
		}

		return count <= 1;
	}
	/**
	 * 格式化字符串,避免出现null,将null转换为""
	 * 
	 * @param s
	 *            需要格式化的字符串
	 * @return 格式化后的字符串
	 */
	public static String normalizeString(String s) {
		return ((s == null) ? "" : s.trim());
	}
}
