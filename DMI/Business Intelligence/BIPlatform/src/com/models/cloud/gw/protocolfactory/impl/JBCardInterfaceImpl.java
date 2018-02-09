package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.impl.CardServiceGWImpl;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
/**
 * 解绑银行卡
 * 
 * 2016-4-15
 * @author haiyan.zhang
 */

@Service("jbcardInterfaceImpl")
public class JBCardInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(JBCardInterfaceImpl.class);

	@Resource
	private CardServiceGWImpl cardServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String bindId = (String) receiveMap.get("bindId");//绑卡 ID
		if(ValidateUtils.isEmpty(bindId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "bindId"));
			return resultMap;
		}
		if(!ValidateUtils.isNumber(bindId)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "bindId"));
            return resultMap;
        }
		
		String accountId = (String) receiveMap.get("accountId");//用户账户
		if(!ValidateUtils.isBlank(accountId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "accountId"));
			return resultMap;
		}
		if(!ValidateUtils.isNumber(accountId)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
            return resultMap;
        }
		
		String payToken = (String) receiveMap.get("payToken");//通行证
		if(!ValidateUtils.isBlank(payToken)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payToken"));
			return resultMap;
		}
		
		try{
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("accountId", accountId);
			inputMap.put("bindId", bindId);
			inputMap.put("hardwareId", receiveMap.get("hardwareId"));
			inputMap.put("payToken", payToken);
			resultMap = cardServiceGWImpl.unBindCard(inputMap);
			
		}catch(Exception e){
			if(logger.isInfoEnabled()){
				logger.error("系统错误：" + e.getMessage(), e);
			}
			 resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			 resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}

}
