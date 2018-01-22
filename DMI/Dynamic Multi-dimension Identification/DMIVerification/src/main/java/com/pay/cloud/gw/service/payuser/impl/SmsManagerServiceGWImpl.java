package com.pay.cloud.gw.service.payuser.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.gw.service.payuser.SmsManagerServiceGW;
import com.pay.cloud.pay.payuser.entity.PayUser;
import com.pay.cloud.pay.payuser.service.LogSmsService;
import com.pay.cloud.pay.payuser.service.PayUserService;
import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.pay.supplier.service.ActSpService;
import com.pay.cloud.util.SmsUtil;
import com.pay.cloud.util.hint.Hint;

@Service("smsManagerServiceGWImpl")
public class SmsManagerServiceGWImpl implements SmsManagerServiceGW{
	private static final Logger logger = Logger.getLogger(SmsManagerServiceGWImpl.class);

	@Resource
	private LogSmsService logSmsServiceImpl;
	@Resource
	private PayUserService payUserServiceImpl;
	@Resource(name="redisService")
	private RedisService redisService;
	@Resource
	private ActSpService actSpServiceImpl;
	
	@Override
	public Map<String, Object> smsSendCode(String appId,String phone,String hardwareId, Integer operateType,String userPhone) throws Exception {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			ActSp channelSp = actSpServiceImpl.findByChannelAppId(appId);
			PayUser payUser = payUserServiceImpl.findByPayUserPhone(phone);
			if(operateType == 1){//注册
				Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您正在通过手机注册账号，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
				returnMap.put("resultCode", hint.getCodeString());
	            returnMap.put("resultDesc", hint.getMessage());
				return returnMap;
			}else if(operateType == 2){//未绑卡的支付密码找回
				if(phone.equals(userPhone)){
					Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您正在通过手机找回支付密码，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
					returnMap.put("resultCode", hint.getCodeString());
		            returnMap.put("resultDesc", hint.getMessage());
					return returnMap;
				}else{
					//该手机号未绑定
					returnMap.put("resultCode", Hint.SMS_60008_THEPHONE_UNDEFINE.getCodeString());
		            returnMap.put("resultDesc", Hint.SMS_60008_THEPHONE_UNDEFINE.getMessage());
					return returnMap;
				}
			}else if(operateType == 3){//登录密码找回
					Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您正在通过手机找回登录密码，{1}分钟内有效，【注意保密】。", redisService,logSmsServiceImpl,payUser);
					returnMap.put("resultCode", hint.getCodeString());
		            returnMap.put("resultDesc", hint.getMessage());
					return returnMap;
			}else if(operateType == 4){//已绑卡找回支付密码
				Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您正在通过手机找回支付密码，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
				returnMap.put("resultCode", hint.getCodeString());
				returnMap.put("resultDesc", hint.getMessage());
				return returnMap;
			}else if(operateType == 5){//实名认证
				Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您正在验证实名认证，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
				returnMap.put("resultCode", hint.getCodeString());
	            returnMap.put("resultDesc", hint.getMessage());
				return returnMap;
			}else if(operateType == 6){//更换登录账号
				Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您要换登录账号，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
				returnMap.put("resultCode", hint.getCodeString());
	            returnMap.put("resultDesc", hint.getMessage());
				return returnMap;
			}else if(operateType == 7){//医保账号认证
				Hint hint=SmsUtil.smsCodeRequest(channelSp.getActId(),phone,hardwareId, operateType, "验证码：{0}，您要通过手机号认证本人信息，{1}分钟内有效，注意保密。", redisService,logSmsServiceImpl,payUser);
				returnMap.put("resultCode", hint.getCodeString());
	            returnMap.put("resultDesc", hint.getMessage());
				return returnMap;
			}else{
				returnMap.put("resultCode", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getCodeString());
	            returnMap.put("resultDesc", Hint.SMS_60012_OPERATETYPE_FORMAT_FAILED.getMessage());
				return returnMap;
			}
		} catch (Exception e) {
			logger.error("发送短信验证码错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SMS_60015_SMSSENDCODE_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.SMS_60015_SMSSENDCODE_EXCEPTION.getMessage());
			return returnMap;
		}
	}
	
	@Override
	public Map<String, Object> smsValidateCode(String phone,String hardwareId,String verifyCode) throws Exception {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			Hint hint=SmsUtil.smsCodeCheck(phone,hardwareId, verifyCode, redisService);
			returnMap.put("resultCode", hint.getCodeString());
            returnMap.put("resultDesc", hint.getMessage());
			return returnMap;
		} catch (Exception e) {
			logger.error("验证短信验证码错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.SMS_60016_SMSVALIDATECODE_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.SMS_60016_SMSVALIDATECODE_EXCEPTION.getMessage());
			return returnMap;
		}
	}
	
}
