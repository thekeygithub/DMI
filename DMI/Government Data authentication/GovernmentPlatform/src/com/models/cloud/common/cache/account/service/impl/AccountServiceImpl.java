package com.models.cloud.common.cache.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.common.cache.account.dao.ActSpInfoMapper;
import com.models.cloud.common.cache.account.entity.ActSpInfo;
import com.models.cloud.common.cache.account.service.AccountService;

/**
 * 商户账户服务实现类
 */
@Service("accountServiceImpl")
public class AccountServiceImpl implements AccountService {
	
	//日志
	private static Logger log = Logger.getLogger(AccountServiceImpl.class);
	
	//商户账户扩展信息Mapper服务
	@Autowired
	private ActSpInfoMapper actSpInfoMapper;

    /*
     * (non-Javadoc)
     * @see com.ebaonet.cloud.common.cache.account.service.AccountService#findActSpInfoList()
     */
	@Override
	public List<ActSpInfo> findActSpInfoList() throws Exception {
		return actSpInfoMapper.findSpInfoList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.cache.account.service.AccountService#findActInfoByAppId(java.lang.Long)
	 */
	@Override
	public ActSpInfo findActInfoByAppId(String chanAppID) {
		// TODO Auto-generated method stub
		return actSpInfoMapper.findSpInfoByAppId(chanAppID);
	}

}
