package com.models.cloud.common.dict.service;

import com.models.cloud.common.dict.entity.DimImageWithBLOBs;

/**
 * 读取图片
 * @author qingsong.li
 *
 */
public interface DimImageService {

	public DimImageWithBLOBs selectDimImage(String id);
	
}
