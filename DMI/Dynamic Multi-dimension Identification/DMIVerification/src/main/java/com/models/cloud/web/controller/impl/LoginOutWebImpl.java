package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.LoginOutImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 退出登录
 * @ClassName: LoginOutWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("loginOutWebImpl")
public class LoginOutWebImpl implements DoWebPageService{
	private Logger logger = Logger.getLogger(LoginOutWebImpl.class);
	
	@Resource(name="loginOutImpl")
	private LoginOutImpl loginOutImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		
		if(request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY)!=null){
			Map<String,Object> sessionMap = (Map<String, Object>) request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY);
			receiveMap.putAll(sessionMap);
		}
		logger.info("退出登录清sessionId: "+request.getSession().getId());
		Map<String,Object> returnMap =loginOutImpl.doService(receiveMap);
		if(request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY)!=null){
			request.getSession().removeAttribute(SmsConstant.SESSION_USER_KEY);
		}
		return ConvertUtils.getMappingHintMessage("loginout", returnMap);
	}

}
