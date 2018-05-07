package com.models.cloud.pay.trade.service;

import java.util.List;
import java.util.Map;

import com.models.cloud.pay.trade.entity.TdMiPara;
import com.models.cloud.pay.trade.entity.TdOrdExtPay;
import com.models.cloud.pay.trade.entity.TdOrder;

public interface TdOrderService {

    /**
     * 获取商户订单记录条数
     * @param record
     * @return
     * @throws Exception
     */
    List<Long> findTdOrderCountByMerOrderId(TdOrder record) throws Exception;

    /**
     * 保存交易单信息
     * @param tdOrder
     * @throws Exception
     */
    void saveTdOrderInfo(TdOrder tdOrder) throws Exception;

    /**
     * 保存交易单&支付扩展信息
     * @param tdOrder
     * @throws Exception
     */
    void saveTdOrderAndExtPayInfo(TdOrder tdOrder, TdOrdExtPay tdOrdExtPay, TdMiPara tdMiPara) throws Exception;

    /**
     * 更新交易单&支付扩展信息
     * @param tdOrder
     * @throws Exception
     */
    void updateTdOrderAndExtPayInfo(TdOrder tdOrder, TdOrdExtPay tdOrdExtPay) throws Exception;

    /**
     * 更新交易单信息
     * @param tdOrder
     * @return
     * @throws Exception
     */
    int updateTdOrderInfo(TdOrder tdOrder) throws Exception;

    /**
     * 保存交易扩展表-支付信息
     * @param tdOrdExtPay
     * @throws Exception
     */
    void saveTdOrdExtPayInfo(TdOrdExtPay tdOrdExtPay) throws Exception;

    /**
     * 获取交易单信息
     * @param tdId
     * @return
     * @throws Exception
     */
    TdOrder findTdOrderByPayOrderId(Long tdId) throws Exception;

    /**
     * 获取交易扩展表信息
     * @param payOrderId
     * @return
     * @throws Exception
     */
    TdOrdExtPay findTdOrdExtPayByPayOrderId(Long payOrderId) throws Exception;

    /**
     * 更新交易扩展表信息
     * @param tdOrdExtPay
     * @return
     * @throws Exception
     */
    int updateTdOrdExtPayInfo(TdOrdExtPay tdOrdExtPay) throws Exception;

    /**
     * 处理易宝支付结果通知
     * @param inputMap
     * @return
     * @throws Exception
     */
    String executeYeepayPaymentResultNotice(Map<String, String> inputMap) throws Exception;

    /**
     * 处理支付宝支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String executeAliPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception;

    /**
     * 处理微信支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String executeWeChatPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception;

    /**
     * 处理建行支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String executeCcbPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception;

    /**
     * 处理银联支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String executeBkuPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception;

    /**
     * 根据不同的条件查询list
     * @Description
     * @encoding UTF-8 
     * @author haiyan.zhang
     * @date 2016年8月16日 
     * @time 下午2:25:28
     * @version V1.0
     * @param @param tdOrder
     * @param @return
     * @param @throws Exception
     *
     */
    List<TdOrder> findTdOrderListByOrigOrdCode(String merOrderId) throws Exception;

    int saveTdMiPara(TdMiPara tdMiPara) throws Exception;

    TdMiPara queryTdMiPara(Long tdId) throws Exception;

    int updateTdMiPara(TdMiPara tdMiPara) throws Exception;

    Map<String, Object> getPreClmPara(TdMiPara tdMiPara) throws Exception;

    /**
     * 变更商业支付通道
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> changeFundId(Map<String, Object> inputMap) throws Exception;

    /**
     * 检测交易单是否有效
     * @param tdOrder
     * @param accountId
     * @return
     * @throws Exception
     */
    Map<String, Object> checkOrderIsValid(TdOrder tdOrder, String accountId) throws Exception;
}
