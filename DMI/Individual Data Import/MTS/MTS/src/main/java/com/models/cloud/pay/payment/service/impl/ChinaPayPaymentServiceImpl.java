package com.models.cloud.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.models.cloud.pay.payment.service.ChinaPayPaymentService;

/**
 * 银联支付接口实现
 */
@Service("chinaPayPaymentServiceImpl")
public class ChinaPayPaymentServiceImpl implements ChinaPayPaymentService {

	//日志
	private static Logger log = Logger.getLogger(ChinaPayPaymentServiceImpl.class);
	
	//网上支付
	@Resource(name = "cPNetPayServiceImpl")
	private CPNetPayService cPNetPayService;
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.ChinaPayPaymentService#consumePayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePayment(Map<String, Object> params) throws Exception {
		return cPNetPayService.consumePayment(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.ChinaPayPaymentService#consumePaymentQuery(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePaymentQuery(Map<String, Object> params) throws Exception {
		return cPNetPayService.consumePaymentQuery(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.ChinaPayPaymentService#consumePaymentRefund(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePaymentRefund(Map<String, Object> params) throws Exception {
		return cPNetPayService.consumePaymentRefund(params);
	}

	@Override
	public Map<String, Object> getNotifyData(String params) throws Exception {
		return cPNetPayService.getNotifyData(params);
	}
	
	

}
