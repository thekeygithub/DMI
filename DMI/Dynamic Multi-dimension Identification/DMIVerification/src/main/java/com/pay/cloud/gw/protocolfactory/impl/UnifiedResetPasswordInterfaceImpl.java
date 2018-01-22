package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 
 * @Description: 设置登录密码
 * @ClassName: LoginPassImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午4:02:35
 */
@Service("unifiedResetPasswordInterfaceImpl")
public class UnifiedResetPasswordInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(UnifiedResetPasswordInterfaceImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("password"))){			
			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
			return returnMap;
		}
//		if(receiveMap.get("password").toString().trim().length()>16){			
//			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
//			return returnMap;
//		}
		receiveMap.put("password", receiveMap.get("password").toString().trim());//处理前后空格
	
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("userCode"))){			
			returnMap.put("resultCode", Hint.SMS_60011_PHONE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60011_PHONE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
//		if(!ValidateUtils.isNumber(receiveMap.get("operateType"))){			
//			returnMap.put("resultCode", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getMessage());
//			return returnMap;
//		}
		if(!ValidateUtils.isBlank(receiveMap.get("hardwareId"))){			
			returnMap.put("resultCode", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getMessage());
			return returnMap;
		}
//		if(!ValidateUtils.isBlank(receiveMap.get("verifyCode"))){			
//			returnMap.put("resultCode", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getMessage());
//			return returnMap;
//		}
		try {
			return payUserServiceGWImpl.unifiedResetPassword(receiveMap);
		} catch (Exception e) {
			logger.error("设置登录密码错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25028_RESTLOGINPASS_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25028_RESTLOGINPASS_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
