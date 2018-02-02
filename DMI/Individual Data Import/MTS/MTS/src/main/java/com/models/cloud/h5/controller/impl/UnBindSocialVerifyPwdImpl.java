package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.h5.controller.DoPageService;
@Service("unBindSocialVerifyPwdImpl")
public class UnBindSocialVerifyPwdImpl implements DoPageService{

	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		String socialSecurityId=request.getParameter("socialSecurityId");
		map.put("socialSecurityId", socialSecurityId);
		map.put("resultCode", "0");
		map.put("resultDesc", "操作成功");
		return map;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
