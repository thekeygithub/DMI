package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mysql.jdbc.StringUtils;
import com.pay.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.Md5SaltUtils;
import com.pay.cloud.util.hint.Hint;
/**
 *  验证支付密码（进行新卡绑定并支付）
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月19日 
 * @time 下午1:35:21
 * @version V1.0
 * @修改记录
 *
 */
@Service("bindVerifyPWDServiceImpl")
public class BindVerifyPWDServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(UnBindCardServiceImpl.class);
	
	@Resource
	VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = (String)map.get("accountId");
		String payOrderId = (String)map.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		
		if(StringUtils.isNullOrEmpty(payOrderId) || StringUtils.isNullOrEmpty(accountId) 
				|| StringUtils.isNullOrEmpty(payPassword) ){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString().replace("{param}", "payOrderId"));
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage());
			return resultMap;
		}
		//支付密码进行md5加密
		payPassword = Md5SaltUtils.encodeMd5(payPassword);
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payPassword", payPassword);
		reqMap.put("payOrderId", payOrderId);
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
		
		return resultMap;
	}

}
