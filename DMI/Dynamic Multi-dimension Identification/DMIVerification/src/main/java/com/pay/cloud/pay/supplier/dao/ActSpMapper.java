package com.pay.cloud.pay.supplier.dao;

import java.util.List;

import com.pay.cloud.pay.supplier.entity.ActSp;

public interface ActSpMapper {
    int deleteByPrimaryKey(Long actId);

    int insert(ActSp record);

    ActSp selectByActId(Long actId);

    int updatStateByActId(ActSp record);
    
    List<ActSp> selectByChanActId(Long chanActId);

    ActSp findByChannelAppId(String channelAppId) throws Exception;

    ActSp findByWorkSpActId(Long workSpActId) throws Exception;
}