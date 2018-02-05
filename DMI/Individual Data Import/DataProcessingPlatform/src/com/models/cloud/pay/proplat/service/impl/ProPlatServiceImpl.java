package com.models.cloud.pay.proplat.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.pay.proplat.dao.PpIntfParaMapper;
import com.models.cloud.pay.proplat.dao.PpIntfPropMapper;
import com.models.cloud.pay.proplat.entity.PpIntfPara;
import com.models.cloud.pay.proplat.entity.PpIntfProp;
import com.models.cloud.pay.proplat.service.ProPlatService;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.secrity.crypto.AesCipher;

/**
 * 项目与资金平台服务实现类
 */
@Service("proPlatServiceImpl")
public class ProPlatServiceImpl implements ProPlatService {

	//日志
	private static Logger log = Logger.getLogger(ProPlatServiceImpl.class);
	
	//资金平台交互属性Mapper服务
	@Autowired
	private PpIntfPropMapper ppIntfPropMapper;
	//资金平台交互属性Mapper服务
	@Autowired
	private PpIntfParaMapper ppIntfParaMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.proplat.service.ProPlatService#findPpIntfPropList()
	 */
	@Override
	public List<PpIntfProp> findPpIntfPropList() throws Exception {
		List<PpIntfProp> lst = ppIntfPropMapper.findPpIntfPropList();
		if(null!=lst){
			AesCipher ac = new AesCipher();
			for(PpIntfProp pp:lst){
				//解密
				pp.setPpPubKey(EncryptUtils.aesDecrypt(pp.getPpPubKey(),CipherAesEnum.SECRETKEY));
				pp.setEbaoPubKey(EncryptUtils.aesDecrypt(pp.getEbaoPubKey(),CipherAesEnum.SECRETKEY));
				pp.setEbaoPriKey(EncryptUtils.aesDecrypt(pp.getEbaoPriKey(),CipherAesEnum.SECRETKEY));
				pp.setEbaoDesKey(EncryptUtils.aesDecrypt(pp.getEbaoDesKey(),CipherAesEnum.SECRETKEY));
			}
		}
		return lst;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.ccb.proplat.service.ProPlatService#findPpIntfParaList()
	 */
	@Override
	public List<PpIntfPara> findPpIntfParaList() throws Exception {
		List<PpIntfPara> lst = ppIntfParaMapper.selectPpIntfParaList();
		if(null!=lst){
			for(PpIntfPara pp:lst){
				//解密
				if(BaseDictConstant.ENCRYPT_FLAG_YES == pp.getEncryptFlag()){
					pp.setVal(EncryptUtils.aesDecrypt(pp.getVal(),CipherAesEnum.PPINTFPRAR));
				}
			}
		}
		return lst;
	}
}
