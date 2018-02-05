package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.PrePaymentOrderInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.web.controller.DoWebPageService;
/**
 * 
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月23日 
 * @time 下午4:14:05
 * @version V1.0
 * @修改记录
 *
 */
@Service("pcVerifyPWDAndPrePayServiceImpl")
public class VerifyPWDAndPrePayServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(VerifyPWDAndPrePayServiceImpl.class);
	
	@Resource
	private VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	@Resource
	private PrePaymentOrderInterfaceImpl prePaymentOrderInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("已绑卡---验证支付密码并预支付");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
    	String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		String hardwareId = (String)map.get("hardwareId");
		String bindId = (String)map.get("bindId");
		String merOrderId = (String)sessionOrderMap.get("merOrderId");//商户订单号
		String ipInfo = (String)map.get("ipInfo");//ip
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payPassword", payPassword);
		reqMap.put("payOrderId", payOrderId);
		reqMap.put("bindId", bindId);
		if(logger.isInfoEnabled()){
			logger.info("已绑卡---验证支付密码并预支付，传入参数："+reqMap);
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
		
		Map<String,Object> prePayMap = new HashMap<String,Object>();
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			
			sessionOrderMap.put("payToken", resultMap.get("payToken"));
			session.setAttribute(SmsConstant.SESSION_USER_ORDER_KEY,sessionOrderMap);
			//2.已绑卡预支付
			prePayMap.put("interfaceCode", "prePaymentVerify");
			prePayMap.put("accountId", accountId);
			prePayMap.put("bindId", bindId);
			prePayMap.put("payToken", resultMap.get("payToken"));
			prePayMap.put("payOrderId", payOrderId);
			prePayMap.put("merOrderId", merOrderId);
			prePayMap.put("paymentType", BaseDictConstant.PAYMENT_TYPE_BIND_CARD);
			prePayMap.put("hardwareId", hardwareId);//硬件id
			prePayMap.put("remoteAddr", ipInfo); // ip
			
			if(logger.isInfoEnabled()){
				logger.info("请求 "+prePayMap.get("interfaceCode")+" 接口，请求参数："+prePayMap);
			}
			resultMap = prePaymentOrderInterfaceImpl.doService(prePayMap);
			
			if(logger.isInfoEnabled()){
				logger.info("请求 "+prePayMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
			}
			
			resultMap = ConvertUtils.getMappingHintMessage((String)prePayMap.get("interfaceCode"),resultMap);
			if(logger.isInfoEnabled()){
				logger.info("错误码转换结果："+resultMap);
			}
		}
		
		return resultMap;
	}

}
