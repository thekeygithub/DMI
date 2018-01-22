package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.hint.Hint;

@Service("payOrderPageServiceImpl")
public class PayOrderPageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(PayOrderPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		String isUnBind = request.getParameter("isUnBind");
		if(isUnBind == null || "".equals(isUnBind)){
			isUnBind = "0";
		}
		if(logger.isInfoEnabled()){
			logger.info("跳转到付款页面，请求参数：isUnBind："+isUnBind);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("isUnBind", isUnBind);
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
