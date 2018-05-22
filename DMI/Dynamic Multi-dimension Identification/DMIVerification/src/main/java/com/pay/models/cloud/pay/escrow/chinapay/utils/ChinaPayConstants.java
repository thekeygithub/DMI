package com.models.cloud.pay.escrow.chinapay.utils;

/**
 * 银联参数
 */
public class ChinaPayConstants {

	
	/** 资金平台参数 Begin*/
	/** 版本号*/
	public static final String VERSION = "VERSION";
	/** 商户号*/
	public static final String MERID = "MERID";
	/** 业务类型*/
	public static final String BUSITYPE = "BUSITYPE";
	/** 消费类交易商户前台通知地址*/
	public static final String CONSUME_TRADE_MERPAGEURL = "CONSUME_TRADE_MERPAGEURL";
	/** 消费类交易商户后台通知地址*/
	public static final String CONSUME_TRADE_MERBGURL = "CONSUME_TRADE_MERBGURL";
	/** 消费类交易URL*/
	public static final String CONSUME_TRADE_URL = "CONSUME_TRADE_URL";
	/** 后续类交易URL*/
	public static final String CONTINUE_TRADE_URL = "CONTINUE_TRADE_URL";
	/** 后续类交易商户后台通知地址*/
	public static final String CONTINUE_TRADE_MERBGURL = "CONTINUE_TRADE_MERBGURL";
	/** 交易查询URL*/
	public static final String TRADE_QUERY_URL = "TRADE_QUERY_URL";
	/** 交易查询交易类型*/
	public static final String TRADE_QUERY_TRANTYPE = "TRADE_QUERY_TRANTYPE";
	/** 后续交易退款交易类型*/
	public static final String CONTINUE_TRADE_REFUND_TRANTYPE = "CONTINUE_TRADE_REFUND_TRANTYPE";
	/** 分账类型*/
	public static final String SPLIT_TYPE = "SPLIT_TYPE";
	
	
	/** 资金平台参数 End*/
	
	/**分账模式 0：按金额分账1：按比例分账*/
	public static final String SPLIT_METHOD_MONEY = "0";//金额
	public static final String SPLIT_METHOD_RATIO = "1";//比例
}
