package com.models.cloud.common.cache.dict;

import java.util.Date;

/**
 * 字典类缓存抽象类
 */
public abstract class DictCache {
	
    /**
     * 相关字典表名
     */
	protected String tableName;
	
	/**
	 * 缓存更新时间
	 */
	protected Date cacheDate;
	
	/**
	 * 初始字典缓存
	 */
	public abstract void initDictCache();
	
	/**
	 * 返回子类的静态变量更新时间
	 * @return
	 */
	public abstract Date getCacheDate();
	
	/**
	 * 返回表名
	 * @return
	 */
	public String getTableName(){
		return tableName;
	}
}
