package com.models.cloud.pay.escrow.yeepay.service;

import java.util.Map;


/**
 * 代发代付接口
 */
public interface TransferPayService {
	
	/**
	 * 批量打款接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> transferPayBatch(Map<String, Object> params) throws Exception;

	/**
	 * 单笔打款接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> transferSingle(Map<String,String> params) throws Exception;
	
	/**
	 * 商户账户余额查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> accountBalanaceQuery(Map<String,String> params) throws Exception;
	
	/**
	 * 打款批次明细查询接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> batchDetailQuery(Map<String,String> params) throws Exception;
	
	/**
	 * 易宝主动通知参数处理
	 * @param notifyXml
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> transferNotify (String notifyXml) throws Exception;
	
	/**
	 * 回写主动通知参数处理
	 * @param params
	 * @return
	 * @throws Excpetion
	 */
	public String transferNotifyBack(Map<String,String> params) throws Exception;
	
	

}
