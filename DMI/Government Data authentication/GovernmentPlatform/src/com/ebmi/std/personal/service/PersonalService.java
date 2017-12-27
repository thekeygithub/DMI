package com.ebmi.std.personal.service;

import java.util.Map;

import com.gd.platform.client.ResultEntity;

/**
 * @Description: 权限类
 * @ClassName: PersonalService
 * @author: zhengping.hu
 * @date: 2015年12月17日 下午5:11:24
 */
public interface PersonalService {
	/**
	 * xuhai add 2016-6-1 16:56:28
	 * @param p_mi_id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	boolean checkuser(String p_mi_id, String password) throws Exception;

	/**
	 * @Description: 注册--社保信息验证（允许第三方平台注册时，要求同时验证社保信息与第三方平台账户是否绑定）
	 * @Title: validateSocialsecurityInfo
	 * @param name 姓名
	 * @param id_no 身份证
	 * @param sb_no 社保卡
	 * @param sb_pass 社保卡密码
	 * @return String 验证通过：返回p_mi_id ，验证不通过：返回空
	 * @throws Exception
	 */
	ResultEntity validateSocialsecurityInfo(String id_no, String name) throws Exception;

	ResultEntity validateSmsCode(String id_no, String name, String reg_key, String phone) throws Exception;
	
	/**
	 * @Description: 第三方平台注册
	 * @Title: register
	 * @param reg_key 用户注册主键序号
	 * @param id_no 身份证号
	 * @param name 姓名
	 * @param user_name 用户名
	 * @param user_pass 用户密码
	 * @param phone 手机号
	 * @param email 电子邮箱
	 * @param qq QQ号
	 * @param weixin 微信号
	 * @param sms_key 短信发送授权序号
	 * @param sms_code 短信验证码
	 * @return boolean 注册成功：true，注册失败：false
	 * @throws Exception
	 */
	ResultEntity register(String reg_key, String id_no, String name, String user_name, String user_pass, String phone, String email,
			String qq, String weixin, String sms_key, String sms_code) throws Exception;

	/**
	 * @Description: 第三方平台验证用户名称是否重复
	 * @Title: validateUserName
	 * @param user_name 用户名称
	 * @return boolean true:用户名不存在，false：用户名存在
	 * @throws Exception
	 */
	boolean validateUserName(String user_name) throws Exception;

	/**
	 * @Description: 第三方平台更新手机号
	 * @Title: pushUserPhone
	 * @param p_mi_id 用户编号
	 * @param phone 手机号
	 * @return boolean true:更新成功，false：更新失败
	 * @throws Exception
	 */
	boolean pushUserPhone(String p_mi_id, String phone) throws Exception;

	/**
	 * @Description: 第三方平台更新密码
	 * @Title: pushUserPass
	 * @param name 
	 * @param password 密码
	 * @return boolean true:更新成功，false：更新失败
	 * @throws Exception
	 */
	Map<String, String> pushUserPass(String name, String oldPass, String newPass);

	/**
	 * @Description: 第三方平台或MI，通过用户身份证号取用户
	 * @Title: getMIUserByUserIdNo
	 * @param id_no 身份证号
	 * @return String
	 * @throws Exception
	 */
	String getMIUserByUserIdNo(String id_no) throws Exception;

	/**
	 * @Description: 第三方平台或MI，通过用户社保卡号取用户
	 * @Title: getMIUserByUserSSCard
	 * @param sb_no 社保卡号
	 * @return String
	 * @throws Exception
	 */
	String getMIUserByUserSSCard(String sb_no) throws Exception;

	/**
	 * @Description: 第三方平台或MI，通过用户编号取用户
	 * @Title: getMIUser
	 * @param p_mi_id 用户编号
	 * @return String {真实姓名:"", 社保卡号 :"",身份证号 :""}
	 * @throws Exception
	 */
	String getMIUser(String p_mi_id) throws Exception;

	/**
	 * @Description: 第三方平台登录
	 * @Title: loginOther
	 * @param user_name 用户账户
	 * @param password 用户密码
	 * @return String
	 * @throws Exception
	 */
	String loginOther(String user_name, String password) throws Exception;

	/**
	 * @Description: 判断用户角色，动态返回角色
	 * @Title: getRoleId
	 * @param p_mi_id 用户编号
	 * @param user_name 用户名
	 * @param user_pass 用户密码
	 * @param phone 手机号
	 * @return
	 * @throws Exception Integer
	 */
	Integer getRoleId(String id_no, String name, String user_name, String user_pass, String phone) throws Exception;
	
	/**
	 * 第三方发送短找回密码
	 * @param name
	 * @return
	 */
	Map<String, String> passwordrecovery(String user_name);
	
	/**
	 * 找回密码
	 * @param name
	 * @param password
	 * @param onlyCode
	 * @param erify
	 * @return
	 */
	Map<String, String> callbackpassword(String name, String password, String onlyCode, String verify) ;
}