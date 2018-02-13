package com.models.cloud.common.cache.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.cache.account.entity.ActSpInfo;
import com.models.cloud.common.cache.account.service.AccountService;
import com.models.cloud.common.cache.service.AccountCacheService;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;

/**
 * 商户用户缓存服务实现类
 */
@Service("accountCacheServiceImpl")
public class AccountCacheServiceImpl implements AccountCacheService{
	
	//日志
	private static Logger log = Logger.getLogger(AccountCacheServiceImpl.class);
	
	//账户服务接口
	@Resource(name="accountServiceImpl")
	private AccountService accountServiceImpl;

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.cache.service.AccountCacheService#findActSpInfoByAppId(long)
	 */
	@Override
	public ActSpInfo findActSpInfoByAppId(String chanAppId) {
		ActSpInfo info =  accountServiceImpl.findActInfoByAppId(chanAppId);
		try{
			if (null != info){
				//解密
				info.setServerKeyPub(EncryptUtils.aesDecrypt(info.getServerKeyPub(),CipherAesEnum.SECRETKEY));
				info.setServerKeyPri(EncryptUtils.aesDecrypt(info.getServerKeyPri(),CipherAesEnum.SECRETKEY));
				info.setSpKeyPub(EncryptUtils.aesDecrypt(info.getSpKeyPub(),CipherAesEnum.SECRETKEY));
				info.setSpKeyPri(EncryptUtils.aesDecrypt(info.getSpKeyPri(),CipherAesEnum.SECRETKEY));
			}
			return info;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
