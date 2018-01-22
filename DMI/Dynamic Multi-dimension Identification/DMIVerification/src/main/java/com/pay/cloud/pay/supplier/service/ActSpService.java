package com.pay.cloud.pay.supplier.service;

import com.pay.cloud.pay.supplier.entity.ActSp;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
public interface ActSpService {

    ActSp selectByActId(Long actId) throws Exception;

    ActSp findByChannelAppId(String channelAppId) throws Exception;

    ActSp findByWorkSpActId(Long workSpActId) throws Exception;
}
