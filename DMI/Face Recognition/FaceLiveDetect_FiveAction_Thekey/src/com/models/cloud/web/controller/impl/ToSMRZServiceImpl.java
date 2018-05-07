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
import com.models.cloud.gw.protocolfactory.impl.RealNameAuthInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 实名认证发送短信验证码
 * @author haiyan.zhang
 *
 */
@Service("pcToSMRZServiceImpl")
public class ToSMRZServiceImpl implements DoWebPageService{
	private Logger logger = Logger.getLogger(ToSMRZServiceImpl.class);
	
	@Resource
	private RealNameAuthInterfaceImpl realNameAuthInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("实名认证");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
//		String phone =String.valueOf((Long) sessionMap.get("phone"));
		
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("interfaceCode", "realNameAuth");
		sendMap.put("userId", (Long)sessionMap.get("userId"));//用户编号
		sendMap.put("bankCode", (String)map.get("cardNo"));//银行卡号
		sendMap.put("phone", (String)map.get("phone"));	//手机号
		sendMap.put("cardName", (String)map.get("owner")); //姓名
		sendMap.put("cardNo", (String)map.get("idCard")); //身份证号
		sendMap.put("verifyCode", (String)map.get("validateCode")); //短信验证码
		sendMap.put("hardwareId", (String)map.get("hardwareId"));
		sendMap.put("appId", map.get("appId"));
		
		
//		if(logger.isInfoEnabled()){
//			logger.info("请求 "+sendMap.get("interfaceCode")+" 接口，请求参数："+sendMap);
//		}
		
		resultMap = realNameAuthInterfaceImpl.doService(sendMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+sendMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage("realNameAuth", resultMap);
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			sessionMap.put("token", resultMap.get("token"));
			sessionMap.put("realNameStatus", "1");
			sessionMap.put("realNameName", resultMap.get("realNameName"));
			sessionMap.put("realNameCardNo", resultMap.get("realNameCardNo"));
			session.setAttribute(SmsConstant.SESSION_USER_KEY,sessionMap);
			
		}
		
		if(logger.isInfoEnabled()){
			logger.info("转换后的结果："+resultMap);
		}
		return resultMap;
	}

}
