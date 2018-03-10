package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.SocialSecurityUnbindInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
/***
 * 社保卡解绑
 * @author enjuan.ren
 *
 */
@Service("socialSecurityUnbindImpl")
public class SocialSecurityUnbindImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SocialSecurityUnbindImpl.class);
	
	@Resource(name="socialSecurityUnbindInterfaceImpl")
	SocialSecurityUnbindInterfaceImpl unbindSocialSecurity;
	
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		String socialSecurityId=(String)receiveMap.get("socialSecurityId");
		receiveMap.put("userId", userId);
		receiveMap.put("socialSecurityId", socialSecurityId);
		if(logger.isInfoEnabled()){
			logger.info("社保卡解绑");
		}
		Map<String, Object> returnMap=unbindSocialSecurity.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("socialSecurityUnbind", returnMap);
	}

}
