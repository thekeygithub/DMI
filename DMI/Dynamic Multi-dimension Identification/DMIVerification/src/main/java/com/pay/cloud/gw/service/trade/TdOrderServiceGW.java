package com.pay.cloud.gw.service.trade;

import java.util.Map;

public interface TdOrderServiceGW {

    /**
     * 保存支付订单信息放入Redis
     * @param inputMap
     * @return
     * @throws Exception
     */
	Map<String, Object> queryTdOrder(Map<String, Object> inputMap) throws Exception;

    /**
     * 支付-预下单
     * @param inputMap
     * @return
     */
    Map<String, Object> createPayOrder(Map<String, Object> inputMap) throws Exception;

    /**
     * 支付-预支付
     * @param inputMap
     * @return
     */
    Map<String, Object> prePaymentOrder(Map<String, Object> inputMap) throws Exception;

    /**
     * 支付-预支付验证
     * @param inputMap
     * @return
     */
    Map<String, Object> prePaymentVerify(Map<String, Object> inputMap) throws Exception;

    /**
     * 确认支付前下发短信验证码
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> sendPayVerifyCode(Map<String, Object> inputMap) throws Exception;

    /**
     * 确认支付
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> confirmPaymentOrder(Map<String, Object> inputMap) throws Exception;

    /**
     * 易宝支付结果通知
     * @param data
     * @param encryptKey
     * @return
     * @throws Exception
     */
    String yeepayPaymentResultNotice(String data, String encryptKey) throws Exception;

    /**
     * 支付宝支付结果通知
     * @param param
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String aliPaymentResultNotice(Map<String, Object> param, String payOrderId) throws Exception;

    /**
     * 微信支付结果通知
     * @param param
     * @param payOrderId
     * @return
     * @throws Exception
     */
    String weChatPaymentResultNotice(Map<String, Object> param, String payOrderId) throws Exception;

    /**
     * 建行支付结果通知
     * @param param
     * @return
     * @throws Exception
     */
    String ccbPaymentResultNotice(Map<String, Object> param) throws Exception;

    /**
     * 银联支付结果通知
     * @param param
     * @return
     * @throws Exception
     */
    String bkuPaymentResultNotice(Map<String, Object> param) throws Exception;

    /**
     * ebao支付平台通知商户订单支付结果
     * @param inputMap
     * @throws Exception
     */
    void noticeMerchantPaymentResult(Map<String, String> inputMap) throws Exception;
    
    /**
     * 确认支付
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> quitOrder(Map<String, Object> inputMap) throws Exception;

    /**
     * 交易单处理
     * @param inputMap
     * @return
     */
    Map<String, Object> createTradeOrderProcess(Map<String, Object> inputMap) throws Exception;

    /**
     * 变更商业支付通道
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> changeFundId(Map<String, Object> inputMap) throws Exception;

    /**
     * 确认支付--纯医保支付
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> confirmPayment(Map<String, Object> inputMap) throws Exception;

    /**
     * 预支付（非yeepay方式）
     * @param inputMap
     * @return
     * @throws Exception
     */
    Map<String, Object> preparePay(Map<String, Object> inputMap) throws Exception;
}
