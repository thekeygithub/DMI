package com.ts.util.redis.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ts.listener.RedisDataLoadListener;
import com.ts.util.CommonUtils;
import com.ts.util.ConnectionPoolManager;
import com.ts.util.DBinit;
import com.ts.util.IConnectionPool;
import com.ts.util.QStringUtil;
import com.ts.util.SeparatorConstant;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @类名称: RedisUtil
 * @类描述: Redis库DDl操作工具类 注意: 保存到Redis库的数据一定要保持 key值唯一 否则会覆盖原有存在的key 类似Map对象的操作
 *       kEY值不要太长 512K以内为限制
 * @作者:李巍
 * @创建时间:2016-2-26 下午6:21:16
 */
public class RedisUtil implements IRedisUtil {
	private static Logger logger = Logger.getLogger(RedisUtil.class);
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
	private Jedis redis = null;
	/**
	 * 宕机处理(容灾处理)库,暂不提供服务
	 */
	private ShardedJedisPool shardedJedisPool;
	/**
	 * 为提高扩展性，以后会有多个redis服务，此Map中key为redis服务分别为oa,service;value为对应的redis池
	 */
	private Map<String, ShardedJedisPool> shardedJedisPoolMap;

	

	/**
	 * @方法名称: set
	 * @功能描述: 添加字符串数据
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午7:57:59
	 * @param system
	 *            选择数据库标识 目前仅用RedisKeys.SYSMTS
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 * @see com.common.redis.IRedisUtil#set(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public String set(String system, String key, String value) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		String result = null;
		try {
			result = shardedJedis.set(key, value);
			return result;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		} finally {
			returnResource(system, shardedJedis);
		}
		return result;
	}

	/**
	 * @方法名称: setLeftMenusData
	 * @功能描述: 添加左侧菜单缓存
	 * @作者:李巍
	 * @创建时间:2016-2-29 上午9:33:36
	 * @param sysid
	 * @param userid
	 * @param functions
	 * @return
	 * @see com.common.redis.IRedisUtil#setLeftMenusData(java.lang.String,
	 *      java.lang.String, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String setLeftMenusData(String sysid, String userid, List<Map<String, Object>> functions) {
		StringBuffer html = new StringBuffer();
		for (Map<String, Object> menus1 : functions) {
			html.append("<li>");// 开始
			/**
			 * 校验是否存在二级菜单
			 */
			if (menus1.get("menu2list") != null) {// 存在
				insertA(html, null, menus1.get("name"));
				html.append("<ul class=\"nav_2j\">");
				List<Map<String, Object>> munus2Node = (List<Map<String, Object>>) menus1.get("menu2list");
				// 加载二级菜单
				for (Map<String, Object> menus2 : munus2Node) {
					html.append("<li>");
					/**
					 * 校验三级菜单是否存在
					 */
					if (menus2.get("menu3list") != null) {// 存在

						html.append(
								"<a href=\"javascript:void(0)\" style=\"background:transparent url('../images/rubiao_44.png') no-repeat scroll left center\">");
						html.append(menus2.get("name2"));
						html.append("</a>");
						// 加载三级页面
						html.append("<ul class=\"nav_3j\">");
						List<Map<String, Object>> munus3Node = (List<Map<String, Object>>) menus2.get("menu3list");
						for (Map<String, Object> menus3 : munus3Node) {
							html.append("<li>");
							insertA(html, menus3.get("url"), menus3.get("lev3name"));
							html.append("</li>");
						}
						html.append("</ul>");
					} else {
						insertA(html, menus2.get("url"), menus2.get("name2"));
					}
					html.append("</li>");
				}
				html.append("</ul>");
			} else {// 不存在
				insertA(html, menus1.get("url"), menus1.get("name"));
			}
			html.append("</li>");// 结束
		}
		StringBuffer str = new StringBuffer();
		str.append(RedisKeys.getSysKey(sysid));
		str.append(RedisKeys.LEFTMENUSPrefix);
		str.append(userid);
		return set(RedisKeys.SYSOA, str.toString(), html.toString());
	}

	/**
	 * @方法名称: insertA
	 * @功能描述: 添加A标签
	 * @作者:李巍
	 * @创建时间:2016-1-14 下午2:27:46
	 * @param html
	 * @param url
	 * @param name
	 *            void
	 */
	private void insertA(StringBuffer html, Object url, Object name) {
		html.append("<a href=\"");
		if (url != null && !"".equals(url.toString())) {
			if (url.toString().startsWith("#")) {
				html.append("javascript:void(0)");
			} else {
				html.append("../" + url);
			}
		} else {
			html.append("javascript:void(0)");
		}
		html.append("\" target=\"mainFrame\">");
		html.append(name);
		html.append("</a>");
	}

	/**
	 * @方法名称: getLeftMenusData
	 * @功能描述:获取左侧菜单缓存数据
	 * @作者:李巍
	 * @创建时间:2016-1-14 下午2:16:51
	 * @return
	 * @throws Exception
	 * @see com.common.redis.IRedisUtil#getLeftMenusData()
	 */
	@Override
	public String getLeftMenusData(String sysid, String key) {
		StringBuffer str = new StringBuffer();
		str.append(RedisKeys.getSysKey(sysid));
		str.append(RedisKeys.LEFTMENUSPrefix);
		str.append(key);
		return get(RedisKeys.SYSOA, str.toString());
	}

	/**
	 * @方法名称: clearLeftMenusCache
	 * @功能描述:根据人员id清除所有系统中的缓存数据
	 * @作者:慕金剑
	 * @创建时间:2016-3-3 上午10:31:29
	 * @param userid
	 * @see com.common.redis.IRedisUtil#clearLeftMenusCache(java.lang.String)
	 */

	@Override
	public void clearLeftMenusCache(String userid) {
		StringBuffer str = new StringBuffer();
		str.append(RedisKeys.LEFTMENUSPrefix);
		str.append(userid);
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSOA + str.toString());
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSHIS + str.toString());
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSLIS + str.toString());
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSSM + str.toString());
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSPACS + str.toString());
		this.delete(RedisKeys.SYSOA, RedisKeys.SYSYPJXC + str.toString());
	}

	/**
	 * @方法名称: delete
	 * @功能描述: 删除单个数据
	 * @作者:李巍
	 * @创建时间:2016-1-4 下午2:01:42
	 * @param key
	 *            void
	 * @throws Exception
	 */
	public Long delete(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		long count = shardedJedis.del(key);
		returnResource(key, shardedJedis);
		return count;
	}

	/**
	 * @方法名称: get
	 * @功能描述: 获取值
	 * @作者:李巍
	 * @创建时间:2016-1-4 下午2:04:39
	 * @param keyId
	 * @return String
	 * @throws Exception
	 */
	public String get(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		String result = null;
		try {
			result = shardedJedis.get(key);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return result;
	}

	/**
	 * @方法名称: setObject
	 * @功能描述: 存一个对象 如果key已经存在 覆盖原值 成功返回 OK 失败返回 null
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午7:20:14
	 * @param system
	 * @param key
	 * @param value
	 * @return
	 * @see com.common.redis.IRedisUtil#setObject(java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	public String setObject(String system, String key, Object value) {
		return setObjectImpl(system, key, value);
	}

	/**
	 * 存一个对象 如果key已经存在 覆盖原值 成功返回 OK 失败返回 null
	 */
	public String setObject(String system, String key, Serializable value) {
		return setObjectImpl(system, key, value);
	}

	/**
	 * 存一个List 对象 如果key已经存在 覆盖原值 成功返回 OK 失败返回 null
	 */
	public String setObject(String system, String key, List<? extends Serializable> value) {
		return setObjectImpl(system, key, value);
	}

	/**
	 * 存一个Map对象 如果key已经存在 覆盖原值 成功返回 OK 失败返回 null
	 */
	public String setObject(String system, String key, Map<?, ? extends Serializable> value) {
		return setObjectImpl(system, key, value);
	}

	/**
	 * 存一个Set集合 如果key已经存在 覆盖原值 成功返回 OK 失败返回 null
	 */
	public String setObject(String system, String key, Set<? extends Serializable> value) {
		return setObjectImpl(system, key, value);
	}

	/**
	 * @方法名称: setObjectImpl
	 * @功能描述: 存储对象
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午7:24:36
	 * @param system
	 * @param key
	 * @param value
	 * @return String 成功返回OK 失败返回null
	 */
	private String setObjectImpl(String system, String key, Object value) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		try {
			byte[] byteArray = objectToByte(value);
			String setObjectRet = shardedJedis.set(key.getBytes(), byteArray);
			return setObjectRet;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return null;
	}

	/************* 取对象 ****************/
	/**
	 * @方法名称: getObject
	 * @功能描述: 获取对象
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午7:25:57
	 * @param system
	 * @param key
	 * @return Object 存在返回对象 异常返回null
	 */
	public Object getObject(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		try {
			byte[] bs = shardedJedis.get(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return null;
	}

	/**
	 * 判断key是不是已经存在
	 * 
	 * @param key
	 *            存在 返回true 否则 返回false
	 */
	public boolean exists(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		boolean result = false;
		try {
			result = shardedJedis.exists(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return result;
	}

	/**
	 * Long expire(String key,int seconds); 给指定的key设置生存时间 以秒为单位 当key过期时，会被自动删除
	 * 如果key不存在 返回0 设置成功返回1 过期时间的延迟在1秒钟之内，也就是，就算key已经过期，但它还是可能在1秒之内被访问到
	 */
	public Long expire(String system, String key, int seconds) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		long result = 0;
		try {
			result = shardedJedis.expire(key.getBytes(), seconds);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return result;
	}

	/**
	 * Long ttl(String key); 以秒为单位，返回给定 key 的剩余生存时间。 当 key 不存在或没有设置生存时间时，返回 -1 。
	 * 否则，返回 key 的剩余生存时间(以秒为单位)。
	 */
	public Long ttl(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		long result = 0;
		try {
			result = shardedJedis.ttl(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return result;
	}

	/**
	 * TYPE public String type(String key) 描述：返回key所对应的value的类型。 返回值：none -
	 * key不存在 string - 字符串 list - 列表 set - 集合 zset - 有序集 hash - 哈希表
	 */
	public String type(String system, String key) {
		ShardedJedis shardedJedis = getShardedJedis(system);
		String result = null;
		try {
			result = shardedJedis.type(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally {
			returnResource(key, shardedJedis);
		}
		return result;
	}

	/**
	 * @方法名称: getShardedJedis
	 * @功能描述: 获取Jedis连接 目前仅用OA库即可,暂不考虑分库
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午8:06:20
	 * @param system
	 * 
	 * 
	 * @return ShardedJedis
	 */
	private ShardedJedis getShardedJedis(String system) {
		ShardedJedisPool pool = null;
		try {
			pool = shardedJedisPoolMap.get(system);
		} catch (Exception e) {
			return shardedJedisPool.getResource(); // 发生异常时,由shardedJedisPool库提供
		}
		if (pool != null) {
			return pool.getResource();
		} else {
			return shardedJedisPool.getResource(); // 如果某一台存放数据的redis服务宕机，有shardedJedisPool提供临时提供服务
		}
	}

	/**
	 * @方法名称: returnResource
	 * @功能描述: 释放资源
	 * @作者:李巍
	 * @创建时间:2016-2-29 上午9:54:11
	 * @param system
	 * @param shardedJedis
	 *            void
	 */
	private void returnResource(String system, ShardedJedis shardedJedis) {
		shardedJedisPool.returnResource(shardedJedis);
	}

	/**
	 * @方法名称: returnBrokenResource
	 * @功能描述: 释放不可用的资源
	 * @作者:李巍
	 * @创建时间:2016-2-29 上午9:54:17
	 * @param shardedJedis
	 *            void
	 */
	private void returnBrokenResource(ShardedJedis shardedJedis) {
		shardedJedisPool.returnBrokenResource(shardedJedis);
	}

	/**
	 * @方法名称: objectToByte
	 * @功能描述: 对象序列化
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午6:36:28
	 * @param obj
	 * @return byte[]
	 */
	protected byte[] objectToByte(Object obj) {
		byte[] bs = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bs = bos.toByteArray();
			oos.close();
			bos.close();
			return bs;
		} catch (IOException e) {

			e.printStackTrace();
		}
		return bs;
	}

	/**
	 * @方法名称: byteToObject
	 * @功能描述: 对象反序列化
	 * @作者:李巍
	 * @创建时间:2016-2-26 下午6:36:39
	 * @param bs
	 * @return Object
	 */
	protected Object byteToObject(byte[] bs) {
		Object obj = null;
		try {
			if (bs != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
				ObjectInputStream inputStream = new ObjectInputStream(bis);
				obj = inputStream.readObject();
				// inputStream.close();
				// bis.close();
			}
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return obj;
	}

	public void setShardedJedisPoolMap(Map<String, ShardedJedisPool> shardedJedisPoolMap) {
		this.shardedJedisPoolMap = shardedJedisPoolMap;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	// =====================2016.11.04 liwei new ==============================

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

	// ===============================Redis对外接口==================================
	/**
	 * 
	 * @方法名称: size
	 * @功能描述: 获取redis数据库总记录数
	 * @作者:李巍
	 * @创建时间:2016年12月7日 下午3:59:49
	 * @return Long
	 */
	public Long getDBsize() {
		try {
			String host = CommonUtils.getPropValue("redis.properties", "redis.host");
			int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
			String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
			redis = new Jedis(host, port, 400000);
			redis.auth(pass);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return redis.dbSize();
	}
	
	/**
	 * 
	 * @方法名称: isRedisLoaded
	 * @功能描述: Redis是否加载完成
	 * @作者:李巍
	 * @创建时间:2016年12月7日 下午4:47:32
	 * @return boolean
	 */
	public boolean isRedisLoaded(){
		if(this.getRedisLoadSize() !=0 && this.getDBsize() == this.getRedisLoadSize()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @方法名称: getRedisLoadSize
	 * @功能描述: 获取redis本次即将加载的数量
	 * @作者:李巍
	 * @创建时间:2016年12月7日 下午4:22:15
	 * @return Long
	 */
	public Long getRedisLoadSize() {
		/*
		 * KEY生成规则 数据聚类 ——标化——原始数据名称 先判断有几个$ ，根据 VALUE生成规则 ，取本体表中 原始整合串的第一个字符串
		 */
		Long count = 0L;// 计数器
		try {
			IConnectionPool pool = initDB();
			conn = pool.getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			stmt4 = conn.createStatement();
			// 装载内存数据规则，从前台配置的装载规则中，查询标准化类型ID
			data_load_rule_rs = stmt.executeQuery("select * from MTS_LOAD_RULE_DETAIL");
			// IRedisUtil redisUtil = (IRedisUtil)
			// SpringContextUtils.getBean("redisUtil");
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
					String data_type_code = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataTypeSeparator) + 1,
							keyRule.indexOf(SeparatorConstant.dataSeparator));
					// 拼装key
					// 校验key规则
					if (keyRule.indexOf(SeparatorConstant.dataSeparator) != -1) {
						data_rs = stmt2.executeQuery("select DATA_ID,ORIG_DATA_ID,ORIG_DATA_NAME,ORIG_DATA_STR from "
								+ tableMTSDATA + " where DATA_TYPE_ID = " + matchDataTypeId);
						String keyline = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator));
						/**
						 * valueline==================================
						 */
						String valueline = valueRule.substring(valueRule.indexOf(SeparatorConstant.dataSeparator));
						// 多线程输出
						// data_rs.last(); // 移到最后一行
						// int rowCount = data_rs.getRow(); // 得到当前行号，也就是记录数
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

							for (int i = 0; i < resultlist.size(); i++) {
								dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator
										+ data_type_code + SeparatorConstant.datajoinSeparator);
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
								String resultKey = dataKey.toString().substring(0,
										dataKey.length() - SeparatorConstant.datajoinSeparator.length());

								// 拼装VALUE//valueRule
								// @ORIG_DATA_ID@ORIG_DATA_NAME@3 VALUE
								// 重症肌无力|脑血管意外||1
								for (int j = 0; j < valuelines.length; j++) {
									dataValue.append(spellValue(SeparatorConstant.dataSeparator + valuelines[j],
											data_id, map, orig_data_str));
								}
								String resultValue = dataValue.substring(0,
										dataValue.length() - SeparatorConstant.datareturnSeparator.length());
								// 计数
								if (!QStringUtil.isEmpty(resultKey) && !QStringUtil.isEmpty(resultValue)) {
									count++;
								}

							}
						} else if ("word".equals(validateRE(keyline))) {// 是字母
							String queryWord = keyRule.substring(keyRule.indexOf(SeparatorConstant.dataSeparator) + 1);
							for (int i = 0; i < resultlist.size(); i++) {
								dataKey = new StringBuffer(data_class_code + SeparatorConstant.datajoinSeparator
										+ data_type_code + SeparatorConstant.datajoinSeparator);
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
								String resultValue = dataValueStr.substring(0,
										dataValueStr.length() - SeparatorConstant.datareturnSeparator.length());
								String resultKey = dataKey.toString().substring(0,
										dataKey.length() - SeparatorConstant.datajoinSeparator.length());
								// 拼装redis
								logger.info("========value======" + resultValue + "===data_id" + data_id);
								// 计数
								if (!QStringUtil.isEmpty(resultKey) && !QStringUtil.isEmpty(resultValue)) {
									count++;
								}
							}
						}
					} else {
						logger.info("此规则没有配置匹配原始数据的规则!!~~");
					}
				} else {
					logger.info("此规则没有配置匹配数据聚类的规则!!~~");
				}
			}
		} catch (Exception e) {
			logger.error("获取当前准数据计数失败，系统内部异常~");
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
		return count;
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

	
	public Long countKey(String key) {
		
		String host = CommonUtils.getPropValue("redis.properties", "redis.host");
		int port = Integer.parseInt(CommonUtils.getPropValue("redis.properties", "redis.port"));
		String pass = CommonUtils.getPropValue("redis.properties", "redis.pass");
		redis = new Jedis(host, port, 400000);
		redis.auth(pass);
		Set<String> cnt=redis.keys(key);
		return (long) cnt.size();
	}

}
