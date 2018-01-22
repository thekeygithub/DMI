package com.pay.cloud.pay.escrow.ccb.utils;

/**
 * 建设银行基本参数
 */
public class CcbConstants {
	
	
	/**
	 * 资金托管平台
	 */
	public static final String FUND_ID = "CCB";
	
	/** 资金平台参数 Begin*/
	
	/** 网上支付Url */
	public static final String NETPAY_URL = "NETPAY_URL";
	/** 商户代码 */
	public static final String MERCHANTID = "MERCHANTID";
	/** 操作员号 */
	public static final String USER_ID = "USER_ID";
	/** 密码  */
	public static final String PASSWORD = "PASSWORD";
	/** 授权号 */
	public static final String AUTHID = "AUTHID";
	/** 商户所属分行号  */
	public static final String BRANCHID = "BRANCHID";
//	/** 交易货币代码 */
//	public static final String CURCODE = "CURCODE";
	/** 网银柜台编号 */
	public static final String POSID = "POSID";
	/** 公钥*/
	public static final String PUB = "PUB";
	/** 语言 */
	public static final String LANGUAGE = "LANGUAGE";
	/** 客户号 */
	public static final String CUST_ID = "CUST_ID";
	/** 商户外联平台 前置机 URL*/
	public static final String MERCHANT_URL = "MERCHANT_URL";
	/** 商户外联平台 前置机 PORT*/
	public static final String MERCHANT_PORT = "MERCHANT_PORT";
	
	/** 网银外联平台 前置机 URL*/
	public static final String EBANK_URL = "EBANK_URL";
	/** 网银外联平台 前置机 PORT*/
	public static final String EBANK_PORT = "EBANK_PORT";
	/** 代发代扣编号*/
	public static final String BILL_CODE = "BILL_CODE";
	/** 资金平台参数 End*/
	
	
	
	/**币种*/
	public static final String CURCODE = "01"; //人民币
	
	/**接口类型 */
	public static final String TYPE_ANTIPHISHING = "1"; //防钓鱼执法
	
 
}
