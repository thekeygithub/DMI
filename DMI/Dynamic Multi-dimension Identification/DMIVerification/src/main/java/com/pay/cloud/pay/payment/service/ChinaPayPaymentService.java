package com.pay.cloud.pay.payment.service;

import java.util.Map;

/**
 * 银联支付接口
 */
public interface ChinaPayPaymentService {

	/**
	 * 消费类交易支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> consumePayment(Map<String,Object> params) throws Exception;
	
	/**
	 * 交易查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> consumePaymentQuery(Map<String,Object> params) throws Exception;
	
    /**
     * 后续类交易
     * @param params
     * @return
     * @throws Exception
     */
	Map<String,Object> consumePaymentRefund(Map<String,Object> params) throws Exception;
	
	/**
	 * 验签并格式化返回串
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getNotifyData(String params) throws Exception;
}
