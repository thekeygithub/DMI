package com.models.cloud.pay.escrow.yeepay.service;

import java.util.Map;

/**
 * 一键支付接口
 */
public interface InstantPayService {
	
	/**
	 * 5.1 信用卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> creditRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 5.2 储蓄卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> debitRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 5.3 查询绑卡信息列表
	 * @param identityid
	 * @param identitytype
	 * @return
	 * @throws Exception
	 */
	Map<String, String> bankcardBindQuery(String identityid, String identitytype) throws Exception;
	
	/**
	 * 5.4 绑卡支付请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> bindidRequest(Map<String, String> params) throws Exception;
	
	/**
	 * 5.5 发送短信验证码
	 * @param orderid
	 * @return
	 * @throws Exception
	 */
	Map<String, String> sendMessage(String orderid) throws Exception;
	
    /**
     * 5.6 确认支付
     * @param params
     * @return
     * @throws Exception
     */
	Map<String, String> paymentConfirm(Map<String, String> params) throws Exception;
	
	/**
	 * 5.7 支付结果查询
	 * @param orderid
	 * @return
	 * @throws Exception
	 */
	Map<String, String> payapiQueryByOrderid(String orderid) throws Exception;
	
    /**
     * 返回支付回调参数
     * @param data
     * @param encryptkey
     * @return
     * @throws Exception
     */
	Map<String, String> decryptCallbackData(String data, String encryptkey) throws Exception;
	
	/**
	 * 单笔查询
	 * @param orderid
	 * @param yborderid
	 * @return
	 * @throws Exception
	 */
	Map<String, String> singleQuery(String orderid, String yborderid) throws Exception;

	/**
	 * getPathOfPayClearData() 
	 *
	 * 参数说明：
	 *
	 * merchantaccount		- 商户编号
	 * merchantPrivateKey	- 商户私钥
	 * merchantAESKey		- 商户随机生成的AESKey
	 * yeepayPublicKey		- 易宝公玥
	 *
	 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
	 *
	 * merchantaccount		- string	- 必填		- 商户编号
	 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
	 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
	 * sign                 - string	- 必填		- 签名信息
	 *
	 * 返回说明：
	 *
	 * filePath				- 批量查询结果文件的路径
	 * error_code			- 错误返回码
	 * error				- 错误信息
	 * customError			- 自定义，非接口返回
	 * @param startdate
	 * @param enddate
	 * @param sysPath
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getPayClearData(String startdate, String enddate) throws Exception;
	
	/**
	 * 单笔退款方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, String> refund(Map<String, String> params) throws Exception;
	
    /**
     * 退款查询
     * @param orderid
     * @param yborderid
     * @return
     * @throws Exception
     */
	Map<String, String> refundQuery(String orderid, String yborderid) throws Exception;
	

	/**
	 * getPathOfRefundClearData() 
	 *
	 * 参数说明：
	 *
	 * merchantaccount		- 商户编号
	 * merchantPrivateKey	- 商户私钥
	 * merchantAESKey		- 商户随机生成的AESKey
	 * yeepayPublicKey		- 易宝公玥
	 *
	 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
	 *
	 * merchantaccount		- string	- 必填		- 商户编号
	 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
	 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
	 * sign                 - string	- 必填		- 签名信息
	 *
	 * 返回说明：
	 *
	 * filePath				- 批量查询结果文件的路径
	 * error_code			- 错误返回码
	 * error				- 错误信息
	 * customError			- 自定义，非接口返回
	 *
	 */
	Map<String, String> getRefundClearData(String startdate, String enddate) throws Exception;
	
	/**
	 * 银行卡查询方法
	 * @param cardno
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
}
