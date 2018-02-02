package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.SmsManagerServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 验证短信验证码
 * @ClassName: SmsValidateCodeImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午3:00:34
 */
@Service("smsValidateCodeImpl")
public class SmsValidateCodeImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(SmsValidateCodeImpl.class);
	
	@Resource(name="smsManagerServiceGWImpl")
	private SmsManagerServiceGW smsManagerServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("phone"))){			
			returnMap.put("resultCode", Hint.SMS_60011_PHONE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60011_PHONE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("hardwareId"))){			
			returnMap.put("resultCode", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("verifyCode"))){			
			returnMap.put("resultCode", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		try {
			return smsManagerServiceGWImpl.smsValidateCode(receiveMap.get("phone").toString(), receiveMap.get("hardwareId").toString(),receiveMap.get("verifyCode").toString());
		} catch (Exception e) {
			logger.error("验证短信验证码错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SMS_60016_SMSVALIDATECODE_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.SMS_60016_SMSVALIDATECODE_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
