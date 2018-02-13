package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.h5.controller.DoPageService;

/***
 * 验证身份
 * @author enjuan.ren
 *
 */
@Service("proofOfIdentityImpl")
public class ProofOfIdentityImpl implements DoPageService{
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		receiveMap.put("userInfo", sessionMap);
		receiveMap.put("topage", request.getParameter("topage")==null?"":request.getParameter("topage"));
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
