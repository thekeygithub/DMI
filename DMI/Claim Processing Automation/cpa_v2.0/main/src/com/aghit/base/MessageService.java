package com.aghit.base;

import java.util.Locale;

/**
 * @author xiaowang
 *
 */
public interface MessageService {
	
	/**
	 * 获取信息，当前设置为中文，以后可根据设置来读取
	 * 
	 * @param key 消息KEY
	 * @param args 消息参数
	 * @return String
	 */
	public String getMessage(String key, Object[] args);
	
	/**
	 * 获取信息
	 * 
	 * @param key
	 * @param args
	 * @param lc 地区
	 * @return String
	 */
	public String getMessage(String key, Object[] args, Locale lc);

}
