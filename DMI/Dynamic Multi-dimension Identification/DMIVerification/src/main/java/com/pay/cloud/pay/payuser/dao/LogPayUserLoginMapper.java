package com.pay.cloud.pay.payuser.dao;

import com.pay.cloud.pay.payuser.entity.LogPayUserLogin;

public interface LogPayUserLoginMapper {

	/**
	 * 
	 * @Description: 记录登录日记
	 * @Title: insert 
	 * @param record
	 * @return int
	 */
    int insert(LogPayUserLogin record);
    
    /**
	 * 
	 * @Description: 获取某个账户最后一次登录信息
	 * @Title: selectByActIdLastLogin 
	 * @param actId
	 * @return LogPayUserLogin
	 * @throws Exception 
	 */
	LogPayUserLogin selectByActIdLastLogin(Long actId) throws Exception;
}