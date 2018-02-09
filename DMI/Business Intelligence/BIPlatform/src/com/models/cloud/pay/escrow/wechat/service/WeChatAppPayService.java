package com.models.cloud.pay.escrow.wechat.service;

import java.util.Map;

/**
 * 微信App支付服务
 * Created by yacheng.ji on 2017/2/16.
 */
public interface WeChatAppPayService {

    /**
     * 统一下单
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 下单结果
     * @throws Exception
     */
    Map<String, Object> unifiedOrder(Map<String, Object> params, String fundId) throws Exception;

    /**
     * 调起支付
     * @param prepayId 预支付交易会话ID
     * @param fundId 资金平台ID
     * @return 请求串
     * @throws Exception
     */
    String transferPayment(String prepayId, String fundId) throws Exception;

    /**
     * 查询订单(“微信订单号”和“商户订单号”可二选一)
     * @param transactionId 微信订单号
     * @param outTradeNo 商户订单号
     * @param fundId 资金平台ID
     * @return 订单信息
     * @throws Exception
     */
    Map<String, Object> orderQuery(String transactionId, String outTradeNo, String fundId) throws Exception;

    /**
     * 关闭订单
     * @param outTradeNo 商户订单号[订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟]
     * @param fundId 资金平台ID
     * @return 订单关闭结果
     * @throws Exception
     */
    Map<String, Object> closeOrder(String outTradeNo, String fundId) throws Exception;

    /**
     * 申请退款
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 申请结果
     * @throws Exception
     */
    Map<String, Object> refundApply(Map<String, String> params, String fundId) throws Exception;

    /**
     * 查询退款
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 退款结果
     * @throws Exception
     */
    Map<String, Object> refundQuery(Map<String, String> params, String fundId) throws Exception;
}
