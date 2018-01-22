package com.pay.cloud.gw.service.payuser;

import java.util.Map;

public interface SocialSecurityServiceGW {

	/**
	 * 社保卡绑定
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityBind(Map<String, Object> inputMap);
	
	/**
	 * 社保卡解绑
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityUnbind(Map<String, Object> inputMap);
	
	/**
	 * 社保卡查询
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityQuery(Map<String, Object> inputMap);
	/**
	 * 社保卡余额查询
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityQueryMoney(Map<String, Object> inputMap);
	
	/**
	 * 社保绑卡字段配置接口
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityConfig(Map<String, Object> inputMap);
	
	/**
	 * 社保绑卡实名认证
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> socialSecurityReal(Map<String, Object> inputMap) throws Exception;
}
