package com.pay.cloud.common.dict.service;

import com.pay.cloud.common.dict.entity.DimImageWithBLOBs;

/**
 * 读取图片
 * @author qingsong.li
 *
 */
public interface DimImageService {

	public DimImageWithBLOBs selectDimImage(String id);
	
}
