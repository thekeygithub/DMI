package com.models.cloud.common.cache.account.service;

import java.util.List;

import com.models.cloud.common.cache.account.entity.ActSpInfo;

/**
 * 商户账户服务类
 */
public interface AccountService {
	
	/**
	 * 获取商户账户扩展信息数据集合
	 * @return
	 */
	List<ActSpInfo> findActSpInfoList() throws Exception;
	
	/**
	 * 根据主键获取商户账户扩展信息数据
	 * @param actId
	 * @return
	 */
	ActSpInfo findActInfoByAppId(String chanAppId);

}
