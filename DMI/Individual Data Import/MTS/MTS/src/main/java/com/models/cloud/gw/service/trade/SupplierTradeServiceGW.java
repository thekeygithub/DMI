package com.models.cloud.gw.service.trade;

import java.util.Map;
/**
 * 
 * @Description: 商户交易服务
 * @ClassName: SupplierTradeServiceGW 
 * @author: danni.liao
 * @date: 2016年4月26日 下午5:27:55
 */
public interface SupplierTradeServiceGW {
	
	/**
	 * 
	 * @Description: 商户提现
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> withdrawForSupplier(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 提现主动通知接口,供资金平台使用
	 * @Title: WithdrawActiveNotify 
	 * @param batchNo
	 * @param tdId
	 * @param status
	 * @param message
	 * @throws Exception void
	 */
	public void WithdrawActiveNotify(String batchNo, String tdId, String status, String message)  throws Exception; 
	
	/**
	 * 
	 * @Description: 查询交易单
	 * @Title: searchTdOrdWithdr 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> searchTdOrdWithdr(Map<String,Object> receiveMap); 
	
	/**
	 * 
	 * @Description: 处理异常订单
	 * @Title: handleExceptionOrder 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> handleExceptionOrder(Map<String,Object> receiveMap);
	/**
	 * 
	 * @Description: 商户账户余额查询接口
	 * @Title: handleExceptionOrder 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> accountBalanaceQuery(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 商户账户余额查询接口
	 * @Title: handleExceptionOrder 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> accountPayClearData(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 获取退款清算对账记录
	 * @Title: handleExceptionOrder 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> refundClearData(Map<String,Object> receiveMap);
	 
	 
}
