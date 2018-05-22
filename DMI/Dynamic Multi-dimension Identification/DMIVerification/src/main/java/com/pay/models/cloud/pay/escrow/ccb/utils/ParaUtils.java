package com.models.cloud.pay.escrow.ccb.utils;

import com.models.cloud.util.CacheUtils;

/**
 * 平台参数Utils
 */
public class ParaUtils {
	
	
	
	/**
	 * 获取网上支付URL
	 * @return
	 */
	public static String getNetPayUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.NETPAY_URL);
	}
	
	/**
	 * 获取公钥
	 * @return
	 */
	public static String getPubKey(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.PUB);
	}
	
	/**
	 * 获取商户外联平台URL
	 * @return
	 */
	public static String getMerchantUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.MERCHANT_URL);
	}
	
	/**
	 * 获取商户外联平台Port
	 * @return
	 */
	public static String getMerchantPort(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.MERCHANT_PORT);
	}
	
	
	/**
	 * 获取商户代码 
	 * @return
	 */
	public static String getMerchantId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.MERCHANTID);
	}
	
	/**
	 * 获取操作员号
	 * @return
	 */
	public static String getUserId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.USER_ID);
	}
	
	/**
	 * 获取密码
	 * @return
	 */
	public static String getPassword(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.PASSWORD);
	}
	
	/**
	 * 获取授权号
	 * @return
	 */
	public static String getAuthId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.AUTHID);
	}
	
	/**
	 * 获取商户所属分行号
	 * @return
	 */
	public static String getBranchId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.BRANCHID);
	}
	
	/**
	 * 获取交易货币代码
	 * @return
	 */
	public static String getCurCode(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.CURCODE);
	}
	
	/**
	 * 获取网银柜台编号
	 * @return
	 */
	public static String getPosId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.POSID);
	}
	
	/**
	 * 获取公钥
	 * @return
	 */
	public static String getPub(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.PUB);
	}
	
	/**
	 * 获取语言
	 * @return
	 */
	public static String getLanguage(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.LANGUAGE);
	}
	
	/**
	 * 获取客户号
	 * @return
	 */
	public static String getCustId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.CUST_ID);
	}
	
	
	/**
	 * 获取网银商户外联平台URL
	 * @return
	 */
	public static String getEBankUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.EBANK_URL);
	}
	
	/**
	 * 获取网银商户外联平台Port
	 * @return
	 */
	public static String getEBankPort(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.EBANK_PORT);
	}
	
	/**
	 * 获取
	 * @return
	 */
	public static String getBillCode(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + CcbConstants.BILL_CODE);
	}

}
