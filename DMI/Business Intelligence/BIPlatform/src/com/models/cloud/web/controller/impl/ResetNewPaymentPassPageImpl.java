package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.ResetPayPasswordForRealNameInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 重置支付密码
 * @author haiyan.zhang
 *
 */
@Service("pcResetNewPaymentPassPageImpl")
public class ResetNewPaymentPassPageImpl implements DoWebPageService{
	private Logger logger = Logger.getLogger(ResetNewPaymentPassPageImpl.class);
	
	@Resource
	private ResetPayPasswordForRealNameInterfaceImpl resetPayPasswordForRealNameInterfaceImpl;
	
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
		if(logger.isInfoEnabled()){
			logger.info("重置支付密码");
		}
		if(request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY)!=null){
			Map<String,Object> sessionMap = (Map<String, Object>) request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY);
			receiveMap.put("userId", sessionMap.get("userId"));
			receiveMap.put("interfaceCode","resetPayPasswordForRealName");
		}
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，请求参数："+receiveMap);
		}
		
		Map<String,Object> returnMap =  resetPayPasswordForRealNameInterfaceImpl.doService(receiveMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		
		return ConvertUtils.getMappingHintMessage("resetPayPasswordForRealName", returnMap);
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
//		if(logger.isInfoEnabled()){
//			logger.info("重置支付密码");
//		}
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		HttpSession session = request.getSession();
//    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
//    	
//    	Map<String, Object> resetPWDMap = new HashMap<String, Object>();
//		resetPWDMap.put("interfaceCode","resetPayPasswordForRealName");
//		resetPWDMap.put("userId", sessionMap.get("userId"));
//		resetPWDMap.put("token", map.get("token"));
//		resetPWDMap.put("password", map.get("password"));
//		resetPWDMap.put("hardwareId", (String)map.get("hardwareId"));
//		
//		if(logger.isInfoEnabled()){
//			logger.info("请求 "+resetPWDMap.get("interfaceCode")+" 接口，请求参数："+resetPWDMap);
//		}
//		
//		resultMap =  resetPayPasswordForRealNameInterfaceImpl.doService(resetPWDMap);
//		
//		if(logger.isInfoEnabled()){
//			logger.info("请求 "+resetPWDMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
//		}
//		
//		resultMap = ConvertUtils.getMappingHintMessage("resetPayPasswordForRealName", resultMap);
//		
//		if(logger.isInfoEnabled()){
//			logger.info("转换后的结果："+resultMap);
//		}
//		return resultMap;
//	}

}
