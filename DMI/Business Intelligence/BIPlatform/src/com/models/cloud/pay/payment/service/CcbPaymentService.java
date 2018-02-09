package com.models.cloud.pay.payment.service;

import java.util.Map;

/**
 * 建行支付统一接口
 */
public interface CcbPaymentService {

	
	/**
	 * 反钓鱼支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> antiPhishingPayment(Map<String,Object> params) throws Exception;
	
	
	/**
	 * 验签并格式化返回串
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getNotifyData(String params) throws Exception;
}
