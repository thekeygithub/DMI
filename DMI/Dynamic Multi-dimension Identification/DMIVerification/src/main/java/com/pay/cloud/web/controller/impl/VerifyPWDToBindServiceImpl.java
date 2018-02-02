package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.web.controller.DoWebPageService;
/**
 *  验证支付密码（进行新卡绑定）
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月19日 
 * @time 下午1:35:21
 * @version V1.0
 * @修改记录
 *
 */
@Service("pcVerifyPWDToBindServiceImpl")
public class VerifyPWDToBindServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(VerifyPWDToBindServiceImpl.class);
	
	@Resource
	VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("验证支付密码");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
    	String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		String hardwareId = (String)map.get("hardwareId");
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payPassword", payPassword);
		reqMap.put("payOrderId", payOrderId);
		if(logger.isInfoEnabled()){
			logger.info("验证支付密码，传入参数："+reqMap);
		}
		
		//1.验证支付密码
		Map<String,Object> verifyPwdMap = new HashMap<String,Object>();
		verifyPwdMap.put("interfaceCode", "verifyPayPassword");
		verifyPwdMap.put("accountId", accountId);
		verifyPwdMap.put("payPassword", payPassword);
		verifyPwdMap.put("hardwareId", hardwareId);//硬件id
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，请求参数："+verifyPwdMap);
		}
		
		resultMap = verifyUserPaymentPwdInterfaceImpl.doService(verifyPwdMap);
	
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)verifyPwdMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			sessionOrderMap.put("payToken", resultMap.get("payToken"));
			session.setAttribute(SmsConstant.SESSION_USER_ORDER_KEY,sessionOrderMap);
			
		}
		
		return resultMap;
	}

}