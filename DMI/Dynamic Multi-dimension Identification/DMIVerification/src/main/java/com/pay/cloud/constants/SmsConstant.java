package com.pay.cloud.constants;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.util.hint.Propertie;
/**
 * 
 * @Description: 短信管理常量类
 * @ClassName: SmsConstant 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午2:56:28
 */
public class SmsConstant {
	/**
	 * SESSION中存储用户的KEY
	 */
	public static final String SESSION_USER_KEY = "_EBAO_USER_KEY_";
	public static final String SESSION_USER_ORDER_KEY = "_EBAO_USER_ORDER_KEY_";
	public static final String SESSION_USER_ORDER_INFO_KEY = "_EBAO_USER_ORDER_INFO_KEY_";
	/**
	 * REDIS中存储发送的短信验证码的KEY
	 */
	public static final String SMS_CODE_ATTR = "_SMS_CODE_ATTR_";
	
	/**
	 * REDIS中存储短信发送时间的KEY
	 */
	public static final String SMS_CODE_TIME = "_SMS_CODE_TIME_";


	/**
	 * REDIS中存储已发送短信验证码
	 */
	public static final String REDIS_SMS_MESSAGE = "_REDIS_SMS_MESSAGE_";
	
//######################################################################################################################################################################
	/**
	 * 短信发送地址
	 */
	public static String SMS_URL="";
	
	/**
	 * 短信发送智验apiKey
	 */
	public static String SMS_APIKEY="";
	
	/**
	 * 短信发送应用appId
	 */
	public static String SMS_APPID="";

	/**
	 * 短信发送,短信有效时间，单位是分钟
	 */
	public static String SMS_VALID_TIME="";
	
	/**
	 * 短信发送,间隔时间，单位是毫秒
	 */
	public static String SMS_FREQUENCY_TIME="";
	
	/**
	 * 短信发送,注册模板
	 */
	public static String SMS_REGISTERFROMAT="";
	
	/**
	 * 短信发送,密码找回模板
	 */
	public static String SMS_CALLBACKPASSFROMAT="";

	/**
	 * 短信发送是否真实发送，0:realSend 1:notRealSend
	 */
	public static String SMS_ISREALSENDSMS="";
	
	static{
		//短信发送地址
		if(StringUtils.isBlank(SMS_URL)){
			SMS_URL=Propertie.APPLICATION.value("sms.url");
		}
		
		//短信发送智验apiKey
		if(StringUtils.isBlank(SMS_APIKEY)){
			SMS_APIKEY=Propertie.APPLICATION.value("sms.apiKey");
		}
		
		//短信发送应用appId
		if(StringUtils.isBlank(SMS_APPID)){
			SMS_APPID=Propertie.APPLICATION.value("sms.appId");
		}
		
		//短信发送,短信有效时间，单位是分钟
		if(StringUtils.isBlank(SMS_VALID_TIME)){
			SMS_VALID_TIME=Propertie.APPLICATION.value("sms.valid.time");
		}
		
		//短信发送,间隔时间，单位是毫秒
		if(StringUtils.isBlank(SMS_FREQUENCY_TIME)){
			SMS_FREQUENCY_TIME=Propertie.APPLICATION.value("sms.frequency.time");
		}
		
		//短信发送,注册模板
		if(StringUtils.isBlank(SMS_REGISTERFROMAT)){
			SMS_REGISTERFROMAT=Propertie.APPLICATION.value("sms.registerFromat");
		}
		
		//短信发送,密码找回模板
		if(StringUtils.isBlank(SMS_CALLBACKPASSFROMAT)){
			SMS_CALLBACKPASSFROMAT=Propertie.APPLICATION.value("sms.callbackPassFromat");
		}
		
		//短信发送，是否真实发送
		if(StringUtils.isBlank(SMS_ISREALSENDSMS)){
			SMS_ISREALSENDSMS=Propertie.APPLICATION.value("mw.isRealSendSms");
		}
	}
}
