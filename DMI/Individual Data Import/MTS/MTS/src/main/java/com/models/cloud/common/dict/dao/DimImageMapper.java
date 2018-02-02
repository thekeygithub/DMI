package com.models.cloud.common.dict.dao;

import com.models.cloud.common.dict.entity.DimImageWithBLOBs;

public interface DimImageMapper {


    DimImageWithBLOBs selectByPrimaryKey(String imageId);

}