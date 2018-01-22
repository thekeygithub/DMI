package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.util.hint.Hint;

/**
 * 
 * @Description: 配置文件
 * @ClassName: ConfigurationImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午4:04:00
 */
@Service("configurationImpl")
public class ConfigurationImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(ConfigurationImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
//		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
//			returnMap.put("resultCode", Hint.USER_25022_USERID_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.USER_25022_USERID_FAILED.getMessage());
//			return returnMap;
//		}
		try {
			return payUserServiceGWImpl.queryConfiguration(receiveMap);
		} catch (Exception e) {
			logger.error("获取配置文件错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25024_CONFIGURATION_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25024_CONFIGURATION_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}
