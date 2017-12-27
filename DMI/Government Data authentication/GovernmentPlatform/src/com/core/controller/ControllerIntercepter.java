/**   
 * . 
 * All rights reserved.
 */
package com.core.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.core.log.service.LogService;
import com.core.property.AppPropNameConst;
import com.core.property.ApplicationProperties;
import com.core.utils.EBWebUtils;
import com.core.utils.JsonStringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 控制器拦截
 * 
 * @author: xiangfeng.guan
 * @date: 2015年12月10日 下午3:28:15
 */
@Aspect
@Component
public class ControllerIntercepter {

	private static final Logger logger = LoggerFactory.getLogger(ControllerIntercepter.class);

	public static final String TICKET = "ticket";

	@Resource
	private LogService logService;

	@Resource
	private ObjectMapper objectMapper;

	private ThreadLocal<String> threadParam = new ThreadLocal<String>();

	private ThreadLocal<Long> threadStart = new ThreadLocal<Long>();

	@Pointcut("execution(public * com.*..*Controller.*(..))")
	private void actionMethod() {}

	@Around("actionMethod()")
	public Object actionAround(ProceedingJoinPoint pjp) throws Throwable {

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		String method = signature.getName();
		String target = pjp.getTarget().getClass() + "." + method + "()";

		HttpServletRequest request = EBWebUtils.getRequest();
		String url = request.getServletPath();
		if (logger.isDebugEnabled()) {
			logger.debug("【日志】日志拦截开始:" + url);
		}

		// 准备日志数据
		String param = generateParamJson(request.getParameterMap());
		threadParam.set(param);
		long startTime = System.currentTimeMillis();
		threadStart.set(startTime);

		// 验证TICKET
		String clientTicket = request.getParameter(TICKET);
		String serverTicket = ApplicationProperties.getPropertyValue(AppPropNameConst.MI_TECKET);
		if (clientTicket == serverTicket || (clientTicket != null && clientTicket.equals(serverTicket))) {} else {
			throw new SecurityException("客户端TICKET不匹配！");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("【日志】调用目标方法:" + target);
		}

		// 调用目标方法
		Object result = pjp.proceed();

		// 记录结束时间
		long endTime = System.currentTimeMillis();

		if (logger.isDebugEnabled()) {
			logger.debug("【日志】日志拦截结束（执行成功）:" + url);
		}

		// 准备日志数据
		String resultJson = (result == null) ? null : objectMapper.writeValueAsString(result);
		long timeCost = endTime - startTime;

		// 记录日志
		if (logger.isDebugEnabled()) {
			logger.debug("【日志】日志记录（业务执行成功）:" + target);
		}
		log(param, resultJson, true, null, timeCost);

		return result;
	}

	@AfterThrowing(pointcut = "actionMethod()", throwing = "e")
	public void actionException(JoinPoint jp, Exception e) throws Exception {
		long endTime = System.currentTimeMillis();

		MethodSignature signature = (MethodSignature) jp.getSignature();
		String method = signature.getName();
		String target = jp.getTarget().getClass() + "." + method + "()";

		HttpServletRequest request = EBWebUtils.getRequest();
		String url = request.getServletPath();
		if (logger.isDebugEnabled()) {
			logger.debug("【日志】日志拦截结束（发生异常）" + url);
		}

		long startTime = threadStart.get();
		long timeCost = endTime - startTime;

		// 记录日志
		if (logger.isDebugEnabled()) {
			logger.debug("【日志】日志记录（业务执行失败）:" + target);
		}
		log(threadParam.get(), null, false, e.getMessage(), timeCost);
	}

	private String generateParamJson(Map<String, String[]> parameterMap) {
		StringBuilder json = new StringBuilder();
		if (MapUtils.isNotEmpty(parameterMap)) {
			json.append("{");
			int index = 0;
			for (Entry<String, String[]> e : parameterMap.entrySet()) {
				String name = e.getKey();
				String[] values = e.getValue();
				if (index > 0) {
					json.append(",");
				}
				String value = null;
				if (values == null || values.length == 0) {
					json.append("\"").append(name).append("\"").append(":").append(value);
				} else if (values.length == 1) {
					value = values[0];
					if (value == null) {
						json.append("\"").append(name).append("\"").append(":").append(value);
					} else {
						json.append("\"").append(name).append("\"").append(":").append("\"").append(value).append("\"");
					}
				} else {
					value = JsonStringUtils.objectToJsonString(values);
					json.append("\"").append(name).append("\"").append(":").append(value);
				}

				index++;
			}
			json.append("}");
			return json.toString();
		}
		return null;
	}

	private void log(String param, String result, boolean isSuccess, String logMsg, long timeCost) {
		HttpServletRequest request = EBWebUtils.getRequest();
		String url = request.getServletPath();
		String ip = request.getRemoteAddr();
		try {
			logService.showLog(url, param, result, isSuccess, timeCost, ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
