package com.models.cloud.common.cache.dict;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.dict.entity.DimDict;
import com.models.cloud.common.dict.service.DictService;
import com.models.cloud.constants.BaseDictConstant;

/**
 * 字典表dim_dict缓存处理
 */
@Service("dimDictCache")
public class DimDictCache extends DictCache {
	
	//日志
    private static Logger log = Logger.getLogger(DimDictCache.class);
    
    //字典服务类
  	@Resource(name = "dictServiceImpl")
  	private DictService dictServiceImpl;	
    
    //系统基础字典表 Map<type,<id,DimDict>>
  	private static Map<String,Map<String,DimDict>> dimDictMap;
  	//缓存更新时间
  	private static Date cacheDate;
  	
  	/**
  	 * 构造方法
  	 */
  	public DimDictCache(){
  		tableName = "DIM_DICT";
  	}
  	
  	/**
     * 获取系统基础字典表
     * @return
     */
	public static Map<String, Map<String, DimDict>> getDimDictMap() {
		//如果静态变量为空，重新初始化缓存
		if(null == dimDictMap || 0 == dimDictMap.size()){
			DimDictCache cache = new DimDictCache();
			cache.initDictCache();
		}
		return dimDictMap;
	}
	
	/**
	 * 获取缓存更新时间
	 * @return
	 */
	@Override
	public Date getCacheDate(){
		return cacheDate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.common.cache.DictCache#initDictCache()
	 */
	@Override
	public void initDictCache() {
		//配置参数
		DimDict dimDict = new DimDict();
		dimDict.setValidFlag(BaseDictConstant.VALID_FLAG_YES);
		try {
			List<DimDict> lst = dictServiceImpl.findDimDictList(dimDict);
			if (null == lst) {
				log.info("加载字典表缓存" + tableName +"：表数据为空。");
				return;
			}
			Map<String,Map<String,DimDict>> map = new LinkedHashMap<String,Map<String,DimDict>>();
			for (DimDict dict : lst) {
                 if (!map.containsKey(dict.getDictTypeId())){
                	 Map<String,DimDict> m = new LinkedHashMap<String,DimDict>();
                	 m.put(dict.getDictId().toString(), dict); 
                	 map.put(dict.getDictTypeId(), m);
                 }else{
                	 map.get(dict.getDictTypeId()).put(dict.getDictId().toString(), dict);
                 }
			}
			if(null != dimDictMap){
				dimDictMap.clear();
			}
			dimDictMap = map;
			cacheDate = new Date();
		} catch (Exception ex) {
			log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
		} finally {
			log.info("已完成加载字典表缓存"+ tableName);
		}

	}


}
