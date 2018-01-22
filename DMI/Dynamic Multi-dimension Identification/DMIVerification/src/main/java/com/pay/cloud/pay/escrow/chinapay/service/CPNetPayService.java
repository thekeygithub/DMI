package com.pay.cloud.pay.escrow.chinapay.service;

import java.util.Map;

/**
 * 网银支付接口
 */
public interface CPNetPayService {

	/**
	 * 消费类交易支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> consumePayment(Map<String,Object> params) throws Exception;
	
	/**
	 * 消费类分账交易支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> consumeSplitPayment(Map<String,Object> params) throws Exception;
	
	/**
	 * 消费类交易支付查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> consumePaymentQuery(Map<String,Object> params) throws Exception;
	
    /**
     * 后续类交易-退款
     * @param params
     * @return
     * @throws Exception
     */
	Map<String,Object> consumePaymentRefund(Map<String,Object> params) throws Exception;
	
	/**
	 * 后续类分账交易-退款
	 */
	Map<String,Object> consumeSplitPaymentRefund(Map<String,Object> params) throws Exception;
	
	/**
	 * 验签并格式化返回串
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getNotifyData(String params) throws Exception;
	
	
	/**
	 * 验签并格式化数组，同时解析汉字
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getNotifyData(Map<String,Object> params) throws Exception;
}
