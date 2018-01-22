package com.pay.cloud.common.dict.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pay.cloud.common.dict.entity.DimDict;
import com.pay.cloud.common.dict.entity.DimSysConf;

/**
 * 数据字典服务接口
 */
public interface DictService {
	
	/**
	 * 获取系统基础配置表数据集合
	 * @return
	 */
	List<DimSysConf> findDimSysConfList(DimSysConf dimSysConf) throws Exception;
	
	/**
	 * 查询基础字典数据集合
	 * @return
	 * @throws Exception
	 */
	List<DimDict> findDimDictList(DimDict dimDict) throws Exception;
	
	/**
	 * 查询字典更新数据集合
	 * @return
	 * @throws Exception
	 */
	Map<String,Date> findDimCacheUpd() throws Exception;

}
