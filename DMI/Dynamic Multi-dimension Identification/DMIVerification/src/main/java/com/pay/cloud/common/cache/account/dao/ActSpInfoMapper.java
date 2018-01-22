package com.pay.cloud.common.cache.account.dao;

import java.util.List;

import com.pay.cloud.common.cache.account.entity.ActSpInfo;

/**
 * 商户账户信息Mapper类
 */
public interface ActSpInfoMapper {
	
    /**
     * 根据渠道账户APPID获取商户账户信息
     * @param actId
     * @return
     */
    ActSpInfo findSpInfoByAppId(String chanAppID);
    
    /**
     * 获取商户账户信息集合类
     * @return
     */
    List<ActSpInfo> findSpInfoList();
}