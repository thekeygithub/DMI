package com.models.cloud.web.controller.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.PreparePayInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.GetRemoteIpAddr;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.web.controller.DoWebPageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付密码验证通过后调用预支付
 * Created by yacheng.ji on 2016/9/28.
 */
@Service("pcVerifyPwdAndPreparePaymentServiceImpl")
public class VerifyPwdAndPreparePaymentServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(VerifyPwdAndPreparePaymentServiceImpl.class);
	
	@Resource
	private VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	@Resource
	private PreparePayInterfaceImpl preparePayInterfaceImpl;
	
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {

		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
    	String accountId = String.valueOf(sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		String hardwareId = (String)map.get("hardwareId");

		//1.验证支付密码
		Map<String,Object> verifyPwdMap = new HashMap<>();
		verifyPwdMap.put("interfaceCode", "verifyPayPassword");
		verifyPwdMap.put("accountId", accountId);
		verifyPwdMap.put("payPassword", payPassword);
		verifyPwdMap.put("hardwareId", hardwareId);//硬件id
		if(logger.isInfoEnabled()){
			logger.info("请求verifyPayPassword接口，输入参数：" + verifyPwdMap);
		}
		Map<String, Object> resultMap = verifyUserPaymentPwdInterfaceImpl.doService(verifyPwdMap);
		if(logger.isInfoEnabled()){
			logger.info("接口响应结果：" + resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage("verifyPayPassword", resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果：" + resultMap);
		}
		
		Map<String,Object> preparePayMap = new HashMap<>();
		if(Hint.SYS_SUCCESS.getCodeString().equals(resultMap.get("resultCode").toString())){
			sessionOrderMap.put("payToken", resultMap.get("payToken"));
			session.setAttribute(SmsConstant.SESSION_USER_ORDER_KEY, sessionOrderMap);
			//2.预支付(非YeePay)
			preparePayMap.put("interfaceCode", "preparePay");
			preparePayMap.put("payOrderId", payOrderId);
			preparePayMap.put("accountId", accountId);
			preparePayMap.put("payToken", resultMap.get("payToken"));
			preparePayMap.put("ccbFlag", "");
			preparePayMap.put("terminalType", DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCodeString());
			preparePayMap.put("hardwareId", hardwareId);//硬件id
			preparePayMap.put("userIp", GetRemoteIpAddr.getIpAddr(request));
			if(logger.isInfoEnabled()){
				logger.info("请求preparePay接口，输入参数：" + preparePayMap);
			}
			resultMap = preparePayInterfaceImpl.doService(preparePayMap);
			if(logger.isInfoEnabled()){
				logger.info("接口响应结果：" + resultMap);
			}

			resultMap = ConvertUtils.getMappingHintMessage("preparePay", resultMap);
			if(logger.isInfoEnabled()){
				logger.info("错误码转换结果："+resultMap);
			}
		}
		return resultMap;
	}
}
