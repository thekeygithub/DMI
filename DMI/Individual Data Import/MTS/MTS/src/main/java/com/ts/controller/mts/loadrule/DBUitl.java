
package com.ts.controller.mts.loadrule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class DBUitl {

	public static void main(String[] args) {
		try {
			insertBatch("201704110020", "9", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertBatch(String batchNo, String dataTypeId, Integer zlFlag) throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://10.10.50.15:3306/mts";
		String username = "root";
		String password = "tsmspwd2016";
		Connection conn = null;
		Long begin = new Date().getTime();
		ResultSet rs;
		ResultSet urs;
		Statement stmt;
		Statement stmt2;
		Class.forName(driver); // classLoader,加载对应驱动
		conn = (Connection) DriverManager.getConnection(url, username, password);
		stmt = conn.createStatement();
		stmt2 = conn.createStatement();
		String tableName = "MTS_DATA";
		// 获取id最大值
		StringBuffer uniqueSql = new StringBuffer(
				"SELECT IFNULL(MAX(CONVERT(DATA_ID,SIGNED)),0) maxid FROM " + tableName + "");
		urs = stmt2.executeQuery(uniqueSql.toString());
		String maxid = null;
		if (urs.next()) {
			maxid = urs.getString("maxid");
		}
		int count = Integer.parseInt(maxid);
		StringBuffer querySql = new StringBuffer(
				"SELECT DATA_ID, DATA_TYPE_ID, ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR,ZL_FLAG,AREA_ID,BATCH_NO,DEL_FLAG,IMP_DATE FROM "
						+ tableName + " WHERE DATA_TYPE_ID =5 AND ZL_FLAG = 2");
		rs = stmt.executeQuery(querySql.toString());
		// 开时时间

		// sql前缀
		String prefix = "INSERT INTO " + tableName + " (DATA_ID, DATA_TYPE_ID, ORIG_DATA_ID,ORIG_DATA_NAME"
				+ ",ORIG_DATA_STR,ZL_FLAG,AREA_ID,BATCH_NO,DEL_FLAG,IMP_DATE) VALUES ";

		// 保存sql后缀
		StringBuffer suffix = new StringBuffer();
		// 设置事务为非自动提交
		conn.setAutoCommit(false);
		// Statement st = conn.createStatement();
		// 比起st，pst会更好些
		PreparedStatement pst = conn.prepareStatement("");
		// 外层循环，总提交事务次数
		// 第次提交步长
		while (rs.next()) {
			// 构建sql后缀
			String ORIG_DATA_ID = rs.getString("ORIG_DATA_ID");
			String ORIG_DATA_NAME = rs.getString("ORIG_DATA_NAME");
			String ORIG_DATA_STR = rs.getString("ORIG_DATA_STR");
			String AREA_ID = rs.getString("AREA_ID");
			suffix.append("(" + (++count) + ", " + dataTypeId + ",'" + ORIG_DATA_ID + "','" + ORIG_DATA_NAME + "','"
					+ ORIG_DATA_STR + "'," + zlFlag + "," + AREA_ID + "," + batchNo + ",0,SYSDATE()),");
		}
		// 构建完整sql
		String sql = prefix + suffix.substring(0, suffix.length() - 1);
		// 添加执行sql
		pst.addBatch(sql);
		// 执行操作
		pst.executeBatch();
		// 提交事务
		conn.commit();
		// 清空上一次添加的数据
		suffix = new StringBuffer();
		// 头等连接
		pst.close();
		conn.close();

		// 结束时间
		Long end = new Date().getTime();
		// 耗时
		System.out.println("cast : " + (end - begin) / 1000 + " ms");
	}
}
