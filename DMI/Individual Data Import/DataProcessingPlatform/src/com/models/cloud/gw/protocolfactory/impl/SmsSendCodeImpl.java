package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.SmsManagerServiceGW;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 发送短信验证码
 * @ClassName: SmsSendCodeImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午3:00:06
 */
@Service("smsSendCodeImpl")
public class SmsSendCodeImpl implements DoServiceInterface{
	
	private static final Logger logger = Logger.getLogger(SmsSendCodeImpl.class);
	
	@Resource(name="smsManagerServiceGWImpl")
	private SmsManagerServiceGW smsManagerServiceGWImpl;
	
	/**
	 * 
	 * @Description: //开始验证各种信息,验证不通过返回错误码
	 * @Title: doService
	 * @param receiveMap
	 * @return 
	 * @see com.models.cloud.gw.protocolfactory.DoServiceInterface#doService(java.util.Map)
	 */
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("phone"))){			
			returnMap.put("resultCode", Hint.SMS_60011_PHONE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60011_PHONE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isNumber(receiveMap.get("operateType"))){			
			returnMap.put("resultCode", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("hardwareId"))){			
			returnMap.put("resultCode", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		try {
			return smsManagerServiceGWImpl.smsSendCode(receiveMap.get("appId").toString(),receiveMap.get("phone").toString(), receiveMap.get("hardwareId").toString(), ConvertUtils.getInt(receiveMap.get("operateType")),
					ConvertUtils.getString(receiveMap.get("userPhone")) );
		} catch (Exception e) {
			logger.error("发送短信验证码错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SMS_60015_SMSSENDCODE_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.SMS_60015_SMSSENDCODE_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
