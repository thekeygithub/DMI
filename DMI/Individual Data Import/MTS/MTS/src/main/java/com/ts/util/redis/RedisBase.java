
package com.ts.util.redis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ts.util.ConnectionPoolManager;
import com.ts.util.DBinit;
import com.ts.util.IConnectionPool;
import com.ts.util.SeparatorConstant;
import com.ts.util.SpringContextUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisBase {

	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	/**
	 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
	 * 
	 * @param String
	 *            key
	 * @return List<String> 集合的全部记录
	 **/
	public List<String> sort(String key) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		List<String> list = sjedis.sort(key);
		shardedJedisPool.returnResource(sjedis);
		return list;
	}

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param String
	 *            key
	 * @return 成员集合
	 */
	public Set<String> smembers(String key) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		Set<String> set = sjedis.smembers(key);
		shardedJedisPool.returnResource(sjedis);
		return set;
	}

	/**
	 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
	 * 
	 * @param String
	 *            key
	 * @return Set<String> 存储名称的集合
	 */
	public Set<String> hkeys(String key) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		Set<String> set = sjedis.hkeys(key);
		shardedJedisPool.returnResource(sjedis);
		return set;
	}

	/**
	 * 获取hash中value的集合
	 * 
	 * @param String
	 *            key
	 * @return List<String>
	 */
	public List<String> hvals(String key) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		List<String> list = sjedis.hvals(key);
		shardedJedisPool.returnResource(sjedis);
		return list;
	}

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 */
	public Map<String, String> hgetall(String key) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		Map<String, String> map = sjedis.hgetAll(key);
		shardedJedisPool.returnResource(sjedis);
		return map;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            start
	 * @param long
	 *            end
	 * @return List
	 */
	public List<String> lrange(String key, long start, long end) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		List<String> list = sjedis.lrange(key, start, end);
		shardedJedisPool.returnResource(sjedis);
		return list;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            start
	 * @param int
	 *            end 如果为负数，则尾部开始计算
	 * @return List
	 */
	public List<byte[]> lrange(byte[] key, int start, int end) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		List<byte[]> list = sjedis.lrange(key, start, end);
		shardedJedisPool.returnResource(sjedis);
		return list;
	}

	/**
	 * 对指定key对应的value进行截取
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            startOffset 开始位置(包含)
	 * @param long
	 *            endOffset 结束位置(包含)
	 * @return String 截取的值
	 */
	public String getrange(String key, long startOffset, long endOffset) {
		ShardedJedis sjedis = shardedJedisPool.getResource();
		String value = sjedis.getrange(key, startOffset, endOffset);
		shardedJedisPool.returnResource(sjedis);
		return value;
	}
	
	
	public static void main(String[] args) {
		IRedisUtil redisUtil = (IRedisUtil) SpringContextUtils.getBean("redisUtil");

//		Long starttime = System.currentTimeMillis();
//		// 每次重启服务器 ，需要清空数据??
//		// System.out.println(event.getServletContext().getRealPath("/"));
//		ResultSet data_load_rule_rs = null;
//		ResultSet data_rs = null;
//		ResultSet data_rs2 = null;
//		ResultSet data_class_rs = null;
//		ResultSet data_type_rs = null;
//		Statement stmt = null;
//		Statement stmt2 = null;
//		Statement stmt3 = null;
//		Statement stmt4 = null;
//		PreparedStatement pstmt = null;
//		Connection conn = null;
//		String tableMTSDATA = "MTS_DATA";
//		//延迟加载
//		Thread.sleep(10000);
//		IConnectionPool pool = initDB();
//		conn = pool.getConnection();
//		stmt = conn.createStatement();
//		stmt2 = conn.createStatement();
//		stmt3 = conn.createStatement();
//		stmt4 = conn.createStatement();
//		// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID
//		data_load_rule_rs = stmt.executeQuery("select * from MTS_LOAD_RULE_DETAIL");
//		while (data_load_rule_rs.next()) {
//			String keyRule = data_load_rule_rs.getString("KEY_GEN_RULE");
//			String valueRule = data_load_rule_rs.getString("VALUE_GEN_RULE");
//			if (keyRule.indexOf(SeparatorConstant.dataClassSeparator) != -1) {// 判断是否有聚类
//				// 标准化类型ID
//				int matchDataTypeId = data_load_rule_rs.getInt("DATA_TYPE_ID");
//				// 查询聚类code
//				String data_class_code = matchClassType(keyRule, matchDataTypeId, conn);
//				// 查询标化类型code
//				String data_type_code = matchDataType(matchDataTypeId, keyRule, conn);
//				// 拼装key
//				// 校验key规则
//				if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
//					data_rs = stmt2
//							.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
//									+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId);
//					String line = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
//					// 多线程输出
//					data_rs.last(); // 移到最后一行
//					int rowCount = data_rs.getRow(); // 得到当前行号，也就是记录数
//					//线程数
//					int people = 8;
//					int everyrow = rowCount%people ==0?rowCount / people:rowCount / people + 1;
//					Thread[] thread = new Thread[people];
//					int count = 0;
//					for (int i = 0; i < people; i++) {
//						PyHelloAppModule myThread = new PyHelloAppModule();
//						myThread.setConn(conn);
//						myThread.setData_class_code(data_class_code);
//						myThread.setData_type_code(data_type_code);
//						myThread.setKeyRule(keyRule);
//						myThread.setLine(line);
//						myThread.setValueRule(valueRule);
//						if (i == people - 1) {
//							data_rs2 = stmt3.executeQuery(
//									"select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
//											+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId
//											+ " limit " + count + "," + rowCount);
//						} else {
//							data_rs2 = stmt4.executeQuery(
//									"select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
//											+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId
//											+ " limit " + count + "," + (count + everyrow));
//						}
//						List resultlist = resultSetToList(data_rs2);
//						myThread.setResultlist(resultlist);
//						thread[i] = new Thread(myThread);
//						thread[i].start();
//						data_rs2.last(); // 移到最后一行
//						System.out.println("单次处理数据数量=======>>" + data_rs2.getRow()); // 得到当前行号，也就是记录数
//						count += everyrow;
//					}
//				} else {
//					System.out.println("此规则没有配置匹配原始数据的规则!!~~");
//				}
//			} else {
//				System.out.println("此规则没有配置匹配数据聚类的规则!!~~");
//			}
//		}
//		logger.error("MTS本体数据加载用时======" + formatDuring(System.currentTimeMillis() - starttime));
		System.out.println("恭喜，数据装载正常结束!!");
		// }
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
				System.out.println("标准化类型ID为" + matchDataTypeId + "无数据聚类项，请及时配置归属的数据聚类!!~~");
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
//				logger.info("此匹配规则无需匹配标准化类型配置项!!~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_type_rs.close();
		}
		return data_type_code;
	}
}
