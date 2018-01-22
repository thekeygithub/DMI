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
 * @Description: 用户注册
 * @ClassName: RegisterImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午3:56:31
 */
@Service("realNameAuthInterfaceImpl")
public class RealNameAuthInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(RealNameAuthInterfaceImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10017_ERROR_USER_ID.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10017_ERROR_USER_ID.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isBlank(receiveMap.get("bankCode"))){			
			returnMap.put("resultCode", Hint.SYS_10018_ERROR_BANK_CODE.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10018_ERROR_BANK_CODE.getMessage());
			return returnMap;
		}
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("phone"))){			
			returnMap.put("resultCode", Hint.SMS_60011_PHONE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60011_PHONE_FORMAT_FAILED.getMessage());
			return returnMap;
		}
//		if(!ValidateUtils.isBlank(receiveMap.get("verifyCode"))){
//			returnMap.put("resultCode", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.SMS_60014_VERIFYCODE_FORMAT_FAILED.getMessage());
//			return returnMap;
//		}
		try {
			return payUserServiceGWImpl.realNameGW(receiveMap);
		} catch (Exception e) {
			logger.error("实名认证：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.TD_13037_REAL_NAME_WITH_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.TD_13037_REAL_NAME_WITH_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
