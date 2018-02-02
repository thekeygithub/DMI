package com.models.cloud.common.dict.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.common.dict.dao.DimImageMapper;
import com.models.cloud.common.dict.entity.DimImageWithBLOBs;
import com.models.cloud.common.dict.service.DimImageService;

/**
 * 图片操作
 * @author qingsong.li
 *
 */
@Service("dimImageServiceImpl")
public class DimImageServiceImpl implements DimImageService{

	@Autowired
	private DimImageMapper dimImageMapper;
	
	/**
	 * 通过图片id查询图片信息
	 */
	@Override
	public DimImageWithBLOBs selectDimImage(String id) {
		return dimImageMapper.selectByPrimaryKey(id);
	}

}
