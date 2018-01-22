package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mysql.jdbc.StringUtils;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.hint.Hint;

@Service("toInputPWDVerifyServiceImpl")
public class ToInputPWDVerifyServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(ToInputPWDVerifyServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		String accountId = request.getParameter("accountId");
		String payOrderId = request.getParameter("payOrderId");
		String bindId = request.getParameter("bindId");
		reqMap.put("accountId", accountId);
		reqMap.put("payOrderId", payOrderId);
		reqMap.put("bindId", bindId);
		
		if(logger.isInfoEnabled()){
			logger.info("跳转到输入密码验证身份页面，传入参数:"+reqMap);
		}
		if(StringUtils.isNullOrEmpty(payOrderId) || StringUtils.isNullOrEmpty(accountId) || StringUtils.isNullOrEmpty(bindId)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString().
					replace("{param}", "payOrderId or accountId or bindId"));
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage());
			return resultMap;
		}
		
		request.setAttribute("accountId", accountId.trim());
		request.setAttribute("payOrderId", payOrderId.trim());
		request.setAttribute("bindId", bindId.trim());
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
