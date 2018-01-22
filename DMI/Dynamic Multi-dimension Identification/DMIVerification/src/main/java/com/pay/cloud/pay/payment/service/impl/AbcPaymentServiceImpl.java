package com.pay.cloud.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.escrow.abc.service.AbcPayService;
import com.pay.cloud.pay.payment.service.AbcPaymentService;

/**
 * 农行行支付统一接口实现类
 */
@Service("abcPaymentServiceImpl")
public class AbcPaymentServiceImpl implements AbcPaymentService {

	//日志
	private static Logger log = Logger.getLogger(AbcPaymentServiceImpl.class);
		
	//网上支付
	@Resource(name = "abcMerPayServiceImpl")
	private AbcPayService abcMerPayService;
	
	//交易订单查询
	@Resource(name = "abcMerQueryOrderServiceImpl")
	private AbcPayService abcMerQueryOrderService;
	
	//交易流水查询
	@Resource(name = "abcMerQueryRecordServiceImpl")
	private AbcPayService abcMerQueryRecordService;
	
	//退款
	@Resource(name = "abcMerRefundServiceImpl")
	private AbcPayService abcMerRefundService;
	
	//支付结果接收处理
	@Resource(name = "abcMerResultServiceImpl")
	private AbcPayService abcMerResultService;
	
	//对账单查询
	@Resource(name = "abcMerSettleQueryServiceImpl")
	private AbcPayService abcMerSettleQueryService;
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merPayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> merPayment(Map<String, Object> params) throws Exception {
		return abcMerPayService.perform(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merQueryOrder(java.util.Map)
	 */
	@Override
	public Map<String, Object> merQueryOrder(Map<String, Object> params) throws Exception {
		return abcMerQueryOrderService.perform(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merQueryRecord(java.util.Map)
	 */
	@Override
	public Map<String, Object> merQueryRecord(Map<String, Object> params) throws Exception {
		return abcMerQueryRecordService.perform(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merRefund(java.util.Map)
	 */
	@Override
	public Map<String, Object> merRefund(Map<String, Object> params) throws Exception {
		return abcMerRefundService.perform(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merResult(java.util.Map)
	 */
	@Override
	public Map<String, Object> merResult(Map<String, Object> params) throws Exception {
		return abcMerResultService.perform(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.AbcPaymentService#merSettleQuery(java.util.Map)
	 */
	@Override
	public Map<String, Object> merSettleQuery(Map<String, Object> params) throws Exception {
		return abcMerSettleQueryService.perform(params);
	}

}
