package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.UcUserAddrServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

@Service("ucUserAddrQueryInterfaceImpl")
public class UcUserAddrQueryInterfaceImpl implements DoServiceInterface{
	
	private static final Logger logger = Logger.getLogger(UcUserAddrQueryInterfaceImpl.class);
	
	@Resource(name="ucUserAddrServiceGWImpl")
	private UcUserAddrServiceGW ucUserAddrServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 String payUserId = String.valueOf(receiveMap.get("userId")).trim();
		 if(ValidateUtils.isEmpty(payUserId)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getMessage().replace("{param}", "userId"));
	            return resultMap;
	        }
		 try {
			resultMap=ucUserAddrServiceGWImpl.ucUserAddrQuery(receiveMap);
		} catch (Exception e) {
			logger.error("系统错误：" + e.getMessage(), e);
			 resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
	         resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		 if(ValidateUtils.isEmpty(payUserId)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getMessage().replace("{param}", "userId"));
	            return resultMap;
	        }
		return resultMap;
	}

}
