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
@Service("realNameAuthImportInterfaceImpl")
public class RealNameAuthImportInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(RealNameAuthImportInterfaceImpl.class);
	
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
		if(!ValidateUtils.isBlank(receiveMap.get("bankCode"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "bankCode"));
			return returnMap;
		}
		if(receiveMap.get("bankCode").toString().length()>30){
			returnMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "bankCode"));
			return returnMap;
		}
		if(!ValidateUtils.isPhoneNumber(receiveMap.get("phone"))){			
			returnMap.put("resultCode", Hint.SMS_60011_PHONE_FORMAT_FAILED.getCodeString());
			returnMap.put("resultDesc", Hint.SMS_60011_PHONE_FORMAT_FAILED.getMessage());
			return returnMap;
		}

		if(receiveMap.get("cardNo")!=null && !"".equals(receiveMap.get("cardNo")) && !ValidateUtils.isIdCard(receiveMap.get("cardNo").toString())){			
			returnMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "cardNo"));
			return returnMap;
		}
		
		if(!ValidateUtils.isBlank(receiveMap.get("cardName"))){			
			returnMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cardName"));
			return returnMap;
		}
		if(receiveMap.get("cardName").toString().length()>20){
			returnMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "cardName"));
			return returnMap;
		}
		try {
			return payUserServiceGWImpl.realNameImportGW(receiveMap);
		} catch (Exception e) {
			logger.error("实名认证：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25029_REGISTER_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25029_REGISTER_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
