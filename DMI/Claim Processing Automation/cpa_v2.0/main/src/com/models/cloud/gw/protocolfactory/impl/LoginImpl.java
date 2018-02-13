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
 * @Description: 用户登录
 * @ClassName: LoginImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午4:01:34
 */
@Service("loginImpl")
public class LoginImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(LoginImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userCode"))){			
			returnMap.put("resultCode", Hint.USER_25025_USERCODE_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25025_USERCODE_FAILED.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("password"))){			
			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
			return returnMap;
		}
		if((receiveMap.get("verifyCode")!=null && !receiveMap.get("verifyCode").equals("")) && !ValidateUtils.checkLength(receiveMap.get("verifyCode").toString(), 4, 4)){			
			returnMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
			return returnMap;
		}
		try {
			return payUserServiceGWImpl.login(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25027_LOGIN_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25027_LOGIN_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
