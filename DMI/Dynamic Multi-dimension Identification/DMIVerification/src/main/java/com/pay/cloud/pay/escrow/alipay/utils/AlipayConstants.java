package com.pay.cloud.pay.escrow.alipay.utils;
/**
 * 
 * @author yanjie.ji
 * @date 2016年8月11日 
 * @time 下午3:29:51
 */
public class AlipayConstants {
	/**
	 * 阿里接口反馈信息--成功
	 */
	public static final String ALIPAY_RESPONSE_SUCCESS = "T";
	/**
	 * 阿里接口反馈信息--失败
	 */
	public static final String ALIPAY_RESPONSE_FAIL ="F";
	
	public static final String FORMAT_JSON = "JSON";
	
	public static final String CHARSET_UTF8 ="UTF-8";
	
	public static final String SIGN_TYPE="RSA";
	
	public static final String VERSION="1.0";
	
	public static final String PAYMENT_TYPE = "1";
	
	public static final String SERVICE_MOBILE_PAY="mobile.securitypay.pay";

    public static final String SIGN_TYPE_RSA                  = "RSA";
    
    public static final int CONNECT_TIMEOUT = 30000;
    
    public static final int READ_TIMEOUT = 90000;

    /**
     * sha256WithRsa 算法请求类型
     */
    public static final String SIGN_TYPE_RSA2                 = "RSA2";

    public static final String SIGN_ALGORITHMS                = "SHA1WithRSA";

    public static final String SIGN_SHA256RSA_ALGORITHMS      = "SHA256WithRSA";

    public static final String ENCRYPT_TYPE_AES               = "AES";

    public static final String APP_ID                         = "app_id";

    public static final String FORMAT                         = "format";

    public static final String METHOD                         = "method";

    public static final String TIMESTAMP                      = "timestamp";

    public static final String SIGN                           = "sign";
    public static final String SIGN_TYPE_STR                  = "sign_type";

    public static final String ALIPAY_SDK                     = "alipay_sdk";

    public static final String ACCESS_TOKEN                   = "auth_token";

    public static final String APP_AUTH_TOKEN                 = "app_auth_token";

    public static final String TERMINAL_TYPE                  = "terminal_type";

    public static final String TERMINAL_INFO                  = "terminal_info";

    public static final String NOTIFY_URL                     = "notify_url";

    public static final String RETURN_URL                     = "return_url";

    public static final String ENCRYPT_TYPE                   = "encrypt_type";

    //-----===-------///

    public static final String BIZ_CONTENT_KEY                = "biz_content";

    /** 默认时间格式 **/
    public static final String DATE_TIME_FORMAT               = "yyyy-MM-dd HH:mm:ss";

    /**  Date默认时区 **/
    public static final String DATE_TIMEZONE                  = "GMT+8";

    /** GBK字符集 **/
    public static final String CHARSET_GBK                    = "GBK";

    /** XML 应格式 */
    public static final String FORMAT_XML                     = "xml";

    /** SDK版本号 */
    public static final String SDK_VERSION                    = "alipay-sdk-java-dynamicVersionNo";

    public static final String PROD_CODE                      = "prod_code";

    /** 老版本失败节点 */
    public static final String ERROR_RESPONSE                 = "error_response";

    /** 新版本节点后缀 */
    public static final String RESPONSE_SUFFIX                = "_response";

    /** 加密后XML返回报文的节点名字 */
    public static final String RESPONSE_XML_ENCRYPT_NODE_NAME = "response_encrypted";



}
