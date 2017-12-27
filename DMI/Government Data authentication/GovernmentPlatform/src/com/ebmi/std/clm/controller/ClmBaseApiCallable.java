package com.ebmi.std.clm.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;

public class ClmBaseApiCallable implements Callable<Object> {
	private IBaseApi baseApi;
	private String methodName;
	private String idcard;
	private String username;
	private String ss_num;
	private int year;
	private int month;

	/**
	 * 
	 * @param baseApi API执行类
	 * @param methodName 执行的工大接口名称
	 * @param idcard 公民身份证号
	 * @param username 姓名
	 * @param ss_num 社会保障卡号
	 */
	public ClmBaseApiCallable(IBaseApi baseApi,String methodName,String idcard,String username,String ss_num, int year, int month) {
		this.baseApi = baseApi;
		this.methodName = methodName;
		this.idcard = idcard;
		this.username = username;
		this.ss_num = ss_num;
		this.year = year;
		this.month = month;
	}

	@Override
	public TheKeyResultEntity call() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("parm0", idcard);// 公民身份证号
		map.put("parm1", username);// 姓名
		map.put("parm2", ss_num);// 社会保障卡号
		map.put("parm3", getMonthFirstDay(year, month));// 开始日期
		map.put("parm4", getMonthFirstDay(year, month + 1));// 终止日期接口原因，月末需要加一天
		return baseApi.getDataInfo(map, methodName);
	}
	
	/**
	 * @描述：获取月份第一天
	 * @作者：SZ
	 * @时间：2017年12月18日 上午10:36:31
	 * @param year
	 * @param month
	 * @return
	 */
	private static String getMonthFirstDay(int year, int month) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, month - 1);
		now.set(Calendar.DAY_OF_MONTH, 1);
		return sf.format(now.getTime());
	}

	/**
	 * @描述：获取月份最后一天如果是当月获取当天
	 * @作者：SZ
	 * @时间：2017年12月18日 上午10:36:31
	 * @param year
	 * @param month
	 * @return
	 */
	private static String getMonthLastDay(int year, int month) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.YEAR) == year && (now.get(Calendar.MONTH) + 1) == month) {
			return sf.format(now.getTime());
		}
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, month);
		now.set(Calendar.DAY_OF_MONTH, 0);
		return sf.format(now.getTime());
	}
}