package com.pay.cloud.pay.trade.dao;

import com.pay.cloud.pay.trade.entity.TdOrdExtWithdr;

public interface TdOrdExtWithdrMapper {

    int deleteByPrimaryKey(Long tdId);

    int insert(TdOrdExtWithdr record);

    TdOrdExtWithdr selectByPrimaryKey(Long tdId);

    int updateByPrimaryKeySelective(TdOrdExtWithdr record);

    int updateWithdrUpdDate(Long tdId) throws Exception;
}