package com.models.cloud.pay.trade.dao;

import java.util.List;

import com.models.cloud.pay.trade.entity.DimBusiDetType;

public interface DimBusiDetTypeMapper {

    List<DimBusiDetType> findDimBusiDetTypeList() throws Exception;
}