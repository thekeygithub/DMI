package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.QueryRealNameInfoInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;

/**
 * 查询社保卡详情
 * @author qingsong.li
 *
 */
@Service("realNamePageServiceImpl")
public class RealNamePageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(RealNamePageServiceImpl.class);
	
	@Resource
	private QueryRealNameInfoInterfaceImpl queryRealNameInfoInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		
		Map<String,Object> sessionMap = (Map<String, Object>) request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY);
		String userId = sessionMap.get("userId")==null?"":sessionMap.get("userId").toString();
		if(logger.isInfoEnabled()){
			logger.info("查询社保信息页面");
		}
		Map<String,Object> receiveMap = new HashMap<String,Object>();
		receiveMap.put("userId", userId);
		Map<String, Object> resultMap = queryRealNameInfoInterfaceImpl.doService(receiveMap);
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		model.addAttribute("userCode", sessionMap.get("userCode")==null?"":sessionMap.get("userCode").toString());
		resultMap.put("topage", request.getParameter("topage"));
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}

}
