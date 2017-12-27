package com.ebmi.std.common.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.core.db.jdbc.JdbcService;

/**
 * 
 * @Description: 基础服务类
 * @ClassName: BaseDao
 * @author: zhengping.hu
 * @date: 2015年11月23日 下午5:08:01
 */
@Repository
public abstract class BaseDao {

	@Resource(name = "jdbcService")
	private JdbcService jdbcService;

	//@Resource(name = "jdbcServiceMI")
	private JdbcService jdbcServiceMI;

	public JdbcService getMIJdbcService() {
		return jdbcService;
	}

	public void setMIJdbcService(JdbcService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public JdbcService getJdbcService() {
		return jdbcService;
	}

	public void setJdbcService(JdbcService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public JdbcService getJdbcServiceMI() {
		return jdbcServiceMI;
	}

	public void setJdbcServiceMI(JdbcService jdbcServiceMI) {
		this.jdbcServiceMI = jdbcServiceMI;
	}
}