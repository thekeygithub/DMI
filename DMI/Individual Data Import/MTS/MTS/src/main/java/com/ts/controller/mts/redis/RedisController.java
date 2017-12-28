package com.ts.controller.mts.redis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.base.BaseController;
import com.ts.entity.mts.MtsData;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.service.mts.matchrule.MtsDictManger;
import com.ts.service.mts.mtsdata.MtsDataManager;
import com.ts.service.mts.mtsdict.MtsDictManager;
import com.ts.service.system.role.RoleManager;
import com.ts.util.AESSecurityUtil;
import com.ts.util.CommonUtils;
import com.ts.util.ConnectionPoolManager;
import com.ts.util.DBinit;
import com.ts.util.IConnectionPool;
import com.ts.util.PageData;
import com.ts.util.QStringUtil;
import com.ts.util.SeparatorConstant;
import com.ts.util.SpringContextUtils;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 
 * @类名称: RedisController
 * @类描述: Redis远程调用
 * @作者:李巍
 * @创建时间:2016年12月8日 下午12:51:32
 */
@Controller
@RequestMapping(value = "/redis")
public class RedisController extends BaseController {
	private static Logger logger = Logger.getLogger(RedisController.class);
	String menuUrl = "mtsDict/listMtsDict.do"; // 菜单地址(权限用)
	@Resource(name = "mtsDictService")
	private MtsDictManager mtsDictService;
	@Resource(name = "roleService")
	private RoleManager roleService;
	@Resource(name = "DictMtsService")
	private MtsDictManger dictMtsService;
	@Resource(name = "DataClassService")
	private DataClassManger dataClassService;
	@Resource(name = "DataTypeService")
	private DataTypeManger dataTypeService;
	@Resource(name = "mtsDataService")
	private MtsDataManager mtsDataService;
	private ResultSet data_load_rule_rs = null;
	private ResultSet data_rs = null;
	private ResultSet data_rs2 = null;
	private Statement stmt = null;
	private Statement stmt2 = null;
	private Connection conn = null;
	private static final String tableMTSDATA = "MTS_DATA";
	private static Jedis redis = null;
	static {
		try {
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			redis = new Jedis(host, port, 400000);
			redis.auth(pass);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ===============================Redis对外接口==================================

	@RequestMapping(value = "/updateMtsData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateMtsData(@RequestBody String toBeStandardStr) {
		try {
			JSONObject dealWithStrJSON = JSONObject.fromObject(toBeStandardStr);
			Object o = mtsDataService.selectOneByName(dealWithStrJSON.getString("ORIG_DATA_NAME"));
			if (o == null) {
				MtsData md = new MtsData();
				//String mdataid = this.mtsDataService.maxData();
				//int Lid = Integer.parseInt(mdataid);
				//Lid++;
//				pd.put("DATA_ID", String.valueOf(Lid));
				md.setDATA_TYPE_ID(String.valueOf(Double.valueOf(dealWithStrJSON.getString("DATA_TYPE_ID")).intValue()));
				//pd.put("DATA_TYPE_ID", Double.valueOf(dealWithStrJSON.getString("DATA_TYPE_ID")).intValue());
				String aresKey = CommonUtils.getPropValue("redis.properties", "redis.aresKey");
				if (!QStringUtil.isEmpty(dealWithStrJSON.getString("ORIG_DATA_ID"))) {
					String bytes1 = AESSecurityUtil.encrypt(dealWithStrJSON.getString("ORIG_DATA_ID"), aresKey);
//					pd.put("ORIG_DATA_ID", bytes1);
					md.setORIG_DATA_ID(bytes1);
				} else {
//					pd.put("ORIG_DATA_ID", null);
					md.setORIG_DATA_ID(null);
				}
				if (!QStringUtil.isEmpty(dealWithStrJSON.getString("ORIG_DATA_NAME"))) {
					String bytes2 = AESSecurityUtil.encrypt(dealWithStrJSON.getString("ORIG_DATA_NAME"), aresKey);
//					pd.put("ORIG_DATA_NAME", bytes2);
					md.setORIG_DATA_NAME(bytes2);
				} else {
					md.setORIG_DATA_NAME(null);
//					pd.put("ORIG_DATA_NAME", null);
				}
				String ORIG_DATA_STR = dealWithStrJSON.getString("ORIG_DATA_STR");
				// 数据AES加密
				String[] strs = ORIG_DATA_STR.split("\\|");
				String mtspass = "";
				for (int j = 0; j < strs.length; j++) {
					String bytes = AESSecurityUtil.encrypt(strs[j], aresKey);
					mtspass += bytes + "|";
				}
				mtspass = mtspass.substring(0, mtspass.length() - 1);
//				pd.put("ORIG_DATA_STR", mtspass);
				md.setORIG_DATA_STR(mtspass);
				String zl_flag = dealWithStrJSON.getString("ZL_FLAG");
				if (StringUtil.isNotBlank(zl_flag)) {
					double d = Double.valueOf(zl_flag);
					md.setZL_FLAG(String.valueOf((int) d));
				}
				// 区域代码 均为易保本体
//				pd.put("AREA_ID", 100000);
				md.setAREA_ID(String.valueOf(100000));
				//pd.put("BATCH_NO", PrimaryGenerater.getInstance().generaterNextNumber(maxBatchNo));
				// 删除标识 0 未删除 、 1 已删除
//				pd.put("DEL_FLAG", 0);
				md.setDEL_FLAG("0");
				md.setLOAD_FLAG("0");
				md.setIMP_DATE(new Date());
//				pd.put("IMP_DATE", new Date());
				return this.mtsDataService.saveMtsData(md);
//				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// =============================重载接口==============================
	@RequestMapping(value = "/reloadRedisData")
	@ResponseBody
	public void reloadRedisData() throws Exception {
		new Thread(new DataReloadThread()).start();
	}

	// =============================是否重载完毕==============================

	/**
	 * 
	 * @方法名称: size
	 * @功能描述: 获取redis数据库总记录数
	 * @作者:李巍
	 * @创建时间:2016年12月7日 下午3:59:49
	 * @return Long
	 */
	@SuppressWarnings("resource")
	public Long getDBsize() {
		Jedis redis = null;
		try {
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			redis = new Jedis(host, port, 400000);
			redis.auth(pass);
			return redis.dbSize();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return 0L;
		}
	}

	/**
	 * 
	 * @方法名称: isRedisLoaded
	 * @功能描述: Redis是否加载完成
	 * @作者:李巍
	 * @创建时间:2016年12月7日 下午4:47:32
	 * @return boolean
	 * @throws Exception
	 */
	@RequestMapping(value = "/isRedisLoaded")
	@ResponseBody
	public boolean isRedisLoaded() throws Exception {
		Long bsize = redis.dbSize();
		Thread.sleep(1000);
		Long esize = redis.dbSize();
		if (bsize.longValue() == esize.longValue()) {
			return true;
		} else {
			return false;
		}
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
	public static StringBuffer getNumberValueOrOldData(String valueRule, ResultSet rs, String data_id,
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
				String[] queryTmpWords = queryTmpWord.split(",");
				String queryTmpWords2 = "";
				for (int i = 0; i < queryTmpWords.length; i++) {
					queryTmpWords2 = queryTmpWords[i];
					queryWord = rs.getString("" + queryTmpWords2 + "");
					// 拼装value
					dataValue.append(queryWord + SeparatorConstant.datareturnSeparator);
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
	public static String spellValue(String valueRule, String data_id, ResultSet rs, String orig_data_str)
			throws SQLException {
		StringBuffer dataValue = new StringBuffer();
		// 判断数字下标 还是 原始数据名称
		dataValue = getNumberValueOrOldData(valueRule, rs, data_id, orig_data_str);
		// 判断MD5加密,这块也要加一个判断MD5的正则表达式 ???????
		if (valueRule.indexOf(SeparatorConstant.md5ValueSeparator) != -1) {
			valueRule = valueRule.substring(valueRule.indexOf(SeparatorConstant.md5ValueSeparator) + 3,
					valueRule.indexOf(")") + 1);
			if (valueRule.indexOf(SeparatorConstant.md5joinSeparator) != -1) {
				String[] valueRules = valueRule.split(SeparatorConstant.md5joinSeparator);
				for (int i = 0; i < valueRules.length; i++) {
					dataValue.append(getNumberValueOrOldData(valueRules[i], rs, data_id, orig_data_str));
				}
			} else {
				dataValue = getNumberValueOrOldData(valueRule, rs, data_id, orig_data_str);
			}
		}
		return dataValue.toString();
	}

	private static int getRsCount(ResultSet rs) throws Exception {
		rs.last(); // 移到最后一行
		int rowCount = rs.getRow(); // 得到当前行号，也就是记录数
		rs.beforeFirst(); // 如果还要用结果集，就把指针再移到初始化的位置
		return rowCount;
	}

	class DataReloadThread implements Runnable {
		@Override
		public void run() {
			/*
			 * KEY生成规则 数据聚类 ——标化——原始数据名称 先判断有几个$ ，根据 VALUE生成规则 ，取本体表中
			 * 原始整合串的第一个字符串
			 */
			try {
				// 延迟加载
				// 清空redis
				redis.flushDB();
				Long size = redis.dbSize();
				logger.info("========current DBSIZE====" + size + "==========");
				IConnectionPool pool = initDB();
				conn = pool.getConnection();
				stmt = conn.createStatement();
				stmt2 = conn.createStatement();
				// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID
				data_load_rule_rs = stmt.executeQuery("select * from MTS_LOAD_RULE_DETAIL");
				IRedisUtil redisUtil = (IRedisUtil) SpringContextUtils.getBean("redisUtil");
				String emptyAes = CommonUtils.getPropValue("redis.properties", "redis.emptyAes");
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
						String data_type_code = keyRule.substring(
								keyRule.indexOf(SeparatorConstant.dataTypeSeparator) + 1,
								keyRule.indexOf(SeparatorConstant.dataSeparator));
						// 拼装key
						// 校验key规则<<<<<<<<<<<<<<<<<<<<<<<<<多加
						if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
							data_rs = stmt2
									.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
											+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId);
							String keyline = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
							/**
							 * valueline==================================
							 */
							String valueline = valueRule.substring(valueRule.indexOf(SeparatorConstant.dataSeparator));
							// 多线程输出
							StringBuffer dataKey = new StringBuffer();
							StringBuffer dataValue = new StringBuffer();
							// List resultlist = resultSetToList(data_rs);
							if ("number".equals(validateRE(keyline))) {// 是数字
								keyline = keyline.substring(1);
								valueline = valueline.substring(1);
								String[] keylines = keyline.split(SeparatorConstant.dataSeparator);
								/**
								 * valuelines==================================
								 */
								String[] valuelines = valueline.split(SeparatorConstant.dataSeparator);

								while (data_rs.next()) {
									dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator
											+ data_type_code + SeparatorConstant.datajoinSeparator);
									dataValue = new StringBuffer("");
									String orig_data_str = data_rs.getString("ORIG_DATA_STR");

									String data_id = data_rs.getString("DATA_ID");
									for (int j = 0; j < keylines.length; j++) {
										String temp_str = orig_data_str.split(
												SeparatorConstant.dataValueSeparator)[Integer.parseInt(keylines[j])];
										if (temp_str != null && !"".equals(temp_str) && !emptyAes.equals(temp_str)) {
											dataKey.append(temp_str + SeparatorConstant.datajoinSeparator);
										}
									}
									// 拼装KEY
									String resultKey = dataKey.toString().substring(0,
											dataKey.length() - SeparatorConstant.datajoinSeparator.length());
									// 拼装VALUE//valueRule
									// @ORIG_DATA_ID@ORIG_DATA_NAME@3 VALUE
									// 重症肌无力|脑血管意外||1
									for (int j = 0; j < valuelines.length; j++) {
										dataValue.append(spellValue(SeparatorConstant.dataSeparator + valuelines[j],
												data_id, data_rs, orig_data_str));
										//System.out.println(dataValue);
									}
									String resultValue = dataValue.substring(0,
											dataValue.length() - SeparatorConstant.datareturnSeparator.length());
									// 拼装redis
									redisUtil.set(RedisKeys.SYSMTS, resultKey, resultValue);
								}
							} else if ("word".equals(validateRE(keyline))) {// 是字母
								String queryWord = keyRule
										.substring(keyRule.indexOf(SeparatorConstant.dataSeparator) + 1);
								while (data_rs.next()) {
									dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator
											+ data_type_code + SeparatorConstant.datajoinSeparator);
									dataValue = new StringBuffer("");
									// 暂时定为string型
									String keyword = data_rs.getString("" + queryWord + "");
									String orig_data_str = data_rs.getString("ORIG_DATA_STR");
									String data_id = data_rs.getString("DATA_ID");
									dataKey.append(keyword + SeparatorConstant.datajoinSeparator);
									dataValue.append(spellValue(valueRule, data_id, data_rs, orig_data_str));
									logger.info("dataValue======" + dataValue);
									String dataValueStr = dataValue.toString();
									String resultValue = dataValueStr.substring(0,
											dataValueStr.length() - SeparatorConstant.datareturnSeparator.length());
									String resultKey = dataKey.toString().substring(0,
											dataKey.length() - SeparatorConstant.datajoinSeparator.length());
									// 拼装redis？？？？？
									logger.info("========value======" + resultValue + "===data_id" + data_id);
									redisUtil.set(RedisKeys.SYSMTS, resultKey, resultValue);
								}
							}
						} else {
							logger.info("此规则没有配置匹配原始数据的规则!!~~");
						}
					} else {
						logger.info("此规则没有配置匹配数据聚类的规则!!~~");
					}
				}
				logger.info("恭喜，数据装载正常结束!!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
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

		}

	}
	
	public static void main(String[] args){
		String host = CommonUtils.getPropValue("redis.properties", "redis.host");
		System.out.println("host-======>>>"+host);
	}
}
