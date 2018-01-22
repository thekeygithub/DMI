package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SendPayVerifyCodeInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;
/**
 * 已绑卡发送短信，用于确认支付
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月23日 
 * @time 下午6:11:42
 * @version V1.0
 * @修改记录
 *
 */
@Service("sendMessageConfirmPayServiceImpl")
public class SendMessageConfirmPayServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(SendMessageConfirmPayServiceImpl.class);
	
	@Resource
	private SendPayVerifyCodeInterfaceImpl sendPayVerifyCodeInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("下发短信 ");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)sessionOrderMap.get("payToken");
		String hardwareId = (String)map.get("hardwareId");
		
		Map<String,Object> sendMeMap = new HashMap<String,Object>();
		sendMeMap.put("interfaceCode", "sendPayVerifyCode");
		sendMeMap.put("accountId", accountId);
		sendMeMap.put("payToken", payToken);
		sendMeMap.put("payOrderId", payOrderId);
		sendMeMap.put("hardwareId", hardwareId);//硬件id
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+sendMeMap.get("interfaceCode")+" 接口，请求参数："+sendMeMap);
		}
		
		resultMap = sendPayVerifyCodeInterfaceImpl.doService(sendMeMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+sendMeMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)sendMeMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		return resultMap;
	}

}
