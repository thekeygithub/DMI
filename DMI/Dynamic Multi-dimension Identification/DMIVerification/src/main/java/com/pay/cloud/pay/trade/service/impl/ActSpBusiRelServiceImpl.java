package com.pay.cloud.pay.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.trade.dao.ActSpBusiRelMapper;
import com.pay.cloud.pay.trade.entity.ActSpBusiRel;
import com.pay.cloud.pay.trade.service.ActSpBusiRelService;

/**
 * Created by yacheng.ji on 2016/8/16.
 */
@Service("actSpBusiRelServiceImpl")
public class ActSpBusiRelServiceImpl implements ActSpBusiRelService {

    @Autowired
    private ActSpBusiRelMapper actSpBusiRelMapper;

    public ActSpBusiRel findActSpBusiRel(ActSpBusiRel actSpBusiRel) throws Exception {
        return actSpBusiRelMapper.findActSpBusiRel(actSpBusiRel);
    }
}
