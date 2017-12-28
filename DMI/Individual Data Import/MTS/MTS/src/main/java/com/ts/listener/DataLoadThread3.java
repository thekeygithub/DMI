package com.ts.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.ts.util.CommonUtils;
import com.ts.util.ConnectionPoolManager;
import com.ts.util.DBinit;
import com.ts.util.IConnectionPool;
import com.ts.util.SeparatorConstant;
import com.ts.util.SpringContextUtils;
import com.ts.util.redis.IRedisUtil;
import redis.clients.jedis.Jedis;

/**
 * 
 * @类名称: PyHelloAppModule
 * @类描述: 多线程匹配
 * @作者:李巍
 * @创建时间:2016年9月28日 下午2:56:06
 */
public class DataLoadThread3 implements Runnable {
	private static Logger logger = Logger.getLogger(DataLoadThread.class);
	Long starttime = System.currentTimeMillis();
	// 每次重启服务器 ，需要清空数据
	ResultSet data_load_rule_rs = null;
	ResultSet data_rs = null;
	ResultSet data_rs2 = null;
	ResultSet data_class_rs = null;
	ResultSet data_type_rs = null;
	Statement stmt = null;
	Statement stmt2 = null;
	Statement stmt3 = null;
	Statement stmt4 = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	private static final String tableMTSDATA = "MTS_DATA";

	public void run() {
		/*
		 * KEY生成规则 数据聚类 ——标化——原始数据名称 先判断有几个$ ，根据 VALUE生成规则 ，取本体表中 原始整合串的第一个字符串
		 */
		try {
			// 延迟加载
			Thread.sleep(10000);
			IRedisUtil redisUtil = (IRedisUtil) SpringContextUtils.getBean("redisUtil");
			// 清空redis
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			Jedis redis = new Jedis(host, port, 400000);
			redis.auth(pass);
			redis.flushDB();
			IConnectionPool pool = initDB();
			conn = pool.getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			stmt4 = conn.createStatement();
			// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID
			data_load_rule_rs = stmt.executeQuery("select * from MTS_LOAD_RULE_DETAIL");
			while (data_load_rule_rs.next()) {
				String keyRule = data_load_rule_rs.getString("KEY_GEN_RULE");
				String valueRule = data_load_rule_rs.getString("VALUE_GEN_RULE");
				if (keyRule.indexOf(SeparatorConstant.dataClassSeparator) != -1) {// 判断是否有聚类
					// 标准化类型ID
					int matchDataTypeId = data_load_rule_rs.getInt("DATA_TYPE_ID");
					// 查询聚类code
					String data_class_code = keyRule.substring(
							keyRule.indexOf(SeparatorConstant.dataClassSeparator) + 1,
							keyRule.indexOf(SeparatorConstant.dataTypeSeparator));
					// 查询标化类型code
					//   /7D1b55/749jybBx43HnHw==@#&VmzIH9Hb1IIurnx7zdI5tQ09C+8X8KNopscfhTxdSw0=
					String data_type_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataTypeSeparator) + 1,
							keyRule.indexOf(SeparatorConstant.dataSeparator));
					// 拼装key
					// 校验key规则
					if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
						data_rs = stmt2.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
								+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId);
						String keyline = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
						String valueline = valueRule.substring(valueRule.indexOf(SeparatorConstant.dataSeparator));
						// 多线程输出
						data_rs.last(); // 移到最后一行
						int rowCount = data_rs.getRow(); // 得到当前行号，也就是记录数
						// 线程数
						int people = 8;
						int everyrow = rowCount % people == 0 ? rowCount / people : rowCount / people + 1;
						Thread[] thread = new Thread[people];
						int count = 0;
						for (int i = 0; i < people; i++) {
							PyHelloAppModule myThread = new PyHelloAppModule();
							myThread.setConn(conn);
							myThread.setData_class_code(data_class_code);
							myThread.setData_type_code(data_type_code);
							myThread.setKeyRule(keyRule);
							myThread.setKeyline(keyline);
							myThread.setValueline(valueline);
							myThread.setValueRule(valueRule);
							if (i == people - 1) {
								data_rs2 = stmt3
										.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
												+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId + " limit "
												+ count + "," + rowCount);
							} else {
								data_rs2 = stmt4
										.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
												+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId + " limit "
												+ count + "," + (count + everyrow));
							}
							List resultlist = resultSetToList(data_rs2);
							myThread.setResultlist(resultlist);
							thread[i] = new Thread(myThread);
							thread[i].start();
							data_rs2.last(); // 移到最后一行
							logger.info("单次处理数据数量=======>>" + data_rs2.getRow()); // 得到当前行号，也就是记录数
							count += everyrow;
						}
					} else {
						logger.info("此规则没有配置匹配原始数据的规则!!~~");
					}
				} else {
					logger.info("此规则没有配置匹配数据聚类的规则!!~~");
				}
			}
			logger.error("MTS本体数据加载用时======" + formatDuring(System.currentTimeMillis() - starttime));
			logger.info("恭喜，数据装载正常结束!!");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (data_class_rs != null) {
					data_class_rs.close();
					data_class_rs = null;
				}
				if (data_type_rs != null) {
					data_type_rs.close();
					data_type_rs = null;
				}
				if (data_rs != null) {
					data_rs.close();
					data_rs = null;
				}
				if (data_rs2 != null) {
					data_rs2.close();
					data_rs2 = null;
				}
				if (data_load_rule_rs != null) {
					data_load_rule_rs.close();
					data_load_rule_rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.error("Redis数据加载用时======" + formatDuring(System.currentTimeMillis() - starttime));
	}

	public static IConnectionPool initDB() {
		Thread dbSKThread = DBinit.dbSKInit();
		dbSKThread.start();
		try {
			dbSKThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IConnectionPool pool;
		pool = ConnectionPoolManager.getInstance().getPool("skPool");
		return pool;
	}

	public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
			// System.out.println("list:" + list.toString());
		}
		return list;
	}

	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
	}

	/**
	 * 
	 * @方法名称: validateRE
	 * @功能描述: 正则匹配数字下标 或是数据名称
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:16
	 * @param line
	 * @return String
	 */
	public static String validateRE(String line) {
		String pattern = "(@\\d)+";
		String pattern2 = "(@\\D)+";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(line);
		// 创建 Pattern 对象
		Pattern r2 = Pattern.compile(pattern2);
		// 现在创建 matcher 对象s
		Matcher m2 = r2.matcher(line);
		if (m.find()) {
			return "number";
		} else if (m2.find()) {
			return "word";
		} else {
			logger.info(line + "===未知匹配类型===!!");
			return "";
		}
	}

	/**
	 * 
	 * @方法名称: getNumberValueOrOldData
	 * @功能描述: 匹配value
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:05
	 * @param valueRule
	 * @param map
	 * @param data_id
	 * @param orig_data_str
	 * @return StringBuffer
	 */
	public static StringBuffer getNumberValueOrOldData(String valueRule, Map map, String data_id,
			String orig_data_str) {
		StringBuffer dataValue = new StringBuffer();
		try {
			if ("number".equals(validateRE(valueRule))) {
				valueRule = valueRule.substring(1);
				String[] lines = valueRule.split(SeparatorConstant.dataSeparator);
				if (orig_data_str != null) {
					for (int j = 0; j < lines.length; j++) {
						logger.info("lines===========" + j + "===========" + lines[j]);
						logger.info("orig_data_str===========" + orig_data_str);
						dataValue.append(
								orig_data_str.split(SeparatorConstant.dataValueSeparator)[Integer.parseInt(lines[j])]
										+ SeparatorConstant.datajoinSeparator);
						// 拼装value
					}
				}
				// return dataValue.toString();
			}
			if ("word".equals(validateRE(valueRule))) {
				String queryWord = valueRule.substring(1);
				String[] queryWords = queryWord.split(SeparatorConstant.dataSeparator);
				String queryTmpWord = "";
				for (int i = 0; i < queryWords.length; i++) {
					queryTmpWord += queryWords[i] + ",";
				}
				queryTmpWord = queryTmpWord.substring(0, queryTmpWord.length() - 1);
				if (map.size() > 0) {
					String[] queryTmpWords = queryTmpWord.split(",");
					String queryTmpWords2 = "";
					for (int i = 0; i < queryTmpWords.length; i++) {
						queryTmpWords2 = queryTmpWords[i];
						queryWord = (String) map.get("" + queryTmpWords2 + "");
						// 拼装value
						dataValue.append(queryWord + SeparatorConstant.datajoinSeparator);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataValue;
	}

	/**
	 * 装载数据聚类key
	 * 
	 * @param matchDataTypeId
	 * @param keyRule
	 * @param stmt
	 * @return
	 * @throws Exception
	 */
	public static String matchClassType(String keyRule, int matchDataTypeId, Connection conn) throws Exception {
		// 去中间表找出标化类型对应的聚类
		Statement stmt = conn.createStatement();
		String queryClassColumn = keyRule.substring(keyRule.indexOf("%") + 1, keyRule.indexOf("$"));
		ResultSet class_relation_rs = stmt.executeQuery("select " + queryClassColumn
				+ " from MTS_CLASS_RELATION r,MTS_DATA_CLASS c where r.DATA_CLASS_ID = c.DATA_CLASS_ID  and  DATA_TYPE_ID="
				+ matchDataTypeId);
		String data_class_code = "";
		try {
			if (class_relation_rs.next()) {
				data_class_code = class_relation_rs.getString("DATA_CLASS_CODE");
			} else {
				logger.info("标准化类型ID为" + matchDataTypeId + "无数据聚类项，请及时配置归属的数据聚类!!~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			class_relation_rs.close();
		}
		return data_class_code;
	}

	/*
	 * 装载标化类型key**
	 * 
	 * @param matchDataTypeId
	 * 
	 * @param keyRule
	 * 
	 * @param stmt
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private static String matchDataType(int matchDataTypeId, String keyRule, Connection conn) throws Exception {
		String queryTypeColumn = keyRule.substring(keyRule.indexOf("$") + 1, keyRule.indexOf("@"));
		Statement stmt = conn.createStatement();
		ResultSet data_type_rs = stmt.executeQuery(
				"select " + queryTypeColumn + " from MTS_DATA_TYPE where DATA_TYPE_ID=" + matchDataTypeId);
		String data_type_code = "";
		try {
			if (data_type_rs.next()) {
				data_type_code = data_type_rs.getString(queryTypeColumn);
				return data_type_code;
			} else {
				logger.info("此匹配规则无需匹配标准化类型配置项!!~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_type_rs.close();
		}
		return data_type_code;
	}

	/**
	 * 
	 * @方法名称: spellValue
	 * @功能描述: 根据value规则 组装VALUE值..包括下标、数据名称、MD5加密。
	 * @作者:李巍
	 * @创建时间:2016年9月26日 下午2:50:19
	 * @param valueRule
	 * @param data_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 *             String
	 */
	public static String spellValue(String valueRule, String data_id, Map map, String orig_data_str)
			throws SQLException {
		StringBuffer dataValue = new StringBuffer();
		ResultSet data_rs = null;
		// 判断数字下标 还是 原始数据名称
		dataValue = getNumberValueOrOldData(valueRule, map, data_id, orig_data_str);
		// 判断MD5加密,这块也要加一个判断MD5的正则表达式 ???????
		if (valueRule.indexOf(SeparatorConstant.md5ValueSeparator) != -1) {
			valueRule = valueRule.substring(valueRule.indexOf(SeparatorConstant.md5ValueSeparator) + 3,
					valueRule.indexOf(")") + 1);
			if (valueRule.indexOf(SeparatorConstant.md5joinSeparator) != -1) {
				String[] valueRules = valueRule.split(SeparatorConstant.md5joinSeparator);
				for (int i = 0; i < valueRules.length; i++) {
					dataValue.append(getNumberValueOrOldData(valueRules[i], map, data_id, orig_data_str));
				}
			} else {
				dataValue = getNumberValueOrOldData(valueRule, map, data_id, orig_data_str);
			}
		}
		return dataValue.toString();
	}

}