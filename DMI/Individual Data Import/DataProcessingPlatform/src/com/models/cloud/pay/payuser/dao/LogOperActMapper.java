package com.models.cloud.pay.payuser.dao;

import com.models.cloud.pay.payuser.entity.LogOperAct;

public interface LogOperActMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(LogOperAct record);

    int insertSelective(LogOperAct record);

    LogOperAct selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(LogOperAct record);

    int updateByPrimaryKey(LogOperAct record);
}