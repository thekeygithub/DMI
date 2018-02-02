package com.models.cloud.pay.trade.service;

public interface PayOrderService {

    /**
     * 支付结果通知时处理交易订单数据
     * @param payOrderId 交易单号
     * @param yeepayFlowId yeepay交易流水号
     * @param status 支付状态 1-成功 0-失败
     * @param closeTime 支付时间 到秒，如：1369973494
     * @param errorCode 错误码 成功为0即可
     * @param errorMsg 错误描述 成功为success即可
     * @param bindId 绑卡ID
     * @return 成功-SUCCESS 失败-FAILURE
     * @throws Exception
     */
    String executeOrderTradeData_EBZF(String payOrderId, String yeepayFlowId, String status, String closeTime, String errorCode, String errorMsg, String bindId) throws Exception;
    String executeOrderTradeData_CCB(String payOrderId, String ccbPayFlowId, String status, String tranDate, String payAmount) throws Exception;
    String executeOrderTradeData_BKU(String payOrderId, String bkuPayFlowId, String status, String transTime, String orderAmt) throws Exception;
    String executeOrderTradeData_ALIPAY(String payOrderId, String alipayFlowId, String status, String closeTime, String totalFee) throws Exception;
    String executeOrderTradeData_WECHAT(String payOrderId, String weChatPayFlowId, String status, String payFinishTime, String totalFee) throws Exception;
}
