package com.models.cloud.web.controller.impl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.web.controller.DoWebPageService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service("pcToLoginPageServiceImpl")
public class ToLoginPageServiceImpl implements DoWebPageService{
	
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		return null;
	}
}
