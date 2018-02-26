package com.models.cloud.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.pay.payuser.entity.LogSms;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.service.LogSmsService;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 短信公用类
 * @ClassName: SmsUtil
 * @author: zhengping.hu
 * @date: 2015年11月30日 上午9:48:03
 */
public class SmsUtil {

	private static final Logger logger = Logger.getLogger(SmsUtil.class);

	/**
	 * 
	 * @Description: 发送短信验证码
	 * @Title: smsCodeRequest 
	 * @param phone 手机号
	 * @param operateType 请求类型 1：注册 2：未绑卡的支付密码找回 3：登录密码找回 4：已绑卡找回支付密码 5：实名认证 6：更换登录账号 7：医保账户认证
	 * @param format 短信模板
	 * @param redisService 缓存
	 * @return Hint
	 */
	public static Hint smsCodeRequest(Long chanActId, String phone, String hardwareId, Integer operateType, String format, RedisService redisService, LogSmsService logSmsServiceImpl, PayUser payUser) {
		phone = String.valueOf(phone).trim();
		String messageKey="sendmessage_"+phone+"_"+hardwareId;
		
		logger.info("messsagekey:"+messageKey);
		// 获取当前会话上次发送短信验证码请求时间
		Long time = null;
		if (redisService.getMap(messageKey)!=null) {
			if(!redisService.getMap(messageKey).isEmpty()){
				time = Long.parseLong(redisService.getMap(messageKey).get("requestTime"));
			}
		}
		
		// 判断当前会话上次发送短信验证码请求时间与本次请求的间隔时间，超出间隔允许再次发送
		if (time != null && System.currentTimeMillis() - time < Long.parseLong(SmsConstant.SMS_FREQUENCY_TIME)) {
			// 发送验证码过于频繁，发送失败
			return Hint.SMS_60001_SED_FREQUENTLY;
		} 
		
		if (!ValidateUtils.isPhoneNumber(phone)) {
			// 手机号格式不正确，发送失败
			return Hint.SMS_60002_PHONE_FORMAT_ERROR;
		}
		// 发送短信
		HashMap<String, Object> respMap = new HashMap<String, Object>();
		String verify ="123456";
		try {
			//短信发送次数计算 start
			String messageCountKey="sendCount_"+phone+"_"+operateType;
			boolean newCount=false;
			if (redisService.getMap(messageCountKey)!=null) {
				if(!redisService.getMap(messageCountKey).isEmpty()){
					Integer countnum = Integer.parseInt(redisService.getMap(messageCountKey).get("countnum"));
					String countdate = redisService.getMap(messageCountKey).get("countdate");
					
					String dd=DateUtils.getNowDate("yyyyMMdd");
					if(countdate.equals(dd)){
						countnum++;
						int SMS_CHK_MAX =5;
						if(!CacheUtils.getDimSysConfConfValue("SMS_CHK_MAX").equals("")){
							SMS_CHK_MAX = Integer.valueOf(CacheUtils.getDimSysConfConfValue("SMS_CHK_MAX"));
						}
						if(countnum>SMS_CHK_MAX && !operateType.toString().equals("7")){ //社保卡验证短信不进行次数判断
							return Hint.SMS_60020_SENDCOUNTOUT_FAILED;
						}
						
						//# 0:realSend 1:notRealSend
						if(SmsConstant.SMS_ISREALSENDSMS.equals("0")){
							verify =ConvertUtils.genRandom(6);
							String content=java.text.MessageFormat.format(format, verify, SmsConstant.SMS_VALID_TIME);
							respMap = SendSmsMWUtil.requestSendSmsWebservice(phone, content);
						}else{
							respMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
							respMap.put("resultDesc", "发送短信成功");
						}
						short smsSendFlag = 0;//是否发送成功 1-是 0-否
						if(Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(respMap.get("resultCode")).trim())){
							smsSendFlag = 1;
						}
						saveLogSmsInfo(phone, payUser, chanActId, operateType, smsSendFlag, logSmsServiceImpl);
						if(!respMap.get("resultCode").toString().equals("0")){
							// 发送短信验证码失败
							return Hint.SMS_60000_SEND_FAILED;
						}
						
						Map<String,String> sendCountMap=new HashMap<String,String>();
						sendCountMap.put("countnum", countnum+"");
						sendCountMap.put("countdate", dd);
						redisService.setMap(messageCountKey,sendCountMap);
					}else{
						newCount=true;
					}
				}else{
					newCount=true;
				}
			}else{
				newCount=true;
			}
			
			if(newCount){
				//# 0:realSend 1:notRealSend
				if(SmsConstant.SMS_ISREALSENDSMS.equals("0")){
					verify =ConvertUtils.genRandom(6);
					String content=java.text.MessageFormat.format(format, verify, SmsConstant.SMS_VALID_TIME);
					respMap = SendSmsMWUtil.requestSendSmsWebservice(phone, content);
				}else{
					respMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					respMap.put("resultDesc", "发送短信成功");
				}
				short smsSendFlag = 0;//是否发送成功 1-是 0-否
				if(Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(respMap.get("resultCode")).trim())){
					smsSendFlag = 1;
				}
				saveLogSmsInfo(phone, payUser, chanActId, operateType, smsSendFlag, logSmsServiceImpl);
				if(!respMap.get("resultCode").toString().equals("0")){
					// 发送短信验证码失败
					return Hint.SMS_60000_SEND_FAILED;
				}
				Map<String,String> sendCountMap=new HashMap<String,String>();
				sendCountMap.put("countnum", "1");
				sendCountMap.put("countdate", DateUtils.getNowDate("yyyyMMdd"));
				redisService.setMap(messageCountKey,sendCountMap);
			}
			
			//短信发送次数计算 end
			// 会话存储验证码
			Map<String,String> messageMap=new HashMap<String,String>();
			messageMap.put("phone", phone);
			messageMap.put("verifycode", verify);
			messageMap.put("operateType", operateType.toString());//业务类型
			messageMap.put("requestTime", System.currentTimeMillis()+"");// 会话存储本次请求时间
			messageMap.put("sms_status","0");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
			messageMap.put("sms_validate_result","0");//验证验证码生成，0：未验证，1：验证通过，2：验证不通过
			redisService.setMap(messageKey, messageMap);
			return Hint.SYS_SUCCESS;
			
		} catch (Exception e) {
			// 发送短信验证码失败
			return Hint.SMS_60000_SEND_FAILED;
		}

	}

	private static void saveLogSmsInfo(String phone, PayUser payUser, Long chanActId, Integer operateType, short smsSendFlag, LogSmsService logSmsServiceImpl) throws Exception {
		LogSms logSms = new LogSms();
		logSms.setLogId(Long.parseLong(IdCreatorUtils.getLogSmsRecId()));
		logSms.setPhoneNo(phone);
		if(null != payUser){
			logSms.setActId(payUser.getActId());
		}
		logSms.setChanActId(chanActId);
		logSms.setBusiTypeCode(operateType.toString());//业务类型编码
		logSms.setSmsChanCode(DimDictEnum.SMS_CHANNEL_MENGWANG.getCodeString());//短信通道编码
		logSms.setRetryCnt(0);
		logSms.setSmsSendFlag(smsSendFlag);
		logSms.setLogTime(new Date());
		logger.info("开始保存发送短信验证码日志信息");
		logSmsServiceImpl.saveLogSms(logSms);
		logger.info("<<保存成功");
	}

	/**
	 * 
	 * @Description: 验证验证码是否通过
	 * @Title: smsCodeCheck 
	 * @param phone 手机号
	 * @param verifyCode 验证码
	 * @param redisService 缓存
	 * @return Hint
	 */
	public static Hint smsCodeCheck(String phone,String hardwareId,String verifyCode,RedisService redisService) {
		String messageKey = "sendmessage_" + phone.trim()+"_"+hardwareId;

		if (redisService.getMap(messageKey) == null || redisService.getMap(messageKey).isEmpty()) {
			// 验证码不存在，验证失败
			return Hint.SMS_60004_VERIFYCODE_NONE;
		} else {
			// 获取当前会话存储的短信验证码
			Map<String, String> messageMap = redisService.getMap(messageKey);

			if (!"0".equals(messageMap.get("sms_status").toString())) {
				// 验证码已使用
				messageMap.put("sms_validate_result", "2");// 验证验证码生成，0：未验证，1：验证通过，2：验证不通过
				messageMap.put("sms_status", "1");// 验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60017_VERIFYCODEUSED_FAILED;
			}

			if (System.currentTimeMillis() - Long.parseLong(messageMap.get("requestTime")) > Long.parseLong(SmsConstant.SMS_VALID_TIME) * 60000) {
				// 验证码超出有效期，验证失败
				messageMap.put("sms_validate_result", "2");// 验证验证码生成，0：未验证，1：验证通过，2：验证不通过
				messageMap.put("sms_status", "1");// 验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60005_CHECKVERIFYCODE_TIMEOUT;
			}

			if (!verifyCode.toLowerCase().equals(messageMap.get("verifycode").toLowerCase().toString())) {
				// 验证码验证不通过
				messageMap.put("sms_validate_result", "2");// 验证验证码生成，0：未验证，1：验证通过，2：验证不通过
//				messageMap.put("sms_status", "1");// 验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60006_VERIFYCODE_DIFFERENT;
			}

			messageMap.put("sms_validate_result", "1");// 验证验证码生成，0：未验证，1：验证通过，2：验证不通过
			messageMap.put("sms_status", "1");// 验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
			redisService.setMap(messageKey, messageMap);
			return Hint.SYS_SUCCESS;
		}
	}

	/**
	 * 
	 * @Description: 验证验证码是否通过
	 * @Title: smsCodeCheckNoTime 
	 * @param phone 手机号
	 * @param operateType 请求类型
	 * @param redisService 缓存
	 * @return Hint
	 */
	public static Hint smsCodeCheckNoVerifyCode(String phone,String hardwareId,Integer operateType,RedisService redisService) {
		String messageKey="sendmessage_"+phone.trim()+"_"+hardwareId;
		
		if (redisService.getMap(messageKey) == null||redisService.getMap(messageKey).isEmpty()) {
			// 验证码不存在，验证失败
			return Hint.SMS_60004_VERIFYCODE_NONE;
		} else {
			// 获取当前会话存储的短信验证码
			Map<String, String> messageMap = redisService.getMap(messageKey);
			if ("2".equals(messageMap.get("sms_validate_result").toString())) {
				// 验证验证码失败
				messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60003_CHECKVERIFYCODE_FAILED;
			}
			
			if ("0".equals(messageMap.get("sms_status").toString())	|| "0".equals(messageMap.get("sms_validate_result").toString())) {
				// 手机号验证操作未执行
				messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60009_PHONEUNVERIFY_FAILED;
			}
			if ("3".equals(messageMap.get("sms_status").toString())) {
				// 验证码已使用
				messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60017_VERIFYCODEUSED_FAILED;
			}
			
			if (operateType != Integer.parseInt(messageMap.get("operateType").toString())) {
				// 请求类型验证不通过
				messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60018_OPERATETYPE_FAILED;
			}
			
			if (System.currentTimeMillis() - Long.parseLong(messageMap.get("requestTime")) > Long.parseLong(SmsConstant.SMS_VALID_TIME) * 60000) {
				// 验证码超出有效期，验证失败
				messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
				redisService.setMap(messageKey, messageMap);
				return Hint.SMS_60005_CHECKVERIFYCODE_TIMEOUT;
			}
			
			messageMap.put("sms_status","3");//验证码使用状态:0：未使用，1：已验证验证码，3：业务已验证
			redisService.setMap(messageKey, messageMap);
			return Hint.SYS_SUCCESS;
		}
	}
	
	/**
	 * 
	 * @Description: 发送验证码
	 * @Title: postSendMsgs 
	 * @param mobile
	 * @param format
	 * @return String
	 */
	private static String postSendMsgs(String mobile,String format) {
		//生成验证码
		String smsCode = ConvertUtils.genRandom(4);

		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Accept-Charset", "UTF-8");
		header.put("Accept", "application/json");
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);// 手机号
		params.put("content",java.text.MessageFormat.format(format, smsCode, SmsConstant.SMS_VALID_TIME));// 短信内容
		//params.put("uid", uid);// 用户自定义的短信ID
		params.put("apiKey",SmsConstant.SMS_APIKEY);// 智验apiKey
		params.put("appId",SmsConstant.SMS_APPID);// 应用appId
		params.put("timestamp", System.currentTimeMillis() + "");// 时间戳
		try {
			String singleResponse = sendPost(SmsConstant.SMS_URL, params, "utf-8", header);
			@SuppressWarnings("unchecked")
			Map<String,String> map=JsonStringUtils.jsonStringToObject(singleResponse, Map.class);
			System.out.println(map.toString());
			if(map.get("result").equals("SUCCESS")){
				return smsCode;
			}else{
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	private static String sendPost(String url, Map<String, String> params, String charset,Map<String, String> headerMap) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = SpringBeanUtil.getSpringBean("httpClientFactory",HttpComponentsClientHttpRequestFactory.class).getHttpClient();//
			httpPost = new HttpPost(url);
			if (null != headerMap && headerMap.size() > 0) {
				for (Map.Entry<String, String> entry : headerMap.entrySet())
					httpPost.setHeader(entry.getKey(), entry.getValue());
			}

			// 设置参数
			StringEntity myEntity = new StringEntity(JsonStringUtils.objectToJsonString(params), "UTF-8");
			httpPost.setEntity(myEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

//	public static void main(String[] args) {
//		System.out.println(SmsUtil.postSendMsgs("18612620184",Constant.SMS_REGISTERFROMAT));
//		System.out.println(SmsUtil.postSendMsgs("18612620184",Constant.SMS_CALLBACKPASSFROMAT));
//	}
	
}