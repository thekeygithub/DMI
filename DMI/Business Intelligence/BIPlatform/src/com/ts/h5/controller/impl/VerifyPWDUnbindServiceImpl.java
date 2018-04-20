package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.JBCardInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.Md5SaltUtils;
import com.models.cloud.util.hint.Hint;
import com.mysql.jdbc.StringUtils;

@Service("verifyPWDUnbindServiceImpl")
public class VerifyPWDUnbindServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(VerifyPWDUnbindServiceImpl.class);
	
	@Resource
	JBCardInterfaceImpl jbcardInterfaceImpl;
	@Resource
	VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = (String)map.get("accountId");
		String payOrderId = (String)map.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		String bindId = (String)map.get("bindId");//绑卡id
		
		if(StringUtils.isNullOrEmpty(payOrderId) || StringUtils.isNullOrEmpty(accountId) 
				|| StringUtils.isNullOrEmpty(bindId) || StringUtils.isNullOrEmpty(payPassword) ){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString().replace("{param}", "payOrderId or accountId payPassword pwd or bindId"));
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage());
			return resultMap;
		}
		//支付密码进行md5加密
		payPassword = Md5SaltUtils.encodeMd5(payPassword);
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payPassword", payPassword);
		reqMap.put("payOrderId", payOrderId);
		reqMap.put("bindId", bindId);
		if(logger.isInfoEnabled()){
			logger.info("传入参数："+reqMap);
		}
		
		
		//1.验证支付密码
		Map<String,Object> verifyPwdMap = new HashMap<String,Object>();
		verifyPwdMap.put("interfaceCode", "verifyPayPassword");
		verifyPwdMap.put("accountId", accountId);
		verifyPwdMap.put("payPassword", payPassword);
		verifyPwdMap.put("hardwareId", request.getSession().getId());//硬件id
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，请求参数："+verifyPwdMap);
		}
		
		resultMap = verifyUserPaymentPwdInterfaceImpl.doService(reqMap);
		
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			//2.解绑银行卡
			Map<String,Object> unBindMap = new HashMap<String,Object>();
			unBindMap.put("interfaceCode", "jbCard");
			unBindMap.put("accountId", accountId);
			unBindMap.put("bindId", bindId);
			unBindMap.put("payToken", resultMap.get("payToken"));
			unBindMap.put("hardwareId", request.getSession().getId());//硬件id
			if(logger.isInfoEnabled()){
				logger.info("请求 "+unBindMap.get("interfaceCode")+" 接口，请求参数："+unBindMap);
			}
			if(false){
				resultMap = jbcardInterfaceImpl.doService(reqMap);
			}
			
		}
		
		request.setAttribute("accountId", accountId.trim());
		request.setAttribute("payOrderId", payOrderId.trim());
		request.setAttribute("bindId", bindId.trim());
		return resultMap;
	}

}
