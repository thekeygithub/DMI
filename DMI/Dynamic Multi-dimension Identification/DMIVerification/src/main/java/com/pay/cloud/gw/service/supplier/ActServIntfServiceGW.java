package com.pay.cloud.gw.service.supplier;

import java.util.Map;


public interface ActServIntfServiceGW {
	
	/**
	 * 
	 * @Description: 账户是否有接口访问权限
	 * @Title: checkPrivilege 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> checkPrivilege(Map<String,Object> receiveMap);
	
}
