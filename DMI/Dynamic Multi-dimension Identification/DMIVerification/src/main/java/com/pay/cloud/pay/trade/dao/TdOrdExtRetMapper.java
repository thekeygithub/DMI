package com.pay.cloud.pay.trade.dao;

import java.util.List;
import java.util.Map;

import com.pay.cloud.pay.trade.entity.TdOrdExtRet;

public interface TdOrdExtRetMapper {

    int deleteByPrimaryKey(Long tdId);

    int insert(TdOrdExtRet record);

    int insertSelective(TdOrdExtRet record);

    TdOrdExtRet selectByPrimaryKey(Long tdId);

    int updateByPrimaryKeySelective(TdOrdExtRet record);

    int updateByPrimaryKey(TdOrdExtRet record);

    List<TdOrdExtRet> selectByRetList(Long tdId) throws Exception;
    
    /**
     * 根据订单号获取交易单退款扩展表
     * @param map
     * @return
     * @throws Exception
     */
    TdOrdExtRet selectByOrderCode(Map<String, Object> map) throws Exception;
    /**
     * 根据原有定单号获取是否存在正在进行中退款
     * @param retTdId
     * @return
     * @throws Exception
     */
    List<TdOrdExtRet> selectByRetTdId(Long retTdId) throws Exception;
}