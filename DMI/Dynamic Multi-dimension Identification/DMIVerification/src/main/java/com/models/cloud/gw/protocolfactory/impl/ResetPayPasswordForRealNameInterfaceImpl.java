package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 用户注册
 * @ClassName: RegisterImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午3:56:31
 */
@Service("resetPayPasswordForRealNameInterfaceImpl")
public class ResetPayPasswordForRealNameInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(ResetPayPasswordForRealNameInterfaceImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userId"));
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("password"))){			
			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("token"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "token"));
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("hardwareId"))){			
			returnMap.put("resultCode", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60013_HARDWAREID_FORMAT_FAILED.getMessage());
			return returnMap;
		}
		try {
			return payUserServiceGWImpl.resetPayPasswordForRealNameGW(receiveMap);
		} catch (Exception e) {
			logger.error("注册错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25029_REGISTER_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25029_REGISTER_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
