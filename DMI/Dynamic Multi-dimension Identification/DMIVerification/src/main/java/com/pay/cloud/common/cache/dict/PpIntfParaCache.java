package com.pay.cloud.common.cache.dict;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.entity.PpIntfPara;
import com.pay.cloud.pay.proplat.service.ProPlatService;

/**
 * 资金平台交互类PP_INTF_PROP缓存
 */
@Service("ppIntfParaCache")
public class PpIntfParaCache extends DictCache {
	
	//日志
    private static Logger log = Logger.getLogger(PpIntfParaCache.class);
	
	//项目与资金平台服务类
  	@Resource(name = "proPlatServiceImpl")
  	private ProPlatService proPlatService;	
    
    //资金平台交互属性 Map<fundid,PpIntfProp>
  	private static Map<String,PpIntfPara> ppIntfParaMap;
  	
  	//缓存更新时间
  	private static Date cacheDate;
  	
  	/**
  	 * 构造方法
  	 */
  	public PpIntfParaCache(){
  		tableName = "PP_INTF_PARA";
  	}
  	
  	/**
     * 获取资金平台交互属性
     * @return
     */
	public static Map<String, PpIntfPara> getPpIntfParaMap() {
		//如果静态变量为空，重新初始化缓存
		if(null == ppIntfParaMap || 0 == ppIntfParaMap.size()){
			PpIntfParaCache cache = new PpIntfParaCache();
			cache.initDictCache();
		}
		return ppIntfParaMap;
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
     * @see com.ebaonet.cloud.common.cache.dict.DictCache#initDictCache()
     */
	@Override
	public void initDictCache() {
		try {
			List<PpIntfPara> lst = proPlatService.findPpIntfParaList();
			if (null == lst) {
				log.info("加载字典表缓存" + tableName +"：表数据为空。");
				return;
			}
			Map<String,PpIntfPara> map = new LinkedHashMap<String,PpIntfPara>();
			for(PpIntfPara prop : lst) {
				map.put(prop.getFundId()+"_"+prop.getParaType(), prop);
			}
			if(null != ppIntfParaMap){
				ppIntfParaMap.clear();
			}
			ppIntfParaMap = map;
			cacheDate = new Date();
		} catch (Exception ex) {
			log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
		} finally {
			log.info("已完成加载字典表缓存"+ tableName);
		}
	}

}
