package com.pay.cloud.common.dict.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.common.dict.dao.DimBankCardBaseMapper;
import com.pay.cloud.common.dict.entity.DimBankCardBase;
import com.pay.cloud.common.dict.service.DimBankCardBaseService;

import java.util.List;

/**
 * Created by yacheng.ji on 2017/2/10.
 */
@Service("dimBankCardBaseServiceImpl")
public class DimBankCardBaseServiceImpl implements DimBankCardBaseService {

    @Autowired
    private DimBankCardBaseMapper dimBankCardBaseMapper;

    public List<DimBankCardBase> findDimBankCardBaseList() throws Exception {
        return dimBankCardBaseMapper.findDimBankCardBaseList();
    }
}
