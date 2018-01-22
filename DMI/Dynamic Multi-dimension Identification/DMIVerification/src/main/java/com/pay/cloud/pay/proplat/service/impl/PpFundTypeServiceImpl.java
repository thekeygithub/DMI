package com.pay.cloud.pay.proplat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.dao.PpFundTypeMapper;
import com.pay.cloud.pay.proplat.entity.PpFundType;
import com.pay.cloud.pay.proplat.service.PpFundTypeService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/8/23.
 */
@Service("ppFundTypeServiceImpl")
public class PpFundTypeServiceImpl implements PpFundTypeService {

    @Autowired
    private PpFundTypeMapper ppFundTypeMapper;

    public List<PpFundType> findPpFundTypeList() throws Exception {
        return ppFundTypeMapper.findPpFundTypeList();
    }
}