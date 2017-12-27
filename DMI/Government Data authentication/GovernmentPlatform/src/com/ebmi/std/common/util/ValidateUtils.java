package com.ebmi.std.common.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtils {
	
	/**
	 * 
	 * @Description: 手机号验证
	 * @Title: isPhoneNumber 
	 * @param value
	 * @return boolean
	 */
	public static boolean isPhoneNumber(String value){
		if(StringUtils.isBlank(value)) return false;
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 身份证号验证
	 * @Title: isIdCard 
	 * @param value
	 * @return boolean
	 */
	public static boolean isIdCard(String value){
		if(StringUtils.isBlank(value)) return false;
		Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 邮箱验证
	 * @Title: isEmail 
	 * @param value
	 * @return boolean
	 */
	public static boolean isEmail(String value){
		if(StringUtils.isBlank(value)) return false;
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,5}$");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 正整数验证
	 * @Title: isNumber 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNumber(String value){
		if(StringUtils.isBlank(value)) return false;
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
    /**
     * 
     * @Description: 字符串长度验证（大于等于最小长度并且小于等于最大长度，返回true）
     * @Title: checkLength 
     * @param value 字符串
     * @param minlength 最小长度
     * @param maxlength 最大长度
     * @return boolean
     */
	public static boolean checkLength(String value, int minlength, int maxlength){	
		if(StringUtils.isBlank(value)) return false;
		int length =  StringUtils.isBlank(value)? 0:value.length();	
		boolean flg = false;
		if(length >= minlength && length <= maxlength){
			flg = true;
		}
		return  flg;
	}
	
	/**
	 * 
	 * @Description: 自定义验证
	 * @Title: isRegex 
	 * @param value 值
	 * @param regex 正则表达式
	 * @return boolean
	 */
	public static boolean isRegex(String value, String regex){
		if(StringUtils.isBlank(value)||StringUtils.isBlank(regex)) return false;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 判断Object对象非NULL并且转字符串后值等于某个字符串
	 * @Title: isEmpty 
	 * @param str
	 * @param value
	 * @return Boolean
	 */
	public static Boolean isEmpty(Object str,String value){
		if(null!=str&&str.toString().trim().equals(value.toString())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 判断Object数组对象非NULL并且数组Length大于零
	 * @Title: isEmpty 
	 * @param strArray
	 * @return Boolean
	 */
	public static Boolean isEmpty(Object [] strArray){
		if(null!=strArray&&strArray.length>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串后非空
	 * @Title: isEmpty 
	 * @param map
	 * @param key
	 * @return Boolean
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean isEmptyMap(Map map,Object key){
		if(map.containsKey(key)&&map.get(key)!=null&&!map.get(key).toString().trim().equals("")){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串值等于某个字符串
	 * @Title: isEmpty 
	 * @param map
	 * @param key
	 * @param value
	 * @return Boolean
	 */
	public static Boolean isEmptyMap(Map<Object,Object> map,Object key,String value){
		if(map.containsKey(key)&&map.get(key)!=null&&map.get(key).toString().trim().equals(value)){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断List非NULL并且SIZE大于零
	 * @Title: isEmpty 
	 * @param li
	 * @return Boolean
	 */
	public static Boolean isEmpty(List<Object> li){
		if(li!=null&&li.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断文件夹是否存在,如果不存在则创建文件夹
	 * @Title: isFile 
	 * @param path 
	 * @return void
	 */
	public static void isFile(String path)
	{
		 File file = new File(path); 
		 if (!file.exists()) 
		 {   
			 file.mkdir();  
		 }
	}
}
