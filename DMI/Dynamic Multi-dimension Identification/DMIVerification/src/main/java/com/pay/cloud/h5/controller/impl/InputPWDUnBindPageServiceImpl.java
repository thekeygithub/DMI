package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.hint.Hint;

@Service("inputPWDUnBindPageServiceImpl")
public class InputPWDUnBindPageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(InputPWDUnBindPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("跳转到输入密码验证身份页面");
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String bindId = request.getParameter("bindId");
		if(logger.isInfoEnabled()){
			logger.info("跳转到输入密码验证身份页面，bindId："+bindId);
		}
		resultMap.put("bindId", bindId);
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
