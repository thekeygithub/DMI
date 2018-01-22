package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.gw.service.payuser.SocialSecurityServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 社保卡查询
 * @author qingsong.li
 *
 */
@Service("socialSecurityQueryMoneyInterfaceImpl")
public class SocialSecurityQueryMoneyInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(SocialSecurityQueryMoneyInterfaceImpl.class);
	
	@Resource(name="socialSecurityServiceGWImpl")
	private SocialSecurityServiceGW socialSecurityServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userId"));
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("socialSecurityId"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "socialSecurityId"));
			return returnMap;
		}
		if(!ValidateUtils.isNumber(receiveMap.get("socialSecurityId"))){			
			returnMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "socialSecurityId"));
			return returnMap;
		}
		try {
			return socialSecurityServiceGW.socialSecurityQueryMoney(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SYS_10001_SYSTEM_ERROR.getCodeString());
            returnMap.put("resultDesc", Hint.SYS_10001_SYSTEM_ERROR.getMessage());
			return returnMap;
		}
	}
}
