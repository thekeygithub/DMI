package com.pay.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SocialSecurityRealInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;
/***
 * 社保卡完成
 * @author enjuan.ren
 *
 */
@Service("socialFinishImpl")
public class SocialFinishImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SocialFinishImpl.class);
	
	@Resource(name="socialSecurityRealInterfaceImpl")
	SocialSecurityRealInterfaceImpl socialFinalImpl;
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		receiveMap.put("interfaceCode", "socialSecurityReal");
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		String phone=(String)receiveMap.get("phone");
		String socialSecurityId=(String)receiveMap.get("socialSecurityId");
		String verifyCode=(String)receiveMap.get("verifyCode");
		receiveMap.put("userId", userId);
		receiveMap.put("phone", phone);
		receiveMap.put("socialSecurityId", socialSecurityId);
		receiveMap.put("verifyCode", verifyCode);
		if(logger.isInfoEnabled()){
			logger.info("社保卡绑卡完成");
		}
		Map<String, Object> returnMap=socialFinalImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return ConvertUtils.getMappingHintMessage("socialSecurityReal", returnMap);
	}

}
