package com.aghit.task.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class ConvertUtil {
	
	/**
	 * 把集合数据变成有key的map数据
	 * 
	 * @param rawData list数据
	 * @param keys map的主键
	 * @return
	 */
	public static Map<String, Map<String, Object>> cvtKeyMap(
			Collection<Map<String, Object>> rawData, String[] keys) {

		Map<String, Map<String, Object>> ret = new HashMap<String, Map<String, Object>>();
		
		Iterator<Map<String, Object>> it = rawData.iterator();
		String mapKey = null;
		
		// 遍历集合数据
		while (it.hasNext()) {
			Map<String, Object> row = it.next();
			mapKey = "";
			// 遍历所有的Key字段
			for (String key : keys) {
				// 组合key的方式：_数值,以此类推， 例如：_9820_1234
				mapKey += "_" + row.get(key).toString();
			}
			ret.put(mapKey, row);
		}

		return ret;
	}
	
	/**
	 * 把集合数据变成有key的map数据
	 * 
	 * @param rawData list数据
	 * @param keys map的主键
	 * @return
	 */
	public static Map<String, Collection<Map<String, Object>>> cvtKeyCMap(
			Collection<Map<String, Object>> rawData, String[] keys) {

		Map<String, Collection<Map<String, Object>>> ret = new HashMap<String, Collection<Map<String, Object>>>();
		
		Iterator<Map<String, Object>> it = rawData.iterator();
		String mapKey = null;
		
		// 遍历集合数据
		while (it.hasNext()) {
			Map<String, Object> row = it.next();
			mapKey = "";
			// 遍历所有的Key字段
			for (String key : keys) {
				// 组合key的方式：_数值,以此类推， 例如：_9820_1234
				mapKey += "_" + row.get(key).toString();
			}
			if (null == ret.get(mapKey)){
				Collection<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
				c.add(row);
				ret.put(mapKey, c);
			}else{
				ret.get(mapKey).add(row);
			}
			
		}

		return ret;
	}
	
	/**
	 * 防止空指针异常-获取int类型
	 * @param obj
	 * @return
	 */
	public static int getInt(Object obj){
		if(obj==null){
			return -1;
		}else{
			return Integer.parseInt(obj.toString());
		}
	}
	
	/**
	 * 防止空指针异常-获取long类型
	 * @param obj
	 * @return
	 */
	public static long getLong(Object obj){
		if(obj==null){
			return -1;
		}else{
			return Long.valueOf(obj.toString());
		}
	}
	
	/**
	 * 防止空指针异常-获取float类型
	 * @param obj
	 * @return
	 */
	public static float getFloat(Object obj){
		if(obj==null){
			return -1;
		}else{
			return Float.valueOf(obj.toString());
		}
	}
	
	/**
	 * 防止空指针异常-获取double类型
	 * @param obj
	 * @return
	 */
	public static double getDouble(Object obj){
		if(obj==null){
			return -1;
		}else{
			return Double.valueOf(obj.toString());
		}
	}
	
	/**
	 * 防止空指针异常-获取String类型
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * 防止空指针异常 - 获取Date类型
	 * @param obj
	 * @return
	 */
	public static Date getDate(Object obj){
		if (null == obj){
			return null;
		}else {
			return (Date)obj;
		}
	}
	
	/**
	 * object 转 map
	 * @param obj
	 * @return
	 */
	public static Map getMap(Object obj){
	    if(obj instanceof Map) return (Map)obj;
	    return null;
	}

	/**
	 * 组装ComparedEntity对象
	 * 
	 * @param code
	 * @param name
	 * @param id
	 * @param pay_tot
	 * @param chkType 术语类型
	 * @param treeCate 分类树ID
	 * @param key 匹配主键
	 * @return
	 */
//	public static ComparedEntity[] crtCpdEntity(String code, String name, long id,
//			double pay_tot, int chkType, String key ,Date exe_date){
//		
//		ComparedEntity rtn = new ComparedEntity(code, name, id, pay_tot,exe_date);
////		List<Long> ids = new ArrayList<Long>();
//		
//		// 药品的时候（包含中、西药）
////		if(Constant.CHECK_PROJECT_DRUG == chkType){
////			ids = CacheServiceImpl.getDrugCate().get(key);
////		}
////		
////		// 耗材的时候
////		if(Constant.CHECK_PROJECT_CONSUM == chkType){
////			ids = CacheServiceImpl.getCsumCate().get(key);
////		}
//		
//		// 保证即使没有分类也不会出现空指针异常
////		rtn.setCateId(list2longArr(ids));
//		
//		return new ComparedEntity[]{rtn};
//	}
	
	/**
	 * 提供List<Long>和原始long类型数组之间的转换
	 * @param raw List参数
	 * @return
	 */
	public static long[] list2longArr(List<Long> raw){
		
		if(raw == null) return new long[0];
		
		long[] rtn = new long[raw.size()];
		for(int i = 0; i < raw.size(); i++){
			rtn[i] = raw.get(i);
		}
		
		return rtn;
	}
	
	public static Set<Long> longArr2List(long[] rawArr){
		
		Set<Long> rtn = new TreeSet<>();
		if(rawArr == null) return rtn;
		for(long num : rawArr){
			rtn.add(num);
		}
		
		return rtn;
	}
}
