package com.models.cloud.pay.trade.dao;

import java.util.List;

import com.models.cloud.pay.trade.entity.TdActRec;

public interface TdActRecMapper {

    int saveTdActRecInfo(TdActRec record) throws Exception;

    List<TdActRec> findTdActRecInfo(TdActRec record) throws Exception;
}