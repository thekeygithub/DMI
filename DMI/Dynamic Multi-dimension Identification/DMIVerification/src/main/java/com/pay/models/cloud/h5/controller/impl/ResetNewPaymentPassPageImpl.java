package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.ResetPayPasswordForRealNameInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;

/**
 * 重置支付密码
 * @author haiyan.zhang
 *
 */
@Service("resetNewPaymentPassPageImpl")
public class ResetNewPaymentPassPageImpl implements DoPageService{
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

}
