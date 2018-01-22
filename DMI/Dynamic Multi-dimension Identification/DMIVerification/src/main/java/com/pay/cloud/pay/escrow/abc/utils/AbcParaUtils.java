package com.pay.cloud.pay.escrow.abc.utils;

import com.pay.cloud.util.CacheUtils;

/**
 * 农业银行平台参数
 */
public class AbcParaUtils {
	
	/**
	 * 获取支付回调地址
	 * @return
	 */
	public static String getResultNotifyURL(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + AbcConstants.RESULT_NOTIFY_URL);
	}

	/**
	 * 获取商户顺序号
	 * @param fundId
	 * @return
	 */
	public static String getMerNo(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + AbcConstants.MER_NO);
	}
}
