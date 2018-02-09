package com.models.cloud.pay.payment.service;

import java.util.Map;

/**
 * 易保支付接口
 */
public interface PaymentService {
	
	/**
	 * 信用卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> creditCardRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 储蓄卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> debitCardRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 查询绑卡信息列表
	 * @param payUserId 终端用户ID
	 * @param payUserTypeId 终端用户身份类型
	 * @return
	 * @throws Exception
	 */
	Map<String, String> bankCardBindQuery(String payUserId, String payUserTypeId) throws Exception;
	
	/**
	 * 绑卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> bindBankCardRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 发送短信验证码
	 * @param tdId 交易单ID
	 * @return
	 * @throws Exception
	 */
	Map<String, String> sendShortMessage(String tdId) throws Exception;
	
    /**
     * 确认支付
     * @param params
     * @return
     * @throws Exception
     */
	Map<String, String> paymentConfirm(Map<String, String> params) throws Exception;
	
	/**
	 * 5.7 支付结果查询
	 * @param tdId
	 * @return
	 * @throws Exception
	 */
	Map<String, String> payapiQueryByTdid(String tdId) throws Exception;
	
    /**
     * 返回支付回调参数
     * @param data
     * @param encryptkey
     * @return
     * @throws Exception
     */
	Map<String, String> decryptCallbackData(String data, String encryptkey) throws Exception;
	
	/**
	 * 单笔交易记录查询
	 * @param tdId
	 * @param serialNo 第三方支付流水号
	 * @return
	 * @throws Exception
	 */
	Map<String, String> singleQuery(String tdId, String serialNo) throws Exception;

//		/**
//		 * getPathOfPayClearData() 
//		 *
//		 * 参数说明：
//		 *
//		 * merchantaccount		- 商户编号
//		 * merchantPrivateKey	- 商户私钥
//		 * merchantAESKey		- 商户随机生成的AESKey
//		 * yeepayPublicKey		- 易宝公玥
//		 *
//		 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
//		 *
//		 * merchantaccount		- string	- 必填		- 商户编号
//		 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
//		 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
//		 * sign                 - string	- 必填		- 签名信息
//		 *
//		 * 返回说明：
//		 *
//		 * filePath				- 批量查询结果文件的路径
//		 * error_code			- 错误返回码
//		 * error				- 错误信息
//		 * customError			- 自定义，非接口返回
//		 * @param startdate
//		 * @param enddate
//		 * @param sysPath
//		 * @return
//		 * @throws Exception
//		 */
//		Map<String, String> getPathOfPayClearData(String startdate, String enddate, String sysPath) throws Exception;
	
	/**
	 * 单笔退款方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> refund(Map<String, String> params) throws Exception;
	
    /**
     * 退款查询
     * @param tdId
     * @param serialNo
     * @return
     * @throws Exception
     */
	Map<String, String> refundQuery(String tdId, String serialNo) throws Exception;
	

//		/**
//		 * getPathOfRefundClearData() 
//		 *
//		 * 参数说明：
//		 *
//		 * merchantaccount		- 商户编号
//		 * merchantPrivateKey	- 商户私钥
//		 * merchantAESKey		- 商户随机生成的AESKey
//		 * yeepayPublicKey		- 易宝公玥
//		 *
//		 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
//		 *
//		 * merchantaccount		- string	- 必填		- 商户编号
//		 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
//		 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
//		 * sign                 - string	- 必填		- 签名信息
//		 *
//		 * 返回说明：
//		 *
//		 * filePath				- 批量查询结果文件的路径
//		 * error_code			- 错误返回码
//		 * error				- 错误信息
//		 * customError			- 自定义，非接口返回
//		 *
//		 */
//		Map<String, String> getPathOfRefundClearData(String startdate, String enddate, String sysPath) throws Exception;
	
	/**
	 * 银行卡查询方法
	 * @param cardno 银行卡号
	 * @return
	 * @throws Exception
	 */
	Map<String, String> bankCardCheck(String cardno) throws Exception;

    /**
     * 银行卡解绑方法
     * @param params
     * @return
     * @throws Exception
     */
	Map<String, String> unbindBankcard(Map<String, String> params) throws Exception;
	
	
	
	
	//********************** 代发代付接口 **************//
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
	 * 主动通知参数处理
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
