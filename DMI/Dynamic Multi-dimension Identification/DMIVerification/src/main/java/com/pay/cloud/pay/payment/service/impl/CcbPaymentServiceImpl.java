package com.pay.cloud.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.escrow.ccb.service.NetPayService;
import com.pay.cloud.pay.payment.service.CcbPaymentService;

/**
 * 建行支付统一接口实现类
 */
@Service("ccbPaymentServiceImpl")
public class CcbPaymentServiceImpl implements CcbPaymentService{

	//日志
	private static Logger log = Logger.getLogger(CcbPaymentServiceImpl.class);
	
	//网上支付
	@Resource(name = "netPayServiceImpl")
	private NetPayService netPayService;
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.CcbPaymentService#antiPhishingPayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> antiPhishingPayment(Map<String, Object> params) throws Exception {
		return netPayService.antiPhishingPayment(params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.CcbPaymentService#antiPhishingPaymentVerifySigature(java.util.Map)
	 */
	@Override
	public Map<String, Object> getNotifyData(String params) throws Exception {
		return netPayService.getNotifyData(params);
	}

}
