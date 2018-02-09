package com.models.cloud.pay.payuser.dao;

import java.util.Map;

import com.models.cloud.pay.payuser.entity.PayUser;

public interface PayUserMapper {

    PayUser findPayUserInfo(PayUser payUser) throws Exception;
    
    /**
     * 
     * @Description: 通过手机号获取用户
     * @Title: findByPayUserPhone 
     * @param phone
     * @return PayUser
     */
    PayUser findByPayUserPhone(String phone) throws Exception;

    /**
     * 
     * @Description: 通过用户编号查询用户
     * @Title: findByPayUserId 
     * @param userId
     * @return PayUser
     * @throws Exception 
     */
	PayUser findByPayUserId(String userId) throws Exception;
	
    /**
     * 
     * @Description: 保存用户
	 * @Title: savePayUser 
	 * @param payUser 用户
     * @return int
     * @throws Exception 
     */
    public int savePayUser(PayUser payUser) throws Exception;

    /**
     * 更新登录账号
     * @param payUser
     * @return
     */
    public int updateUserCode(PayUser payUser);
    /**
     * 
     * @Description: 记录密码错误次数
     * @Title: updatePassErrorCount 
     * @param map
     * @return int
     * @throws Exception
     */
	int updatePassErrorCount(Map<String, Object> map) throws Exception;

	 /**
     * 
     * @Description: 设置用户登录密码
	 * @Title: restLoginPass 
	 * @param payUser 用户
     * @return int
     * @throws Exception 
     */
	int restLoginPass(PayUser u) throws Exception;

	/**
	 * 
	 * @Description: 更新最后登录时间
	 * @Title: updateLastLoginTime 
	 * @param payUser
	 * @return int
	 * @throws Exception
	 */
	int updateLastLoginTime(PayUser payUser) throws Exception;

	/**
	 * 
	 * @Description:  登出系统
	 * @Title: loginOut 
	 * @param payUserId
	 * @return int
	 * @throws Exception
	 */
	int loginOut(Long payUserId) throws Exception;

	/**
	 * 
	 * @Description: 通过用户账户编号查询用户
	 * @Title: findByPayUserActId 
	 * @param actId 用户账户编号
	 * @return PayUser
	 * @throws Exception 
	 */
	PayUser findByPayUserActId(String actId) throws Exception;

}