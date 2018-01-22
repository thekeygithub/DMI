package com.pay.cloud.pay.trade.service;

import java.util.List;
import java.util.Map;

import com.pay.cloud.pay.trade.entity.TdOrdExtRet;

public interface TdOrderRefundService {

    
    /**
     * 保存交易扩展表-退款信息
     * @param tdOrdExtRet
     * @throws Exception
     */
    void saveTdOrdExtRetInfo(TdOrdExtRet tdOrdExtRet) throws Exception;

    /**
     * 获取成功状态的交易订单退款附加信息
     * @param params 商户APPID + 商户订单号
     * @return
     * @throws Exception
     */
    TdOrdExtRet findTdOrdExtRetByMerOrderId(Map<String,Object> params) throws Exception;
    /**
     * 根据原交易单获取申请中的交易订单退款附加信息
     * @param retTdId
     * @return
     * @throws Exception
     */
    List<TdOrdExtRet> findTdOrdExtRetByRetTdId(Long retTdId) throws Exception;
//    
    /**
     * 根据交易单主键获取交易订单退款附加信息
     * @param tdId
     * @return
     * @throws Exception
     */
    TdOrdExtRet findTdOrdExtRetByTdId(Long tdId) throws Exception;
    
    /**
     * 获取交易单中商户订单数目
     * @param params
     * @return
     * @throws Exception
     */
    Long findTdOrdCountByMerOrderId(Map<String,Object> params) throws Exception;

    List<TdOrdExtRet> selectByRetList(Long tdId) throws Exception;
}
