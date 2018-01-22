package com.pay.cloud.gw.service.trade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.service.trade.TdOrdExtWithdrService;
import com.pay.cloud.pay.trade.dao.TdOrdExtWithdrMapper;
import com.pay.cloud.pay.trade.entity.TdOrdExtWithdr;

/**
 * Created by yacheng.ji on 2016/12/16.
 */
@Service("tdOrdExtWithdrServiceImpl")
public class TdOrdExtWithdrServiceImpl implements TdOrdExtWithdrService {

    @Autowired
    private TdOrdExtWithdrMapper tdOrdExtWithdrMapper;

    public TdOrdExtWithdr findTdOrdExtWithdr(Long tdId) throws Exception {
        return tdOrdExtWithdrMapper.selectByPrimaryKey(tdId);
    }

    public int updateWithdrUpdDate(Long tdId) throws Exception {
        return tdOrdExtWithdrMapper.updateWithdrUpdDate(tdId);
    }
}
