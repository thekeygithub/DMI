package com.models.cloud.gw.service.trade;

import java.util.Map;

/**
 * 退款服务
 */
public interface TdOrderRefundServiceGW {
	
	/**
	 * 生成退款订单
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> prePayOrderRefund(Map<String, Object> inputMap) throws Exception;
	
	/**
	 * 退款
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> PayOrderRefund(Map<String, Object> inputMap) throws Exception;

	/**
	 * 退款查询
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> payOrderRefundQuery(Map<String, Object> inputMap) throws Exception;
	
	/**
	 * 退款结果查询，入口参数为第三方接口参数
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> payRefundQuery(Map<String, Object> inputMap) throws Exception;
	
	/**
	 * 调用建设银行退款接口
	 * @param @param inputMap
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	Map<String, Object> CCBRefund(Map<String, Object> inputMap, int doType) throws Exception;
	
	/**
	 * 调用银联退款接口
	 * @param @param inputMap
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	Map<String, Object> BKURefund(Map<String, Object> inputMap, int doType) throws Exception;

	/**
	 * 生成混合支付退款订单
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> prePayOrderRefundMix(Map<String, Object> inputMap) throws Exception;

	/**
	 * 社保撤消
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> payOrderRefundSi(Map<String, Object> inputMap) throws Exception;

	/**
	 * 社保撤消结果查询，入口参数为第三方接口参数
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> payRefundQuerySi(Map<String, Object> inputMap) throws Exception;
}
