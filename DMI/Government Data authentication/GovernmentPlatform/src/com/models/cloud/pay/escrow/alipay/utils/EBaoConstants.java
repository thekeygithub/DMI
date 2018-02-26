package com.models.cloud.pay.escrow.alipay.utils;
/**
 * EBAO使用的常量
 * @author yanjie.ji
 * @date 2016年8月12日 
 * @time 上午10:50:59
 */
public class EBaoConstants {
	/**
	 * ALIPAY配置
	 */
	public static final String FUND_ID_ALIPAY="ALIPAY_KF";
	/**
	 * ALIPAY配置-商户号
	 */
	public static final String PARA_TYPE_APP_ID = "APP_ID";
	/**
	 * ALIPAY配置-合作者身份ID
	 */
	public static final String PARA_TYPE_PARTNER = "PARTNER";
	/**
	 * ALIPAY配置-卖家支付宝账号
	 */
	public static final String PARA_TYPE_SELLER_ID="SELLER_ID";
	/**
	 * ALIPAY配置-支付宝公钥
	 */
	public static final String PARA_TYPE_ALI_PUBLIC_KEY = "ALI_PUBLIC_KEY";
	
	/**
	 * ALIPAY配置-私钥
	 */
	public static final String PARA_TYPE_PRIVATE_KEY = "PRIVATE_KEY";
	/**
	 * ALIPAY配置-公钥
	 */
	public static final String PARA_TYPE_PUBLIC_KEY = "PUBLIC_KEY";
	/**
	 * ALIPAY配置-URL
	 */
	public static final String PARA_TYPE_URL = "URL";
	/**
	 * ALIPAY配置-阿里网关
	 */
	public static final String PARA_TYPE_ALI_GATEWAY = "ALI_GATEWAY";
	/**
	 * ALIPAY配置-移动支付接口
	 */
	public static final String PARA_TYPE_MOBILE_PAY_METHOD = "MOBILE_PAY_METHOD";
	/**
	 * ALIPAY配置-即时到账接口
	 */
	public static final String PARA_TYPE_IMMEDIATE_PAY_METHOD = "IMMEDIATE_PAY_METHOD";
	/**
	 * ALIPAY配置-APP支付接口
	 */
	public static final String PARA_TYPE_APP_PAY_METHOD = "APP_PAY_METHOD";
	/**
	 * ALIPAY配置-支付查询接口
	 */
	public static final String PARA_TYPE_QUERY_METHOD = "QUERY_METHOD";
	/**
	 * ALIPAY配置-退款接口
	 */
	public static final String PARA_TYPE_REFUND_METHOD = "REFUND_METHOD";
	/**
	 * ALIPAY配置-退款查询接口
	 */
	public static final String PARA_TYPE_REFUND_QUERY_METHOD = "REFUND_QUERY_METHOD";
	/**
	 * ALIPAY配置-字符编码
	 */
	public static final String PARA_TYPE_CHARSET = "CHARSET";
	/**
	 * ALIPAY配置-加密方式
	 */
	public static final String PARA_TYPE_SIGN_TYPE = "SIGN_TYPE";
	/**
	 * ALIPAY配置-时间格式
	 */
	public static final String PARA_TYPE_TIMESTAMP = "TIMESTAMP";
	/**
	 * ALIPAY配置-版本
	 */
	public static final String PARA_TYPE_VERSION = "VERSION";
	
	
	/**
	 * 拼接字符串，以_(下划线)分隔
	 * @param paras
	 * @return
	 */
	public static String joinStr(String[] paras){
		if(paras==null||paras.length<1) return "";
		StringBuffer str = new StringBuffer();
		boolean first = true;
		for (String string : paras) {
			if(first){
				str.append(string);
				first = false;
			}else{
				str.append("_").append(string);
			}
		}
		return str.toString();
	}
}
