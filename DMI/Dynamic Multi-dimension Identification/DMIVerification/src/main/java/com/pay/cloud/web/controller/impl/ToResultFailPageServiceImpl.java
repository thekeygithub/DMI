package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.web.controller.DoWebPageService;

@Service("pcToResultFailPageServiceImpl")
public class ToResultFailPageServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(ToResultFailPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("failDescription", request.getParameter("failDescription"));
		 if(logger.isInfoEnabled()){
			 logger.info("跳转到支付失败结果页面，传入参数：" + resultMap.toString());
		 }
	        
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
