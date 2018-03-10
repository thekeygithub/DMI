package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.impl.PayOrderServiceGWImpl;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 *  查询交易单
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年8月10日 
 * @time 下午2:18:20
 * @version V1.0
 * @修改记录
 *
 */
@Service("queryTradeInfoInterfaceImpl")
public class QueryTradeInfoInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(QueryTradeInfoInterfaceImpl.class);
	
	@Resource
	private PayOrderServiceGWImpl payOrderServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		if(logger.isInfoEnabled()){
			logger.info("查询交易单");
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		String payOrderId = (String) receiveMap.get("payOrderId");//支付订单号
		if(ValidateUtils.isEmpty(payOrderId)){
			receiveMap.put("payOrderId","");
		}else {
			if(!ValidateUtils.isNumber(payOrderId)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "payOrderId"));
				return resultMap;
			}
		}
		String merOrderId = (String) receiveMap.get("merOrderId");//易宝交易流水号
		if(ValidateUtils.isEmpty(merOrderId)){
			receiveMap.put("merOrderId", "");
		}
		
		if(ValidateUtils.isEmpty(payOrderId) && ValidateUtils.isEmpty(merOrderId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}
		
		String accountId = (String) receiveMap.get("accountId");//用户账户ID
		if(ValidateUtils.isEmpty(accountId)){
			receiveMap.put("accountId","");
		}else{
			if(!ValidateUtils.isPositiveInteger(accountId)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
				return resultMap;
			}
		}
		
		try{
			resultMap = payOrderServiceGWImpl.queryOrder(receiveMap);
			
		}catch(Exception e){
			if(logger.isInfoEnabled()){
				logger.error("查询交易单异常：" + e.getMessage(), e);
			}
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		
		return resultMap;
	}
}
