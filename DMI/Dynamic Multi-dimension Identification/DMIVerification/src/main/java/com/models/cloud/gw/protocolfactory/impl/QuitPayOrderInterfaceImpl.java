package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.impl.TdOrderServiceGWImpl;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 放弃付款
 */
@Service("quitPayOrderInterfaceImpl")
public class QuitPayOrderInterfaceImpl implements DoServiceInterface {

private static Logger logger = Logger.getLogger(QueryCardListInterfaceImpl.class);
	
	@Resource
	private TdOrderServiceGWImpl tdOrderServiceGWImpl;
	@Resource
	private RedisService redisService;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<>();
		
		String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
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
		
		String payOrderId = String.valueOf(receiveMap.get("payOrderId")).trim();//支付订单号
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
		String quitFlag = String.valueOf(receiveMap.get("quitFlag")).trim();//通知业务线标识 0或为空-通知 1-不通知
		if(ValidateUtils.isEmpty(quitFlag)){
			quitFlag = "";
		}

		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		try{
			Map<String,Object> inputMap = new HashMap<>();
			inputMap.put("accountId", accountId);
			inputMap.put("hardwareId", receiveMap.get("hardwareId"));
			inputMap.put("payOrderId", payOrderId);
			inputMap.put("quitFlag", quitFlag);

			int expire = 60;
			Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
			if(logger.isInfoEnabled()){
				logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
			}
			if(null == redisValue || redisValue == 0){
				if(logger.isInfoEnabled()){
					logger.info("当前订单发生并发请求，已阻止");
				}
				resultMap.put("resultCode", Hint.SYS_10015_INTERFACE_CONCURRENT_PREVENT.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10015_INTERFACE_CONCURRENT_PREVENT.getMessage());
			}else{
				resultMap = tdOrderServiceGWImpl.quitOrder(inputMap);
				redisService.delete(redisKey);
			}
		}catch(Exception e){
			if(logger.isInfoEnabled()){
				logger.error("系统错误：" + e.getMessage(), e);
			}
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			redisService.delete(redisKey);
		}
		
		return resultMap;
	}

}
