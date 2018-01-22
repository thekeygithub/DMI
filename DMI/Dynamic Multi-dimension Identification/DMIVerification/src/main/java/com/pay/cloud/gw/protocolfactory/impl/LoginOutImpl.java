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
 * @Description: 登出系统
 * @ClassName: LoginOutImpl 
 * @author: zhengping.hu
 * @date: 2016年4月21日 下午3:05:23
 */
@Service("loginOutImpl")
public class LoginOutImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(LoginOutImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.USER_25025_USERCODE_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.USER_25025_USERCODE_FAILED.getMessage());
			return returnMap;
		}
		try {
			return payUserServiceGWImpl.loginOut(receiveMap);
		} catch (Exception e) {
			logger.error("登出系统错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25032_LOGINOUT_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25032_LOGINOUT_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
