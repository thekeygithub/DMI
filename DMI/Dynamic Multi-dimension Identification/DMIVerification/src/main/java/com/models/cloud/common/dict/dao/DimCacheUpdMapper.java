package com.models.cloud.common.dict.dao;

import java.util.List;

import com.models.cloud.common.dict.entity.DimCacheUpd;

/**
 * 字典更新状态Mapper类
 */
public interface DimCacheUpdMapper {
    
	/**
	 * 获取字典更新状态List
	 * @return
	 */
    List<DimCacheUpd> findDimCacheList();
}