package com.ebmi.std.personal.dao;

public interface PersonalDao {
	/**
	 * @Description: MI通过用户身份证号取用户
	 * @Title: getMIUserByUserIdNo
	 * @param id_no 身份证号
	 * @return String
	 * @throws Exception
	 */
	String getMIUserByUserIdNo(String id_no) throws Exception;

	/**
	 * @Description: MI通过用户社保卡号取用户
	 * @Title: getMIUserByUserSSCard
	 * @param sb_no 社保卡号
	 * @return String
	 * @throws Exception
	 */
	String getMIUserByUserSSCard(String sb_no) throws Exception;

	/**
	 * @Description: MI通过用户编号取用户
	 * @Title: getMIUser
	 * @param p_mi_id 用户编号
	 * @return String {真实姓名:"", 社保卡号 :"",身份证号 :""}
	 * @throws Exception
	 */
	String getMIUser(String p_mi_id) throws Exception;

	/**
	 * @Description: 注册--社保信息验证
	 * @Title: validateSocialsecurityInfo
	 * @param user_name 其他验证账户名
	 * @param user_pass 其他验证账户密码
	 * @param name 姓名
	 * @param id_no 身份证
	 * @param sb_no 社保卡
	 * @param sb_pass 社保卡密码
	 * @return String 验证通过：返回p_mi_id ，验证不通过：返回空
	 * @throws Exception
	 */
	String validateSocialsecurityInfo(String name, String id_no, String sb_no, String sb_pass) throws Exception;

}
