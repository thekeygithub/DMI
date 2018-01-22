package com.pay.cloud.common.dict.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.common.dict.dao.DimCacheUpdMapper;
import com.pay.cloud.common.dict.dao.DimDictMapper;
import com.pay.cloud.common.dict.dao.DimSysConfMapper;
import com.pay.cloud.common.dict.entity.DimCacheUpd;
import com.pay.cloud.common.dict.entity.DimDict;
import com.pay.cloud.common.dict.entity.DimSysConf;
import com.pay.cloud.common.dict.service.DictService;

/**
 * 数据字典实现类
 */
@Service("dictServiceImpl")
public class DictServiceImpl implements DictService{
	
	//日志
	private static Logger log = Logger.getLogger(DictServiceImpl.class);
	
	//系统基础配置表Mapper服务
	@Autowired
	private DimSysConfMapper dimSysConfMapper;
	
	//系统基础字典Mapper服务
	@Autowired
	private DimDictMapper dimDictMapper;
	
	//字典更新数据Mapper服务
	@Autowired
	private DimCacheUpdMapper dimCacheUpdMapper;

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.base.dict.service.DictService#findDimSysConfList()
	 */
	@Override
	public List<DimSysConf> findDimSysConfList(DimSysConf dimSysConf) throws Exception{
		return dimSysConfMapper.findDimSysConfList(dimSysConf);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.dict.service.DictService#findDimDictMap()
	 */
	@Override
	public List<DimDict> findDimDictList(DimDict dimDict) throws Exception {
		return dimDictMapper.findDimDictList(dimDict);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.dict.service.DictService#findDimCacheUpd()
	 */
	@Override
	public Map<String, Date> findDimCacheUpd() throws Exception {
		Map<String,Date> map = new HashMap<String,Date>();
		List<DimCacheUpd> lst = dimCacheUpdMapper.findDimCacheList();
		if(null != lst){
			for(DimCacheUpd upd:lst){
				map.put(upd.getTabName(), upd.getLastUpdTime());
			}
		}
		return map;
	}
	
	
	
	
	

}
