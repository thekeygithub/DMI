package com.models.cloud.pay.escrow.ccb.service;

import java.util.Map;


/**
 * 网上支付接口
 */
public interface NetPayService {

	
	/**
	 * 反钓鱼支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> antiPhishingPayment(Map<String,Object> params) throws Exception;
	
	/**
	 * 验签并返回回调数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getNotifyData(String params) throws Exception;
	
	
}
