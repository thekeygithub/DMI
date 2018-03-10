package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.SmsValidateCodeImpl;
import com.models.cloud.h5.controller.DoPageService;
/**
 * 下发验证码查询
 * @author enjuan.ren
 *
 */
@Service("socialSmsValidateCodeImpl")
public class SocialSmsValidateCodeImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SocialSmsValidateCodeImpl.class);
	
	@Resource(name="smsValidateCodeImpl")
	SmsValidateCodeImpl smsValidateCodeImpl;
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		String phone=(String)receiveMap.get("phone");
		String verifyCode=(String)receiveMap.get("verifyCode");
		receiveMap.put("phone", phone);
		receiveMap.put("verifyCode", verifyCode);
		receiveMap.put("interfaceCode", "verifymessage");
		if(logger.isInfoEnabled()){
			logger.info("社保卡绑卡主卡完成验证-验证码");
		}
		Map<String, Object> returnMap=smsValidateCodeImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return returnMap;
	}

}
