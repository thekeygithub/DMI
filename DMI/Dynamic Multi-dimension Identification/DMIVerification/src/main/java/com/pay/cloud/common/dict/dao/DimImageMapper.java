package com.pay.cloud.common.dict.dao;

import com.pay.cloud.common.dict.entity.DimImageWithBLOBs;

public interface DimImageMapper {


    DimImageWithBLOBs selectByPrimaryKey(String imageId);

}