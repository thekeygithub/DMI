package com.aghit.task.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DbcpUtil {

	private static DbcpUtil du = new DbcpUtil();
	private DataSource DS;
	private Log log = LogFactory.getLog(DbcpUtil.class);

	public DbcpUtil() {
		try {
			this.init();
		} catch (IOException e) {
			log.error("初始化数据源失败," + e.getMessage());
		}
	}

	public static DbcpUtil getInstance() {
		return du;
	}

	private void init() throws IOException {

		Properties p = new Properties();
		p.load(DbcpUtil.class.getResourceAsStream("/jdbc.properties"));
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(p.getProperty("jdbc.driverClassName"));
		ds.setUsername(p.getProperty("jdbc.username"));
		ds.setPassword(p.getProperty("jdbc.password"));
		ds.setUrl(p.getProperty("jdbc.url"));
		DS = ds;
	}

	public DataSource getDataSource() {
		return DS;
	}

	/**
	 * 从数据源获得一个连接
	 * 
	 * @throws SQLException
	 */
	public Connection getConn() throws SQLException {

		try {
			return DS.getConnection();
		} catch (SQLException e) {
			throw new SQLException("获取数据库连接出错," + e.getMessage());
		}
	}

	public void shutdownDataSource() throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		bds.close();
	}

}
