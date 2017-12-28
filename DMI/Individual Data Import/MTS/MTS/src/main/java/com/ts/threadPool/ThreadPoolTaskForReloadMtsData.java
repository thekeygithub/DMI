package com.ts.threadPool;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.ts.service.mts.MapCacheService;
import com.ts.util.AESSecurityUtil;
import com.ts.util.CommonUtils;
import com.ts.util.SeparatorConstant;
import com.ts.util.StringUtil;

import redis.clients.jedis.Jedis;

public class ThreadPoolTaskForReloadMtsData implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	private static Logger logger = Logger.getLogger(ThreadPoolTaskForReloadMtsData.class);
	// 保存任务所需要的数据
	private Map<String, Object> paramMap;
	private CountDownLatch latch;
	ResultSet data_rs = null;
	Statement stmt2 = null;
	Jedis redis = null;
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForReloadMtsData(CountDownLatch latch, Map<String, Object> paramMap) {
		this.paramMap = paramMap;
		this.latch = latch;
	}

	public synchronized String call() throws Exception {
		latch.countDown();
		String aresKey = (String) paramMap.get("aresKey");
		String emptyAes = (String) paramMap.get("emptyAes");
		String keyRule = (String) paramMap.get("keyRule");
		Integer matchDataTypeId = (Integer) paramMap.get("matchDataTypeId");
		String zlFlag = (String) paramMap.get("zlFlag");
		String valueRule = (String) paramMap.get("valueRule");
		String loadruleareaid = (String) paramMap.get("loadruleareaid");
		redis = (Jedis) paramMap.get("redis");
		if (keyRule.indexOf(SeparatorConstant.dataClassSeparator) != -1) {// 判断是否有聚类
			// 区分聚类code
			/**
			 * 加载区分聚类code 2017.2.8 liwei
			 */
			String data_class_code = null;
			data_class_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataClassSeparator) + 1,
					keyRule.indexOf(SeparatorConstant.dataTypeSeparator));
			/**
			 * 加载时区分标化类型code 2017.2.8 liwei
			 */
			String data_type_code = null;
			// 标准化类型ID
			data_type_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataTypeSeparator) + 1,
					keyRule.indexOf(SeparatorConstant.dataSeparator));
			// 拼装key
			// 校验key规则<<<<<<<<<<<<<<<<<<<<<<<<<多加
			if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
				// 未被删除的数据
				String tableMTSDATA = (String) paramMap.get("tableMTSDATA");
				StringBuffer sql = new StringBuffer(
						"select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR,AREA_ID from " + tableMTSDATA
								+ " where DATA_TYPE_ID = " + matchDataTypeId + " AND DEL_FLAG = 0 ");
				// 如果是第一次加载数据，需要选择未加载过的数据
				if ((Boolean) paramMap.get("isLoadData")) {
					sql.append(" AND LOAD_FLAG = 0");
				}
				if (StringUtil.isNotBlank(zlFlag)) {
					sql.append(" AND ZL_FLAG = " + zlFlag);
				}
				if (StringUtil.isNotBlank(paramMap.get("cityCode"))) {
					sql.append(" AND AREA_ID = " + paramMap.get("cityCode"));
				}
				/**
				 * 2017.2.9 批次号
				 */
				if (StringUtil.isNotBlank(paramMap.get("batchNo"))) {
					sql.append(" AND BATCH_NO = " + paramMap.get("batchNo"));
				}
				stmt2 = (Statement) paramMap.get("stmt2");
				data_rs = stmt2.executeQuery(sql.toString());
				data_rs.last(); // 移到最后一行
				int rowCount = data_rs.getRow(); // 得到当前行号，也就是记录数
				if (rowCount != 0) {
					data_rs.beforeFirst();// 如果还要用结果集，就把指针再移到初始化的位置
				}
				// 截取key规则
				String keyline = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
				/**
				 * valueline==================================
				 */
				String valueline = valueRule.substring(valueRule.indexOf(SeparatorConstant.dataSeparator));
				StringBuffer dataKey = new StringBuffer();
				StringBuffer dataValue = new StringBuffer();
				List resultlist = resultSetToList(data_rs);
				if ("number".equals(validateRE(keyline))) {// 是数字
					keyline = keyline.substring(1);
					valueline = valueline.substring(1);
					String[] keylines = keyline.split(SeparatorConstant.dataSeparator);
					/**
					 * valuelines==================================
					 */
					String[] valuelines = valueline.split(SeparatorConstant.dataSeparator);
					/**
					 * 循环 MTSDATA 结果集
					 */
					Map<String, String> countMap = new HashMap<String, String>();// 计数器
					Map<String, String> resultMap = new HashMap<String, String>();// 计数器
					for (int i = 0; i < resultlist.size(); i++) {
						Map map = (Map) resultlist.get(i);
						String areaId = (String) map.get("AREA_ID");
						if (StringUtil.isNotBlank(areaId) && StringUtil.isNotBlank(areaId)
								&& !areaId.equals(loadruleareaid)) {
							continue;
						}
						dataKey = new StringBuffer(areaId + SeparatorConstant.datajoinSeparator + data_class_code
								+ SeparatorConstant.datajoinSeparator + data_type_code
								+ SeparatorConstant.datajoinSeparator);
						dataValue = new StringBuffer("");
						String orig_data_str = (String) map.get("ORIG_DATA_STR");
						int data_id = (Integer) map.get("DATA_ID");
						if (orig_data_str.lastIndexOf("|") == (orig_data_str.length() - 1)) {
							orig_data_str += emptyAes;
						}
						logger.info("orig_data_str最后一个竖线位置==" + orig_data_str.lastIndexOf("|") + "=====大串长度-1"
								+ (orig_data_str.length() - 1));
						logger.info("key=====orig_data_str====" + orig_data_str);
						logger.info("key=====orig_data_str拆分长度===="
								+ orig_data_str.split(SeparatorConstant.dataValueSeparator).length);
						logger.info("key=====keylines长度====" + keylines.length);
						logger.info("dataid====" + data_id);
						for (int j = 0; j < keylines.length; j++) {
							String temp_str = orig_data_str.split(SeparatorConstant.dataValueSeparator)[Integer
									.parseInt(keylines[j])];
							if (temp_str != null && !"".equals(temp_str) && !emptyAes.equals(temp_str)) {
								temp_str = StringUtil.full2Half(AESSecurityUtil.decrypt(temp_str, aresKey))
										.toUpperCase();
								dataKey.append(temp_str + SeparatorConstant.datajoinSeparator);
							}
						}
						// 拼装KEY
						String resultKey = dataKey.toString().substring(0,
								dataKey.length() - SeparatorConstant.datajoinSeparator.length());
						// 拼装VALUE//valueRule
						// @ORIG_DATA_ID@ORIG_DATA_NAME@3
						// VALUE 重症肌无力|脑血管意外||1
						for (int j = 0; j < valuelines.length; j++) {
							dataValue.append(spellValue(SeparatorConstant.dataSeparator + valuelines[j], data_id, map,
									orig_data_str));
						}
						String resultValue = dataValue.substring(0,
								dataValue.length() - SeparatorConstant.datareturnSeparator.length());
						// 所有的key、 解密 半角 大写

						// 拼装redis
						// 药品类需要校验key是否重复,如果重复，本身连带重复的一律删除
						// 聚类为 药品制剂型 ,并且 能够获取到值，那么不加入redis内存，并保存
						if (StringUtil.isNotBlank(resultKey) && StringUtil.isNotBlank(resultValue)
								&& redis.exists(resultKey) && !resultValue.equals(redis.get(resultKey))) {
							countMap.put(resultKey, resultValue);
						} else {
							// key相同，value也相同：如果数据库有此key，先删除key，然后在添加，否则key拼接会出现错误数据
							if (redis.exists(resultKey)) {
								redis.del(resultKey);
							}
							redis.append(resultKey, resultValue);
						}
					}
					for (String key : countMap.keySet()) {
						redis.del(key);
					}
				} else if ("word".equals(validateRE(keyline))) {// 是字母
					String queryWord = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator) + 1);
					Map<String, String> countMap = new HashMap<String, String>();
					for (int i = 0; i < resultlist.size(); i++) {
						Map map = (Map) resultlist.get(i);
						String areaId = (String) map.get("AREA_ID");
						// 如果本体数据里的区域 与 加载规则中的区域 不一致 不进行加载
						if (StringUtil.isNotBlank(areaId) && StringUtil.isNotBlank(areaId)
								&& !areaId.equals(loadruleareaid)) {
							continue;
						}
						dataKey = new StringBuffer(areaId + SeparatorConstant.datajoinSeparator + data_class_code
								+ SeparatorConstant.datajoinSeparator + data_type_code
								+ SeparatorConstant.datajoinSeparator);
						dataValue = new StringBuffer("");
						// 暂时定为string型
						String keyword = (String) map.get("" + queryWord + "");
						String orig_data_str = (String) map.get("ORIG_DATA_STR");
						int data_id = (Integer) map.get("DATA_ID");
						if (StringUtil.isNotBlank(keyword)) {
							keyword = StringUtil.full2Half(AESSecurityUtil.decrypt(keyword, aresKey)).toUpperCase();
						} else {
							continue;
						}
						dataKey.append(keyword + SeparatorConstant.datajoinSeparator);
						dataValue.append(spellValue(valueRule, data_id, map, orig_data_str));
						logger.info("dataValue======" + dataValue);
						String dataValueStr = dataValue.toString();
						String resultKey = dataKey.toString().substring(0,
								dataKey.length() - SeparatorConstant.datajoinSeparator.length());
						String resultValue = dataValueStr.substring(0,
								dataValueStr.length() - SeparatorConstant.datareturnSeparator.length());
						// 拼装redis
						// 药品类需要校验key是否重复,如果重复，本身连带重复的一律删除
						// 聚类为 药品制剂型 ,并且 能够获取到值，那么不加入redis内存，并保存
						//// key相同，value不同：如果查到redis内存中有此key，并且 value值与
						// redis 获取的value值 不同 ，删掉key
						if (StringUtil.isNotBlank(resultKey) && StringUtil.isNotBlank(resultValue)
								&& redis.exists(resultKey) && !resultValue.equals(redis.get(resultKey))) {
							countMap.put(resultKey, resultValue);
						} else {
							// key相同，value也相同：如果数据库有此key，先删除key，然后在添加，否则key拼接会出现错误数据
							if (redis.exists(resultKey)) {
								redis.del(resultKey);
							}
							logger.info("redis==key ===" + resultKey + "====resultValue" + resultValue);
							redis.append(resultKey, resultValue);
						}
					}
					for (String key : countMap.keySet()) {
						// map.keySet()返回的是所有key的值
						redis.del(key);
					}
				}
			} else {
				logger.info("此规则没有配置匹配原始数据的规则!!~~");
			}
		} else {
			logger.info("此规则没有配置匹配数据聚类的规则!!~~");
		}

		return "true";
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
	public static String spellValue(String valueRule, int data_id, Map map, String orig_data_str) throws SQLException {
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
	public static StringBuffer getNumberValueOrOldData(String valueRule, Map map, int data_id, String orig_data_str) {
		StringBuffer dataValue = new StringBuffer();
		String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
		try {
			String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
			if (orig_data_str.lastIndexOf("|") == (orig_data_str.length() - 1)) {
				orig_data_str += emptyAes;
			}
			if ("number".equals(validateRE(valueRule))) {
				valueRule = valueRule.substring(1);
				String[] lines = valueRule.split(SeparatorConstant.dataSeparator);
				if (orig_data_str != null) {
					for (int j = 0; j < lines.length; j++) {
						/*
						 * logger.info("value规则的长度=====" + lines.length +
						 * "==========当前索引位置===========" + j + "===========" +
						 * lines[j]); logger.info("orig_data_str===========" +
						 * orig_data_str);
						 * logger.info("orig_data_name===========" +
						 * map.get("ORIG_DATA_NAME"));
						 */
						String[] orig_data_strs = orig_data_str.split(SeparatorConstant.dataValueSeparator);
						// logger.info("orig_data_str数组长度===========" +
						// orig_data_strs.length);
						String tempVal = orig_data_strs[Integer.parseInt(lines[j])];
						tempVal = AESSecurityUtil.decrypt(tempVal, aresKey);
						dataValue.append(tempVal + SeparatorConstant.datareturnSeparator);
						// 拼装value
					}
				}
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
						queryWord = AESSecurityUtil.decrypt(queryWord, aresKey);
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

}
