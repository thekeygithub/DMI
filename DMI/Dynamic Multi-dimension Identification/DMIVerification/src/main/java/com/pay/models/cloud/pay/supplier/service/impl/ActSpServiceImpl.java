package com.models.cloud.pay.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.supplier.dao.ActSpMapper;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.ActSpService;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
@Service("actSpServiceImpl")
public class ActSpServiceImpl implements ActSpService {

    @Autowired
    private ActSpMapper actSpMapper;

    public ActSp selectByActId(Long actId) throws Exception {
        return actSpMapper.selectByActId(actId);
    }
    public ActSp findByChannelAppId(String channelAppId) throws Exception {
        return actSpMapper.findByChannelAppId(channelAppId);
    }
    public ActSp findByWorkSpActId(Long workSpActId) throws Exception {
        return actSpMapper.findByWorkSpActId(workSpActId);
    }
}
