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
 * 统一登录接口
 * @author qingsong.li
 *
 */
@Service("unifiedLoginInterfaceImpl")
public class UnifiedLoginInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(UnifiedLoginInterfaceImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(ValidateUtils.isEmpty(String.valueOf(receiveMap.get("userCode")))){
			returnMap.put("resultCode", Hint.USER_25025_USERCODE_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25025_USERCODE_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isPhoneNumber(String.valueOf(receiveMap.get("userCode")))){
			returnMap.put("resultCode", Hint.USER_25025_USERCODE_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25025_USERCODE_FAILED.getMessage());
			return returnMap;
		}
		if(ValidateUtils.isEmpty(String.valueOf(receiveMap.get("password")))){
			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
			return returnMap;
		}
		try {
			return payUserServiceGWImpl.unifiedLogin(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25027_LOGIN_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25027_LOGIN_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
