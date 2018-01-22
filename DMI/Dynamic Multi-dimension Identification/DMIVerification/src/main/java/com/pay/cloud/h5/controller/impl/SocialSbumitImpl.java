package com.pay.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SocialSecurityBindInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;
/***
 * 社保卡提交完成
 * @author enjuan.ren
 *
 */
@Service("socialSbumitImpl")
public class SocialSbumitImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SocialSbumitImpl.class);
	
	@Resource(name="socialSecurityBindInterfaceImpl")
	SocialSecurityBindInterfaceImpl bindImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		String cardNo=(String)receiveMap.get("cardNo");
		String cardName=(String)receiveMap.get("cardName");
		String socialSecurityNo1=(String)receiveMap.get("socialSecurityNo");
		String socialSecurityNo=socialSecurityNo1.replace(" ", "");
		String cityId=(String)receiveMap.get("cityId");
		String socialSecurityPassword=(String)receiveMap.get("socialSecurityPassword");
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		receiveMap.put("userId", userId);
		receiveMap.put("interfaceCode", "socialSecurityBind");
		receiveMap.put("cardNo", cardNo);
		receiveMap.put("cardName", cardName);
		receiveMap.put("socialSecurityNo", socialSecurityNo.trim());
		receiveMap.put("cityId", cityId);
		receiveMap.put("socialSecurityPassword", socialSecurityPassword);
		if(logger.isInfoEnabled()){
			logger.info("社保卡绑卡");
		}
		Map<String, Object> returnMap=bindImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return ConvertUtils.getMappingHintMessage("socialSecurityBind", returnMap);
		
	}

}
