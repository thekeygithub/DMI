package com.pay.cloud.constants;

/**
 * 配置参数常量类
 * @author yanjie.ji
 * @date 2016年7月18日 
 * @time 下午2:33:23
 */
public class PropertiesConstant {
	/**
	 * 身份证校验有效的出生年起始
	 */
	public static final String ID_CARD_NO_BIRTH_START="id.card.no.birth.start";
	/**
	 * 身份证校验有效的出生年截止
	 */
	public static final String ID_CARD_NO_BIRTH_END="id.card.no.birth.end";

	/**
	 * 身份证校验有效的出生年截止
	 */
	public static final String CERT_REGULAR_CHINESE_NAME="cert.regular.chinese.name";
	/**
	 * 身份证校验有效的出生年截止
	 */
	public static final String CERT_REGULAR_CHINESE_PHONE_NO="cert.regular.chinese.phone.no";
	
	/**
	 * 实名认证模式
	 */
	public static final String CERT_MODEL_TEST="cert.model.test";
	
	/**
	 * 实名认证--普惠四要素接口url
	 */
	public static final String CERT_CONFIG_PH_FOUR_URL="cert.config.ph.four.url";
	/**
	 * 实名认证--普惠商户uid
	 */
	public static final String CERT_CONFIG_PH_UID="cert.config.ph.uid";
	/**
	 * 实名认证--普惠安全校验码
	 */
	public static final String CERT_CONFIG_PH_SAFETY_CODE="cert.config.ph.safety.code";
	/**
	 * 实名认证--普惠RSA私钥文件路径
	 */
	public static final String CERT_CONFIG_PH_RSA_PRIVATE_KEY_FILE_PATH="cert.config.ph.rsa.private.key.file.path";
	
	/**
	 * 透传系统所URL
	 */
	public static final String TRANSPARENT_URL = "transparent.url";
	/**
	 * 透传系统所需的渠道
	 */
	public static final String TRANSPARENT_SYSTEM = "transparent.system";
	/**
	 * 透传系统所需的AES密钥
	 */
	public static final String TRANSPARENT_AESKEY = "transparent.aeskey";
	/**
	 * MTS服务地址
	 */
	public static final String MTS_SERVICE_URL="mts.service.url";
	/**
	 * 社保123服务地址
	 */
	public static final String EBAO123_SERVICE_URL="ebao123.service.url";
	/**
	 * 社保123所需AES密钥
	 */
	public static final String EBAO123_AESKEY = "ebao123.aeskey";
}
