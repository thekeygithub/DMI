package com.models.cloud.common.cache.dict;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.dict.entity.DimSysConf;
import com.models.cloud.common.dict.service.DictService;

/**
 * 字典表dim_Sys_conf缓存处理
 */
@Service("dimSysConfCache")
public class DimSysConfCache extends DictCache{
	
	//日志
    private static Logger log = Logger.getLogger(DimSysConfCache.class);
    
    //字典服务类
  	@Resource(name = "dictServiceImpl")
  	private DictService dictServiceImpl;	
  	
    //系统基础配置表Map<id,DimSysConf>
  	private static Map<String,DimSysConf> dimSysConfMap;
    //缓存更新时间
  	private static Date cacheDate;
  	
	/**
  	 * 构造方法
  	 */
  	public DimSysConfCache(){
  		tableName = "DIM_SYS_CONF";
  	}
  	
  	/**
  	 * 获取系统基础配置表缓存
  	 * @return
  	 */
	public static Map<String, DimSysConf> getDimSysConfMap() {
		//如果静态变量为空，重新初始化缓存
		if(null == dimSysConfMap || 0 == dimSysConfMap.size()){
			DimSysConfCache cache = new DimSysConfCache();
			cache.initDictCache();
		}
		return dimSysConfMap;
	}

	/**
	 * 获取缓存更新时间
	 * @return
	 */
  	@Override
	public Date getCacheDate(){
		return cacheDate;
	}

	@Override
	public void initDictCache() {
		//配置参数
		DimSysConf dimSysConf = new DimSysConf();
		try{
			List<DimSysConf> lst = dictServiceImpl.findDimSysConfList(dimSysConf);
			if(null == lst){
				log.info("加载字典表缓存" + tableName +"：表数据为空。");
				return;
			}
			Map<String,DimSysConf> map = new HashMap<String,DimSysConf>();
			for(DimSysConf conf:lst){
				map.put(conf.getConfId(), conf);
			}
			if(null != dimSysConfMap){
				dimSysConfMap.clear();
			}
			dimSysConfMap = map;
			cacheDate = new Date();
		} catch (Exception ex) {
			log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
		} finally {
			log.info("已完成加载字典表缓存 "+ tableName);
		}
	}


}
