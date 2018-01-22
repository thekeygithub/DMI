package com.pay.cloud.pay.escrow.abc.service;

import java.util.Map;


/**
 * 农行支付接口
 */
public interface AbcPayService {
	
	/**
	 * 支付相关操作
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> perform(Map<String,Object> receiveMap) throws Exception;

}
