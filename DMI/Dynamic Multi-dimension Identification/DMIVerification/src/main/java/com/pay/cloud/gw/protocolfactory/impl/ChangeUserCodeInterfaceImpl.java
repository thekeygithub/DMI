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
@Service("changeUserCodeInterfaceImpl")
public class ChangeUserCodeInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(ChangeUserCodeInterfaceImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10053_USERID_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10053_USERID_NOT_NULL_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("oldUserCode"))){			
			returnMap.put("resultCode", Hint.SYS_10054_OLDUSERCODE_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10054_OLDUSERCODE_NOT_NULL_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("oldUserCode"))){			
			returnMap.put("resultCode", Hint.SYS_10051_USERCODE_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10051_USERCODE_INVALID_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("password"))){			
			returnMap.put("resultCode", Hint.SYS_10049_PASSWORD_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10049_PASSWORD_NOT_NULL_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("newUserCode"))){			
			returnMap.put("resultCode", Hint.SYS_10055_NEWUSERCODE_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10055_NEWUSERCODE_NOT_NULL_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("newUserCode"))){			
			returnMap.put("resultCode", Hint.SYS_10051_USERCODE_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10051_USERCODE_INVALID_ERROR.getMessage());
			return returnMap;
		}
		if(receiveMap.get("newUserCode").toString().equals(receiveMap.get("oldUserCode").toString())){
			returnMap.put("resultCode", Hint.UC_CENTER_ADDR_32018_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.UC_CENTER_ADDR_32018_ERROR.getMessage());
			return returnMap;
		}
//		if(!ValidateUtils.isBlank(receiveMap.get("verifyCode"))){			
//			returnMap.put("resultCode", Hint.SYS_10052_VERIFYCODE_NOT_NULL_ERROR.getCodeString());
//			returnMap.put("resultDesc", Hint.SYS_10052_VERIFYCODE_NOT_NULL_ERROR.getMessage());
//			return returnMap;
//		}
		try {
			return payUserServiceGWImpl.changeUserCodeGW(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25027_LOGIN_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25027_LOGIN_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
