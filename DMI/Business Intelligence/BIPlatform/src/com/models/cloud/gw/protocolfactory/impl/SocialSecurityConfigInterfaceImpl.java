package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.gw.service.payuser.SocialSecurityServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 社保卡解绑
 * @author qingsong.li
 *
 */
@Service("socialSecurityConfigInterfaceImpl")
public class SocialSecurityConfigInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(SocialSecurityConfigInterfaceImpl.class);
	
	@Resource(name="socialSecurityServiceGWImpl")
	private SocialSecurityServiceGW socialSecurityServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		
		try {
			return socialSecurityServiceGW.socialSecurityConfig(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SYS_10001_SYSTEM_ERROR.getCodeString());
            returnMap.put("resultDesc", Hint.SYS_10001_SYSTEM_ERROR.getMessage());
			return returnMap;
		}
	}
}
