package com.models.cloud.pay.payment.service;

import java.util.Map;


/**
 * 农行支付统一接口
 */
public interface AbcPaymentService {
	
	/**
	 * 支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> merPayment(Map<String,Object> params) throws Exception;
	
	/**
	 * 交易订单查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> merQueryOrder(Map<String,Object> params) throws Exception;
	
	/**
	 * 交易流水查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> merQueryRecord(Map<String,Object> params) throws Exception;
	
	/**
	 *单笔退款
	 */
	Map<String,Object> merRefund(Map<String,Object> params) throws Exception;
	
	/**
	 * 支付结果处理
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> merResult(Map<String,Object> params) throws Exception;
	
	/**
	 * 对账单查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> merSettleQuery(Map<String,Object> params) throws Exception;

}
