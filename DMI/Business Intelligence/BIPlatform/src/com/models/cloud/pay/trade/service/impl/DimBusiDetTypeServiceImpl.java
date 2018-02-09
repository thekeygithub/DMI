package com.models.cloud.pay.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.trade.dao.DimBusiDetTypeMapper;
import com.models.cloud.pay.trade.entity.DimBusiDetType;
import com.models.cloud.pay.trade.service.DimBusiDetTypeService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/8/16.
 */
@Service("dimBusiDetTypeServiceImpl")
public class DimBusiDetTypeServiceImpl implements DimBusiDetTypeService {

    @Autowired
    private DimBusiDetTypeMapper dimBusiDetTypeMapper;

    public List<DimBusiDetType> findDimBusiDetTypeList() throws Exception {
        return dimBusiDetTypeMapper.findDimBusiDetTypeList();
    }
}
