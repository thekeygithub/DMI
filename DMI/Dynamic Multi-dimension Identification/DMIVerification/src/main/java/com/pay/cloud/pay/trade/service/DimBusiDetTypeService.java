package com.pay.cloud.pay.trade.service;

import java.util.List;

import com.pay.cloud.pay.trade.entity.DimBusiDetType;

/**
 * Created by yacheng.ji on 2016/8/16.
 */
public interface DimBusiDetTypeService {

    List<DimBusiDetType> findDimBusiDetTypeList() throws Exception;
}
