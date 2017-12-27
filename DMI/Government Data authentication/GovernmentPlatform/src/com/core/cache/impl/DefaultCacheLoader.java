package com.core.cache.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.core.action.SysAction;
import com.core.cache.CacheLoader;
import com.core.cache.CacheService;
import com.core.config.SysConf;
import com.core.db.jdbc.JdbcService;
import com.core.dict.SysDict;

@Service
public class DefaultCacheLoader extends CacheLoader {

	@Resource(name="jdbcService")
	private JdbcService jdbcService;
	
//	@Resource(name="jdbcServiceMI")
//	private JdbcService jdbcServiceMI;
	
	@Resource
	EhCacheManagerFactoryBean ehcache;

	@Override
	public void loadAppCacheData(CacheService cacheService) throws Exception {
		// 加载医院基本信息
//		String sql = "select trim(yybh) as yybh, trim(yymc) as yymc, trim(yyjb) as yyjb from view_yyjbxx";
//		List<Map<String, Object>> list1 = jdbcServiceMI.findForList(sql, null);
//		if (list1 != null) {
//			Map<String, Map<String, Object>> map1 = new LinkedHashMap<String, Map<String, Object>>();
//			for (Map<String, Object> m : list1) {
//				map1.put(String.valueOf(m.get("yybh")), m);
//			}
//			cacheService.put(APPCacheKeyConst.VIEW_YYJBXX, map1);
//		}
		
	}

	@Override
	public Collection<SysAction> loadSysAction() throws Exception {
		return null;
	}

	@Override
	public Collection<SysConf> loadSysConf() throws Exception {
		// TODO 从dim_sys_conf表读取
		return null;
	}

	@Override
	public Collection<SysDict> loadSysDict() throws Exception {
		String sql = "SELECT DICT_TYPE_ID dictTypeId, DICT_TYPE_NAME dictTypeName, DICT_ID dictId, DISP_NAME dispName FROM dim_dict WHERE VALID_FLAG = 1 ORDER BY DICT_TYPE_ID, DISP_ORDER";
		final Map<String, SysDict> m = new HashMap<String, SysDict>();
		jdbcService.findListByQuery(sql, null, new RowMapper<Object>() {
			@Override
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				String dictTypeId = rs.getString("dictTypeId");
				String dictTypeName = rs.getString("dictTypeName");
				String dictId = rs.getString("dictId");
				String dispName = rs.getString("dispName");
				SysDict sd = m.get(dictTypeId);
				if (sd == null) {
					sd = new SysDict();
					sd.setDictTypeId(dictTypeId);
					sd.setDictTypeName(dictTypeName);
					sd.put(dictId, dispName);
					m.put(dictTypeId, sd);
				} else {
					sd.put(dictId, dispName);
				}
				return null;
			}
		});
		return m.values();
	}

}
