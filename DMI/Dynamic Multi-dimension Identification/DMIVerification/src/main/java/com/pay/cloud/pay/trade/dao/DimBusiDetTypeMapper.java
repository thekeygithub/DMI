package com.pay.cloud.pay.trade.dao;

import java.util.List;

import com.pay.cloud.pay.trade.entity.DimBusiDetType;

public interface DimBusiDetTypeMapper {

    List<DimBusiDetType> findDimBusiDetTypeList() throws Exception;
}