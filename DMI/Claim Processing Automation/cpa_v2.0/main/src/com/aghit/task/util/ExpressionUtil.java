package com.aghit.task.util;

import java.util.Calendar;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 字符串表达式计算工具类
 * @author Administrator
 *
 */
public class ExpressionUtil {
	
	private static Logger log = Logger.getLogger(ExpressionUtil.class);
	/**
	 * 计算逻辑表达式的结果
	 * 表达式的格式：true && true || false && ....
	 * @param exp 表达式字符串
	 * @return 表达式的逻辑结果
	 */
	public static boolean calcLogicExp(String exp){
		
		boolean result = false;// 默认返回值为false
		// 异常判断，表达式不能为空
		if(StringUtils.isBlank(exp)){
			log.error("表达式为空！");
			return result;
		}
		if (exp.contains("[") || exp.contains("]")){
			log.error("表达式不正确:" + exp);
		}
		
		
		// 构建Jexl表达式的引擎
		JexlEngine jexl = new JexlEngine();
		Expression e = jexl.createExpression(exp);
		// 计算表达式
		result = (Boolean)e.evaluate(null);
		
		return result;
	}
	
	public static void main(String[] args) {
//		System.out.println(ExpressionUtil.calcLogicExp("true || false"));
//		System.out.println(Integer.parseInt("-0"));
//		String[] area = "-0/-8".split("/");
//		System.out.println(area[0] + "  " + area[1]);
//		Calendar u = Calendar.getInstance();
//		u.set(Calendar.getInstance().getMinimum(Calendar.YEAR), 12, 12, 45, 23,55);
//		System.out.print(Calendar.getInstance().getMinimum(Calendar.YEAR));
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, 0, 5, 14, 5, 6);
		System.out.println(startDate.getTime());
		startDate.set(Calendar.HOUR_OF_DAY, 23);
		startDate.set(Calendar.MINUTE, 59);
		startDate.set(Calendar.SECOND, 59);
		System.out.println(startDate.getTime());
		
		
		
	}
}
