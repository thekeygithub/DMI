package com.ts.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ts.util.CommonUtils;
import com.ts.util.SeparatorConstant;
import com.ts.util.SpringContextUtils;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

/**
 * 
 * @类名称: PyHelloAppModule
 * @类描述: 多线程匹配
 * @作者:李巍
 * @创建时间:2016年9月28日 下午2:56:06
 */
public class PyHelloAppModule implements Runnable {
	private static Logger logger = Logger.getLogger(PyHelloAppModule.class);
	private String keyline = null;
	private String valueline = null;
	private ResultSet data_rs = null;
	private String data_class_code = null;
	private String data_type_code = null;
	private String keyRule = null;
	private String valueRule = null;
	private Connection conn = null;
	private List resultlist = new ArrayList();

	public void run() {
		try {
			String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
			IRedisUtil redisUtil = (IRedisUtil) SpringContextUtils.getBean("redisUtil");
			StringBuffer dataKey = new StringBuffer();
			StringBuffer dataValue = new StringBuffer();
			if ("number".equals(validateRE(keyline))) {// 是数字
				keyline = keyline.substring(1);
				valueline = valueline.substring(1);
				String[] keylines = keyline.split(SeparatorConstant.dataSeparator);
				/**
				 * valuelines==================================
				 */
				String[] valuelines = valueline.split(SeparatorConstant.dataSeparator);
				for (int i = 0; i < resultlist.size(); i++) {
					dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator + data_type_code
							+ SeparatorConstant.datajoinSeparator);
					dataValue = new StringBuffer("");
					Map map = (Map) resultlist.get(i);
					String orig_data_str = (String) map.get("ORIG_DATA_STR");
					String data_id = (String) map.get("DATA_ID");
					for (int j = 0; j < keylines.length; j++) {
						String temp_str = orig_data_str.split(SeparatorConstant.dataValueSeparator)[Integer
								.parseInt(keylines[j])];
						if (temp_str != null && !"".equals(temp_str) && !emptyAes.equals(temp_str)) {
							dataKey.append(temp_str + SeparatorConstant.datajoinSeparator);
						}
					}
					// 拼装KEY
					String resultKey = dataKey.toString().substring(0, dataKey.length() - SeparatorConstant.datajoinSeparator.length());
					// 拼装VALUE//valueRule @ORIG_DATA_ID@ORIG_DATA_NAME@3 VALUE
					// 重症肌无力|脑血管意外||1
					for (int j = 0; j < valuelines.length; j++) {
						dataValue.append(spellValue(SeparatorConstant.dataSeparator + valuelines[j], data_id, map,
								orig_data_str));
						//System.out.println(dataValue);
					}
					String resultValue = dataValue.substring(0, dataValue.length() - SeparatorConstant.datareturnSeparator.length());
					// 拼装redis
					redisUtil.set(RedisKeys.SYSMTS, resultKey, resultValue);

				}
			} else if ("word".equals(validateRE(keyline))) {// 是字母
				String queryWord = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator) + 1);
				for (int i = 0; i < resultlist.size(); i++) {
					dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator + data_type_code
							+ SeparatorConstant.datajoinSeparator);
					dataValue = new StringBuffer("");
					// 暂时定为string型
					Map map = (Map) resultlist.get(i);
					String keyword = (String) map.get("" + queryWord + "");
					String orig_data_str = (String) map.get("ORIG_DATA_STR");
					String data_id = (String) map.get("DATA_ID");
					dataKey.append(keyword + SeparatorConstant.datajoinSeparator);
					dataValue.append(spellValue(valueRule, data_id, map, orig_data_str));
					logger.info("dataValue======" + dataValue);
					String dataValueStr = dataValue.toString();
					String resultValue = dataValueStr.substring(0, dataValueStr.length() - SeparatorConstant.datareturnSeparator.length());
					String resultKey = dataKey.toString().substring(0, dataKey.length() - SeparatorConstant.datajoinSeparator.length());
					// 拼装redis？？？？？
					logger.info("========value======" + resultValue + "===data_id" + data_id);
					redisUtil.set(RedisKeys.SYSMTS, resultKey, resultValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getData_rs() {

		return data_rs;
	}

	public void setData_rs(ResultSet data_rs) {

		this.data_rs = data_rs;
	}

	public String getData_class_code() {

		return data_class_code;
	}

	public String getKeyline() {

		return keyline;
	}

	public void setKeyline(String keyline) {

		this.keyline = keyline;
	}

	public String getValueline() {

		return valueline;
	}

	public void setValueline(String valueline) {

		this.valueline = valueline;
	}

	public void setData_class_code(String data_class_code) {

		this.data_class_code = data_class_code;
	}

	public String getData_type_code() {

		return data_type_code;
	}

	public void setData_type_code(String data_type_code) {

		this.data_type_code = data_type_code;
	}

	public String getKeyRule() {

		return keyRule;
	}

	public void setKeyRule(String keyRule) {

		this.keyRule = keyRule;
	}

	public String getValueRule() {

		return valueRule;
	}

	public void setValueRule(String valueRule) {

		this.valueRule = valueRule;
	}

	public Connection getConn() {

		return conn;
	}

	public void setConn(Connection conn) {

		this.conn = conn;
	}

	public List getResultlist() {

		return resultlist;
	}

	public void setResultlist(List resultlist) {

		this.resultlist = resultlist;
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
										+ SeparatorConstant.datareturnSeparator);
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
						dataValue.append(queryWord + SeparatorConstant.datareturnSeparator);
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