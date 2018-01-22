package com.pay.cloud.common.dict.dao;

import java.util.List;

import com.pay.cloud.common.dict.entity.DimDict;

/**
 * 基础字典Mapper类
 */
public interface DimDictMapper {

	/**
	 * 获取基础字典集合类
	 * @param dimDict
	 * @return
	 */
    List<DimDict> findDimDictList(DimDict dimDict);
}