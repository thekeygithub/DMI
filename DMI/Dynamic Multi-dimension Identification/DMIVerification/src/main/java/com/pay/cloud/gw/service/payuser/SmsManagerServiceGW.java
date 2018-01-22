package com.pay.cloud.gw.service.payuser;

import java.util.Map;

public interface SmsManagerServiceGW {
	/**
	 * 
	 * @Description: 发送短信验证码
	 * @Title: smsSendCode 
	 * @param phone 手机号
	 * @param operateType 请求类型
	 * 1：注册，
	 * 2：未绑卡的支付密码找回，
	 * 3：登录密码找回
	 * 4：实名认证
	 * @param hardwareId 硬件ID
	 * @param userPhone session中用户手机号
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	public Map<String,Object> smsSendCode(String appId,String phone,String hardwareId,Integer operateType,String userPhone) throws Exception;

	/**
	 * 
	 * @Description: 验证短信验证码
	 * @Title: smsValidateCode 
	 * @param phone 手机号
	 * @param verifyCode 验证码
	 * 1：注册，
	 * 2：未绑卡的支付密码找回，
	 * 3：登录密码找回
	 * 4：实名认证
	 * @param hardwareId 硬件ID
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	public Map<String,Object> smsValidateCode(String phone,String hardwareId,String verifyCode) throws Exception;
}
