package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.impl.TdOrderServiceGWImpl;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
/**
 * 
 * 查询交易单
 * 2016-4-18
 * @author haiyan.zhang
 */
@Service("searchPayOrderInterfaceImpl")
public class SearchPayOrderInterfaceImpl implements DoServiceInterface {
	private static Logger logger = Logger.getLogger(SearchPayOrderInterfaceImpl.class);

	@Resource
	private TdOrderServiceGWImpl tdOrderServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String payOrderId = (String) receiveMap.get("payOrderId");//支付订单号
		if(ValidateUtils.isEmpty(payOrderId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}
		if(!ValidateUtils.isNumber(payOrderId)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}
		
		String accountId = (String) receiveMap.get("accountId");//用户账户ID
		if(ValidateUtils.isEmpty(accountId)){
			accountId = "";
		}else{
			if(!ValidateUtils.isPositiveInteger(accountId)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
				return resultMap;
			}
		}
		String ebOrderId = (String) receiveMap.get("ebOrderId");//易宝交易流水号
		if(ValidateUtils.isEmpty(ebOrderId)){
			ebOrderId = "";
		}
		
		try{
			Map<String,Object> inputMap = new HashMap<String,Object>();
			inputMap.put("payOrderId", payOrderId);
			inputMap.put("accountId", accountId);
			inputMap.put("ebOrderId", ebOrderId);
			inputMap.put("hardwareId", receiveMap.get("hardwareId"));
			resultMap = tdOrderServiceGWImpl.queryTdOrder(inputMap);
			
		}catch(Exception e){
			if(logger.isInfoEnabled()){
				logger.error("系统错误：" + e.getMessage(), e);
			}
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		
		return resultMap;
	}

}
