package com.models.cloud.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.escrow.yeepay.service.InstantPayService;
import com.models.cloud.pay.escrow.yeepay.service.TransferPayService;
import com.models.cloud.pay.payment.service.PaymentService;

@Service("yeepayPaymentServiceImpl")
public class YeepayPaymentServiceImpl implements PaymentService{
	
	//日志
	private static Logger log = Logger.getLogger(YeepayPaymentServiceImpl.class);
	
	//一键支付
	@Resource(name = "instantPayServiceImpl")
	private InstantPayService instantPayService;
	
	//代发代付
	@Resource(name = "transferPayServiceImpl")
	private TransferPayService transferPayService;
	
	@Override
	public Map<String, String> creditCardRequest(Map<String, String> params) throws Exception {
		return instantPayService.creditRequest(params);
	}

	@Override
	public Map<String, String> debitCardRequest(Map<String, String> params) throws Exception {		
		return instantPayService.debitRequest(params);
	}

	@Override
	public Map<String, String> bankCardBindQuery(String payUserId,String payUserTypeId) throws Exception {
		return instantPayService.bankcardBindQuery(payUserId, payUserTypeId);
	}

	@Override
	public Map<String, String> bindBankCardRequest(Map<String, String> params) throws Exception {
		return instantPayService.bindidRequest(params);
	}

	@Override
	public Map<String, String> sendShortMessage(String tdId) throws Exception {
		return instantPayService.sendMessage(tdId);
	}

	@Override
	public Map<String, String> paymentConfirm(Map<String, String> params) throws Exception {
		return instantPayService.paymentConfirm(params);
	}

	@Override
	public Map<String, String> payapiQueryByTdid(String tdId) throws Exception {
		return instantPayService.payapiQueryByOrderid(tdId);
	}

	@Override
	public Map<String, String> singleQuery(String tdId, String serialNo) throws Exception {
		return instantPayService.singleQuery(tdId, serialNo);
	}

	@Override
	public Map<String, String> refund(Map<String, String> params) throws Exception {
		return instantPayService.refund(params);
	}

	@Override
	public Map<String, String> refundQuery(String tdId, String serialNo) throws Exception {
		return instantPayService.refundQuery(tdId, serialNo);
	}

	@Override
	public Map<String, String> bankCardCheck(String cardno) throws Exception {
		return instantPayService.bankCardCheck(cardno);
	}

	@Override
	public Map<String, String> unbindBankcard(Map<String, String> params) throws Exception {
		return instantPayService.unbindBankcard(params);
	}
	
	@Override
	public Map<String, String> decryptCallbackData(String data,String encryptkey) throws Exception {
		return instantPayService.decryptCallbackData(data, encryptkey);
	}

	//*************** 代发代付接口 ***************//
	
	@Override
	public Map<String, Object> transferPayBatch(Map<String, Object> params) throws Exception {
		return transferPayService.transferPayBatch(params);
	}

	@Override
	public Map<String, Object> transferSingle(Map<String, String> params) throws Exception {
		return transferPayService.transferSingle(params);
	}

	@Override
	public Map<String, Object> accountBalanaceQuery(Map<String, String> params) throws Exception {
		return transferPayService.accountBalanaceQuery(params);
	}

	@Override
	public Map<String, Object> batchDetailQuery(Map<String, String> params) throws Exception {
		return transferPayService.batchDetailQuery(params);
	}

	@Override
	public Map<String, Object> transferNotify(String notifyXml) throws Exception {
		return transferPayService.transferNotify(notifyXml);
	}

	@Override
	public String transferNotifyBack(Map<String, String> params) throws Exception {
		return transferPayService.transferNotifyBack(params);
	}

	

}
