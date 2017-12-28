
package com.ts.util.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.ShardedJedis;

/**
 * @类名称: IRedisUtil
 * @类描述: Redis库DDl操作工具类 注意: 保存到Redis库的数据一定要保持 key值唯一 否则会覆盖原有存在的key 类似Map对象的操作
 *       kEY值不要太长 512K以内为限制
 * @作者:于文涛
 * @创建时间:2016-2-26 下午6:21:16
 */
public interface IRedisUtil {
	/**
	 * @方法名称: set
	 * @功能描述: 添加字符串数据
	 * @作者:于文涛
	 * @创建时间:2016-2-26 下午7:57:59
	 * @param system
	 *            选择数据库标识
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String set(String system, String key, String value);

	/**
	 * 
	 * @方法名称: setObject
	 * @功能描述: 添加对象
	 * @作者:于文涛
	 * @创建时间:2016-2-26 下午7:58:57
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String setObject(String system, String key, Object value);

	/**
	 * 
	 * @方法名称: setObject
	 * @功能描述: 添加Set集合
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:21:43
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String setObject(String system, String key, Set<? extends Serializable> value);

	/**
	 * @方法名称: setObject
	 * @功能描述: 添加map集合
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:22:09
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String setObject(String system, String key, Map<?, ? extends Serializable> value);

	/**
	 * @方法名称: setObject
	 * @功能描述: 添加List集合
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:22:09
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String setObject(String system, String key, List<? extends Serializable> value);

	/**
	 * @方法名称: setObject
	 * @功能描述: 添加可持久化对象
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:22:09
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param value
	 *            值
	 * @return String 成功返回OK 失败返回Null
	 */
	public String setObject(String system, String key, Serializable value);

	/**
	 * @方法名称: delete
	 * @功能描述: 删除数据
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:23:17
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return Long
	 */
	public Long delete(String system, String key);

	/**
	 * @方法名称: get
	 * @功能描述: 获取对应key的值
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:23:53
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return String
	 */
	public String get(String system, String key);

	/**
	 * @方法名称: getObject
	 * @功能描述: 获取对应key的值(对象)
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:24:42
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return Object
	 */
	public Object getObject(String system, String key);

	/**
	 * @方法名称: exists
	 * @功能描述: 查看key值是否存在
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:25:17
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return boolean
	 */
	public boolean exists(String system, String key);

	/**
	 * @方法名称: expire
	 * @功能描述: 为对应key设置超时时间
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:25:58
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @param seconds
	 *            秒
	 * @return Long
	 */
	public Long expire(String system, String key, int seconds);

	/**
	 * @方法名称: ttl
	 * @功能描述: 获取对应key剩余超时清除时间
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:26:35
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return Long
	 */
	public Long ttl(String system, String key);

	/**
	 * @方法名称: type
	 * @功能描述: 获取对应key在redis保存的类型
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:27:14
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param key
	 *            唯一主键
	 * @return String 返回值:none - key不存在 string - 字符串 list - 列表 set - 集合 zset -
	 *         有序集 hash - 哈希表
	 */
	public String type(String system, String key);

	/**
	 * @方法名称: setLeftMenusData
	 * @功能描述: 添加左侧菜单缓存数据,如果已经存在则覆盖更新
	 * @作者:于文涛
	 * @创建时间:2016-2-29 上午9:28:38
	 * @param system
	 *            选择数据库标识 跟据该值选取 oa库 service(业务)库
	 * @param userid
	 *            对应用户的userid
	 * @param functions
	 *            拥有的菜单对象集合
	 * @return String
	 */
	public String setLeftMenusData(String system, String userid, List<Map<String, Object>> functions);

	/**
	 * @方法名称: getLeftMenusData
	 * @功能描述: 获取左侧菜单的缓存数据
	 * @作者:于文涛
	 * @创建时间:2016-1-14 下午2:16:27
	 * @param system
	 * @return String
	 */
	public String getLeftMenusData(String system, String key);

	/**
	 * @方法名称: clearLeftMenusCache
	 * @功能描述: 根据人员id清除所有系统中的缓存数据
	 * @作者:慕金剑
	 * @创建时间:2016-3-3 上午10:29:41
	 * @param userid
	 *            void
	 */
	public void clearLeftMenusCache(String userid);

	// =====================2016.11.04 liwei new ==============================
	/**
	 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
	 * 
	 * @param String
	 *            key
	 * @return List<String> 集合的全部记录
	 **/
	public List<String> sort(String key);

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param String
	 *            key
	 * @return 成员集合
	 */
	public Set<String> smembers(String key);

	/**
	 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
	 * 
	 * @param String
	 *            key
	 * @return Set<String> 存储名称的集合
	 */
	public Set<String> hkeys(String key);

	/**
	 * 获取hash中value的集合
	 * 
	 * @param String
	 *            key
	 * @return List<String>
	 */
	public List<String> hvals(String key);

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 */
	public Map<String, String> hgetall(String key);

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
	public List<String> lrange(String key, long start, long end);

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
	public List<byte[]> lrange(byte[] key, int start, int end);

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
	public String getrange(String key, long startOffset, long endOffset);
	
	/**
	 * 
	 * @方法名称: isRedisLoaded
	 * @功能描述: Redis内存是否加载完成
	 * @作者:李巍
	 * @创建时间:2016年12月8日 下午1:11:43
	 * @return boolean
	 */
	public boolean isRedisLoaded();
	
	/**
	 * 返回key模糊查询的数量
	 * 
	 * @param String
	 *            key
	 * @return 数量
	 */
	public Long countKey(String key);

}
