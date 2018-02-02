package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.util.hint.Hint;
import com.models.cloud.web.controller.DoWebPageService;

@Service("pcToResultWaitingPageServiceImpl")
public class ToResultWaitingPageServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(ToResultWaitingPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 if(logger.isInfoEnabled()){
			 logger.info("跳转到支付结果中结果页面");
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
