package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.h5.controller.DoPageService;
@Service("toFaceRecognitionImpl")
public class ToFaceRecognitionImpl implements DoPageService{

	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		String socialSecurityId=request.getParameter("socialSecurityId");
		receiveMap.put("socialSecurityId", socialSecurityId);
		receiveMap.put("resultCode", "0");
		receiveMap.put("resultDesc", "操作成功");
		return receiveMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
