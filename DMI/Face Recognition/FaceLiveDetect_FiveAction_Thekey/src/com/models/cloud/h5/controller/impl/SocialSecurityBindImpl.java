package com.models.cloud.h5.controller.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.SocialSecurityQueryInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;

/***
 * 社保卡绑卡
 * @author enjuan.ren
 *
 */
@Service("socialSecurityBindImpl")
public class SocialSecurityBindImpl implements DoPageService{
	private Logger logger = Logger.getLogger(SocialSecurityBindImpl.class);
	
	@Resource(name="socialSecurityQueryInterfaceImpl")
	private SocialSecurityQueryInterfaceImpl socialSecurityBindImpl;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("查询社保卡信息");
		}
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		receiveMap.put("userId", userId);
		Map<String,Object> returnMap=socialSecurityBindImpl.doService(receiveMap);
		System.out.println(returnMap.get("socialSecurityList"));
		List<String> socialList=(List<String>) returnMap.get("socialSecurityList");
		if (socialList!=null && socialList.size()>0) {
			returnMap.put("socialList", socialList);
		}
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return ConvertUtils.getMappingHintMessage("socialSecurityBindList", returnMap);
	}

	
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		return null;
	}

}
