package com.pay.cloud.pay.payment.service;

import java.util.Map;


/**
 * 错误处理统一服务
 */
public interface ErrorCodeService {
	
	/**
	 * 支付API错误码码转换处理
	 * @param result
	 * @return
	 * @throws Exception
	 */
	Map<String,String> getPaymentResultMap(Map<String,String> result) throws Exception;
	
	/**
	 * 商户通用接口错误码转换处理
	 * @param result
	 * @return
	 * @throws Exception
	 */
	Map<String,String> getSupplyResultMap(Map<String,String> result) throws Exception;

}
