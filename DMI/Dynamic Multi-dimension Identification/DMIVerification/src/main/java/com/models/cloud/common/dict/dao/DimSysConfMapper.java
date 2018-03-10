package com.models.cloud.common.dict.dao;

import java.util.List;
import java.util.Map;

import com.models.cloud.common.dict.entity.DimSysConf;

/**
 * 系统基础配置表Mapper类 
 */
public interface DimSysConfMapper {
	
    /**
     * 根据条件查询配置信息集合
     * @param dimSysConf
     * @return
     */
    List<DimSysConf> findDimSysConfList(DimSysConf dimSysConf);
    
   
}