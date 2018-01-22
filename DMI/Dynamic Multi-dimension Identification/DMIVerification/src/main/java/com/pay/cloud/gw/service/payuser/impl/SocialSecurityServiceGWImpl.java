package com.pay.cloud.gw.service.payuser.impl;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.gw.service.payuser.SocialSecurityServiceGW;
import com.pay.cloud.pay.payuser.service.impl.SocialSecurityServiceImpl;

@Service("socialSecurityServiceGWImpl")
public class SocialSecurityServiceGWImpl implements SocialSecurityServiceGW{

	
	@Resource(name="socialSecurityServiceImpl")
	private SocialSecurityServiceImpl socialSecurityServiceImpl;
	@Resource(name="redisService")
	private RedisService redisService;
	/**
	 * 社保卡绑定
	 */
	@Override
	public Map<String, Object> socialSecurityBind(Map<String, Object> inputMap) {
		return socialSecurityServiceImpl.socialSecurityBind(inputMap);
	}

	/**
	 * 社保卡解绑
	 */
	@Override
	public Map<String, Object> socialSecurityUnbind(Map<String, Object> inputMap) {
		return socialSecurityServiceImpl.socialSecurityUnbind(inputMap);
	}

	/**
	 * 社保卡查询
	 */
	@Override
	public Map<String, Object> socialSecurityQuery(Map<String, Object> inputMap) {
		return socialSecurityServiceImpl.socialSecurityQuery(inputMap);
	}

	/**
	 * 社保绑卡字段配置接口
	 */
	@Override
	public Map<String, Object> socialSecurityConfig(Map<String, Object> inputMap) {
		return socialSecurityServiceImpl.socialSecurityConfig(inputMap);
	}

	@Override
	public Map<String, Object> socialSecurityReal(Map<String, Object> inputMap) throws Exception {
		return socialSecurityServiceImpl.socialSecurityRealMain(inputMap);
	}

	@Override
	public Map<String, Object> socialSecurityQueryMoney(Map<String, Object> inputMap) {
		
		return socialSecurityServiceImpl.socialSecurityQueryMoney(inputMap);
	}

}
