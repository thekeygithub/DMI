package com.ts.service.mts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.service.mts.matchrule.impl.DataMatchRuleDetail;

@Service("MapCacheService")
public class MapCacheService {
	private final static Log log = LogFactory.getLog(MapCacheService.class);

	private static Map<String, MatchRuleDetailT> cacheMap = null;// 缓存容器
	
	private static Map<String, String> standardizeResMap = null;// 缓存容器

	public static Map<String, String> getStandardizeResMap() {
		if (null == standardizeResMap) {
			standardizeResMap = new HashMap<String, String>();
		}
		return standardizeResMap;
	}

	/*public static void setStandardizeResMap(Map<String, String> standardizeResMap) {
		MapCacheService.standardizeResMap = standardizeResMap;
	}*/

	@Resource(name = "DataMatchRuleDetail")
	private DataMatchRuleDetail dmrd;

	/**
	 * 采用单例模式获取缓存对象实例
	 * 
	 * @return
	 */
	public Map<String, MatchRuleDetailT> getCacheMap() {
		if (null == cacheMap) {
			this.LoadCache();
		}
		return cacheMap;
	}

	/**
	 * 装载缓存
	 */
	public void LoadCache() {
		/********** 数据处理，将数据放入cacheMap缓存中 **begin ******/
		List<MatchRuleDetailT> matchRuleDetailTList = null;
		List<MatchRuleDetailT> matchRuleDetailTStartList = null;
		MatchRuleDetailT matchRuleDetailT = null;
		if (cacheMap == null || cacheMap.isEmpty()) {
			cacheMap = new HashMap<String, MatchRuleDetailT>();
			matchRuleDetailT = new MatchRuleDetailT();
			matchRuleDetailT.setFLAG("1");
			try {
				matchRuleDetailTList = dmrd.findMatchRuleListByClassCode(matchRuleDetailT);
				if (matchRuleDetailTList != null && matchRuleDetailTList.size() > 0) {
					for (int i = 0; i < matchRuleDetailTList.size(); i++) {
						matchRuleDetailT = matchRuleDetailTList.get(i);
						cacheMap.put("memDataCode_" + matchRuleDetailT.getMEM_ID() + matchRuleDetailT.getAREA_ID(), matchRuleDetailT);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			matchRuleDetailT = new MatchRuleDetailT();
			matchRuleDetailT.setORDERBY("1");
			matchRuleDetailT.setFLAG("1");
			try {
				matchRuleDetailTStartList = dmrd.findMatchRuleListByClassCode(matchRuleDetailT);
				if (matchRuleDetailTStartList != null && matchRuleDetailTStartList.size() > 0) {
					for (int i = 0; i < matchRuleDetailTStartList.size(); i++) {
						matchRuleDetailT = matchRuleDetailTStartList.get(i);
						cacheMap.put("startCode_" + matchRuleDetailT.getDATA_CLASS_CODE() + matchRuleDetailT.getAREA_ID(), matchRuleDetailT);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/********** 数据处理，将数据放入cacheMap缓存中 ***end *******/
	}

	/**
	 * 获取缓存项大小
	 * 
	 * @return
	 */
	public int getCacheSize() {
		if (null == cacheMap) {
			return 0;
		}
		return cacheMap.size();
	}

	/**
	 * 重新装载
	 */
	public void ReLoadCache() {
		if (null != cacheMap && !cacheMap.isEmpty()) {
			this.cacheMap.clear();
		}
		this.LoadCache();
	}

	/**
	 * 重新装载
	 */
	public void ClearCache() {
		if (null != cacheMap && !cacheMap.isEmpty()) {
			this.cacheMap.clear();
		}
	}
}
