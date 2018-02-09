package com.models.cloud.pay.trade.dao;

import java.util.List;
import java.util.Map;

import com.models.cloud.pay.trade.entity.TdOrder;

public interface TdOrderMapper {

    int saveTdOrder(TdOrder record) throws Exception;

    TdOrder findTdOrderByPayOrderId(Long tdId) throws Exception;

    List<Long> findTdOrderCountByMerOrderId(TdOrder record) throws Exception;
    
    List<TdOrder> findTdOrderList(TdOrder record) throws Exception;
    
    int updateTdOrder(TdOrder record) throws Exception;
    
    TdOrder findTdOrderByTdId(Long tdId) throws Exception;
    
    List<TdOrder> findTdOrderListByCondition(TdOrder tdOrder)throws Exception;
    
    void replaceSeqYeepayBatchNo()throws Exception;
    
    String selectLastInsertBatchNo()throws Exception;
    
    /**
     * 查询订单号数量
     * @param map 渠道AppId 订单号
     * @return
     * @throws Exception
     */
    List<Long> findTdOrderCountListByMerOrderId(Map<String, Object> map) throws Exception;

	List<TdOrder> findTdOrderListByOrigOrdCode(String merOrderId) throws Exception;
}