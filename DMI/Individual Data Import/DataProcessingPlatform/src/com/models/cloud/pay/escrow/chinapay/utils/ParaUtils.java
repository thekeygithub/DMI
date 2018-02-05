package com.models.cloud.pay.escrow.chinapay.utils;

import com.models.cloud.util.CacheUtils;

/**
 * 平台参数Utils
 */
public class ParaUtils {
	
	/**
	 * 获取版本号
	 * @return
	 */
	public static String getVersion(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.VERSION);
	}
	
	/**
	 * 获取商户号
	 * @return
	 */
	public static String getMerId(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.MERID);
	}
	
	/**
	 * 获取业务类型
	 * @return
	 */
	public static String getBusiType(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.BUSITYPE);
	}
	
	/**
	 * 获取消费类交易商户前台通知地址
	 * @return
	 */
	public static String getConsumeTradeMerpageurl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.CONSUME_TRADE_MERPAGEURL);
	}
	
	
	/**
	 * 获取消费类交易商户后台通知地址
	 * @return
	 */
	public static String getConsumeTradeMerbgurl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.CONSUME_TRADE_MERBGURL);
	}
	
	/**
	 * 获取消费类交易URL
	 * @return
	 */
	public static String getConsumeTradeUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.CONSUME_TRADE_URL);
	}
	
	/**
	 * 获取后续类交易URL
	 * @return
	 */
	public static String getContinueTradeUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId+ "_" + ChinaPayConstants.CONTINUE_TRADE_URL);
	}
	
	/**
	 * 获取后续类交易商户后台通知地址
	 * @return
	 */
	public static String getContinueTradeMerbgurl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.CONTINUE_TRADE_MERBGURL);
	}
	
	/**
	 * 获取交易查询URL
	 * @return
	 */
	public static String getTradeQueryUrl(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.TRADE_QUERY_URL);
	}
	
	/**
	 * 获取交易查询交易类型
	 * @return
	 */
	public static String getTradeQueryTranType(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.TRADE_QUERY_TRANTYPE);
	}
	
	/**
	 * 获取后续交易退款交易类型
	 * @return
	 */
	public static String getContinueTradeRefundTranType(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.CONTINUE_TRADE_REFUND_TRANTYPE);
	}
	
	/**
	 * 分账类型
	 * @param fundId
	 * @return
	 */
	public static String getSplitType(String fundId){
		return CacheUtils.getPpIntfPara(fundId + "_" + ChinaPayConstants.SPLIT_TYPE);
	}
}
