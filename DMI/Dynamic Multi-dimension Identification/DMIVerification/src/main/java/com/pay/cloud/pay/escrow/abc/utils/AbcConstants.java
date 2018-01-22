package com.pay.cloud.pay.escrow.abc.utils;

/**
 * 农业银行参数类
 */
public class AbcConstants {
	
	/** 交易类型 */
	public static final String PAYTYPEID_IMMEDIATEPAY = "ImmediatePay"; //直接支付
	public static final String PAYTYPEID_REFUND = "Refund"; //分期支付
	
	/**交易币种*/
	public static final String CURRENCYCODE_CNY = "156"; //分期支付
	
	/**分期标识*/
	public static final String INSTALLMENTMARK_YES = "1"; //分期支付
	public static final String INSTALLMENTMARK_NO = "0"; //不分期支付
	
	/**支付类型*/
	public static final String PAYMENTTYPE_1 = "1";  //农行卡支付
	public static final String PAYMENTTYPE_2 = "2";  //国际卡支付
	public static final String PAYMENTTYPE_3 = "3";  //农行贷记卡支付
	public static final String PAYMENTTYPE_5 = "5";  //基于第三方的跨行支付
	public static final String PAYMENTTYPE_6 = "6";  //银联跨行支付
	public static final String PAYMENTTYPE_7 = "7";  //对公户
	public static final String PAYMENTTYPE_A = "A";  //支付方式合并
	
	/**交易渠道*/
	public static final String PAYMENTLINKTYPE_INTERNET = "1"; //internet网络接入
	public static final String PAYMENTLINKTYPE_PHONE = "2";    //手机网络接入
	public static final String PAYMENTLINKTYPE_TV = "3";       //数字电视网络接入
	public static final String PAYMENTLINKTYPE_SMART = "4";    //智能客户端
	
	
	/** 回调URL*/
	public static final String RESULT_NOTIFY_URL = "RESULT_NOTIFY_URL";
	/**商户顺序号*/
	public static final String MER_NO = "MER_NO";
	
	/**订单查询参数*/
	public static final String QUERYDETAIL_STATE = "0";  // 0：状态查询
	public static final String QUERYDETAIL_DETAIL = "1"; // 1：详细查询
	
	/**压缩标识*/
	public static final String UNZIP = "0";  // 0：不压缩
	public static final String ZIP = "1";  // 1：压缩
	
	
	
	public static final String RETURN_CODE_SUCCESS = "0000"; //成功
	
	
	
}
