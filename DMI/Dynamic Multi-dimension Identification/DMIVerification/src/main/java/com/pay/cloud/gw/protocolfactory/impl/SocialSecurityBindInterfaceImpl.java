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
 * 社保卡绑定
 * @author qingsong.li
 *
 */
@Service("socialSecurityBindInterfaceImpl")
public class SocialSecurityBindInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(SocialSecurityBindInterfaceImpl.class);
	
	@Resource(name="socialSecurityServiceGWImpl")
	private SocialSecurityServiceGW socialSecurityServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10017_ERROR_USER_ID.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10017_ERROR_USER_ID.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("cardName"))){			
			returnMap.put("resultCode", Hint.SYS_10021_ERROR_CARD_NAME.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10021_ERROR_CARD_NAME.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.checkChineseName(receiveMap.get("cardName").toString())){			
			returnMap.put("resultCode", Hint.SYS_10022_CARDNAME_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10022_CARDNAME_INVALID_ERROR.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("cardNo").toString())){			
			returnMap.put("resultCode", Hint.SYS_10019_ERROR_CARD_NO.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10019_ERROR_CARD_NO.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isIdCard(receiveMap.get("cardNo").toString())){			
			returnMap.put("resultCode", Hint.SYS_10020_ERROR_CARD_NO.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10020_ERROR_CARD_NO.getMessage());
			return returnMap;
		}
		
		if(!ValidateUtils.isBlank(receiveMap.get("cityId"))){			
			returnMap.put("resultCode", Hint.SYS_10023_ERROR_CITY_ID.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10023_ERROR_CITY_ID.getMessage());
			return returnMap;
		}
		
		try {
			return socialSecurityServiceGW.socialSecurityBind(receiveMap);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25027_LOGIN_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25027_LOGIN_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
