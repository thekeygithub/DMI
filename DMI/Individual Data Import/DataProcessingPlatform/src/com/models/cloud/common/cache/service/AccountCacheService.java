package com.models.cloud.common.cache.service;

import com.models.cloud.common.cache.account.entity.ActSpInfo;

/**
 * 商户账户缓存服务类
 */
public interface AccountCacheService {
	
	/**
	 * 根据渠道商户APPID获取账户信息
	 * @param actId
	 * @return
	 */
	ActSpInfo findActSpInfoByAppId(String chanAppId);

}
