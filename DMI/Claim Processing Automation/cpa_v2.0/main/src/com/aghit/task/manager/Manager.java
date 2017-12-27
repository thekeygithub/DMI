package com.aghit.task.manager;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.aghit.utils.DbcpUtil;
import com.framework.db.jdbc.JdbcOracleService;

public class Manager {

	
	private JdbcTemplate jdbcTemplate;
	private JdbcOracleService jdbcService;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	public Manager(){
		
		this.jdbcTemplate=new JdbcTemplate();
		this.jdbcTemplate.setDataSource(DbcpUtil.getInstance().getDataSource());
		
		this.jdbcService=new JdbcOracleService();
		this.jdbcService.setJdbcTemplate(this.jdbcTemplate);
		
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(DbcpUtil.getInstance().getDataSource());
	}

	public JdbcOracleService getJdbcService() {
		return jdbcService;
	}

	public void setJdbcService(JdbcOracleService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	/**
	 * 获取序列的下一个值
	 * @param seqName 序列名称
	 * @return
	 */
	public long getSeqNext(String seqName) throws Exception{
		
		long nextVal= -1;
		try{
			String sql = "SELECT " + seqName + ".nextval FROM dual";
			nextVal = this.jdbcTemplate.queryForLong(sql);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return nextVal;
	}
	
	
	
}
