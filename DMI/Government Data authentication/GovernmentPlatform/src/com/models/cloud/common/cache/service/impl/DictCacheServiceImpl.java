package com.models.cloud.common.cache.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.cache.dict.DictCache;
import com.models.cloud.common.cache.service.DictCacheService;
import com.models.cloud.common.dict.service.DictService;

/**
 * 缓存服务实现类
 */
@Service("dictCacheServiceImpl")
public class DictCacheServiceImpl implements DictCacheService{
	
	//日志
	private static Logger log = Logger.getLogger(DictCacheServiceImpl.class);
	
	//字典服务类
	@Resource(name = "dictServiceImpl")
	private DictService dictServiceImpl;	
	
	//字典变缓存列表
	private List<DictCache> dictCacheList;
	
	/**
	 * 设置字典缓存列表
	 * @param dictCacheList
	 */
	@Resource
	public void setDictCacheList(List<DictCache> dictCacheList) {
		this.dictCacheList = dictCacheList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.cache.service.CacheService#initCache()
	 */
	@Override @PostConstruct
	public void initDictCache() {
		try {
			//需缓存字典表为空，不做处理
			if(null == dictCacheList){
				return;
			}
			//循环字典缓存列表
			for(DictCache cictCache:dictCacheList){
				cictCache.initDictCache();
			}
		} catch (Exception ex) {
			log.error("初始化字典缓存出错：" + ex.getMessage());
		} finally {
			log.info("已完成字典缓存初始化");
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.cache.service.DictCacheService#updateDictCache()
	 */
	@Override
	public void updateDictCache() {
		try {
			Map<String, Date> dbTime = dictServiceImpl.findDimCacheUpd();
			//数据库缓存为空或需缓存字典表为空，不做处理
			if(null == dbTime || null == dictCacheList){
				return;
			}
			//循环字典缓存列表
			for(DictCache dictCache:dictCacheList){
				//如果数据库时间大于当前缓存时间，说明缓存后字典数据有更新操作，则需再次更新缓存
				if (needUpdate(dictCache.getCacheDate(), dbTime.get(dictCache.getTableName()))) {
					log.info("开始更新缓存信息 TableName=" + dictCache.getTableName());
					dictCache.initDictCache();
				}else {
					log.info("无需更新缓存信息 TableName=" + dictCache.getTableName());
				}
			}
		} catch (Exception ex) {
			log.error("更新字典缓存出错：" + ex.getMessage());
		}
	}
	
	//判断是否更新字典缓存
	private boolean needUpdate(Date cacheDate,Date dbDate){
		//如果缓存时间为空，则需要更新
		if(null == cacheDate){
			return true;
		}
		//如果数据库日期为空，则不需要更新字典缓存
		if (null != dbDate){
		    //如果数据库时间大于当前缓存时间，说明缓存后字典数据有更新操作，则需再次更新缓存
			if (dbDate.getTime() - cacheDate.getTime() >0){
				return true;
			}
		}
		return false;
	}

}
