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
 * 查询已绑定有效的银行卡
 * 
 * 2016-4-15
 * @author haiyan.zhang
 */
@Service("queryCardListInterfaceImpl")
public class QueryCardListInterfaceImpl implements DoServiceInterface {
	
	private static Logger logger = Logger.getLogger(QueryCardListInterfaceImpl.class);
	
	@Resource
	private CardServiceGWImpl cardServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String accountId = (String) receiveMap.get("accountId");//用户账户
		if(ValidateUtils.isEmpty(accountId)){
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
		
		try{
			Map<String,Object> inputMap = new HashMap<String,Object>();
			inputMap.put("accountId", accountId);
			inputMap.put("hardwareId", receiveMap.get("hardwareId"));
			inputMap.put("payToken", payToken);
			resultMap = cardServiceGWImpl.queryCardList(inputMap);
			
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
