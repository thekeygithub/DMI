package com.pay.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.QueryRealNameInfoInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;

/***
 * 医保账户验证
 * @author enjuan.ren
 *
 */
@Service("medicareAccountVerificationImpl")
public class MedicareAccountVerificationImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(MedicareAccountVerificationImpl.class);
	
	@Resource(name="queryRealNameInfoInterfaceImpl")
	QueryRealNameInfoInterfaceImpl queryRealNameImpl;
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		String socialSecurityId=request.getParameter("socialSecurityId");
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		receiveMap.put("userId", String.valueOf(userId));
		receiveMap.put("interfaceCode", "queryRealNameInfo");
		if(logger.isInfoEnabled()){
			logger.info("查询实名认证信息");
		}
		Map<String, Object> returnMap=queryRealNameImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		returnMap.put("socialSecurityId", socialSecurityId);
		return ConvertUtils.getMappingHintMessage("queryRealNameInfo", returnMap);
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
