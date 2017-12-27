package com.ebmi.std.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ConvertUtils {

	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串非空后返回该值的字符串
	 * @Title: mapHasKeyValue 
	 * @param map
	 * @param key
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String mapHasKeyValue(Map map,Object key){
		if(map.containsKey(key)&&map.get(key)!=null&&!map.get(key).toString().trim().equals("")){
			return map.get(key).toString().trim();
		}else{
			return "";
		}
		
	}
	
	/**
	 * 
	 * @Description: 获取int类型
	 * @Title: getInt 
	 * @param obj
	 * @return int
	 */
	public static int getInt(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			return Integer.parseInt(obj.toString());
		}
	}

	/**
	 * 
	 * @Description: 获取long类型
	 * @Title: getLong 
	 * @param obj
	 * @return long
	 */
	public static long getLong(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			return Long.valueOf(obj.toString());
		}
	}

	/**
	 * 
	 * @Description: 获取float类型
	 * @Title: getFloat 
	 * @param obj
	 * @return float
	 */
	public static float getFloat(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			return Float.valueOf(obj.toString());
		}
	}

	/**
	 * 
	 * @Description: 获取double类型
	 * @Title: getDouble 
	 * @param obj
	 * @return double
	 */
	public static double getDouble(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			return Double.valueOf(obj.toString());
		}
	}

	/**
	 * 
	 * @Description: 返回格式化double
	 * @Title: getFormatDouble 
	 * @param obj
	 * @return String
	 */
	public static String getFormatDouble(Object obj) {
		if (obj == null) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			df.setRoundingMode(RoundingMode.HALF_UP);
			return df.format(Double.valueOf(obj.toString()));
		}
	}

	/**
	 * 
	 * @Description: 返回格式化double
	 * @Title: getFormatDouble 
	 * @param obj
	 * @param fromat
	 * @return String
	 */
	public static String getFormatDouble(Object obj, String fromat) {
		if (obj == null) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat(fromat);
			df.setRoundingMode(RoundingMode.HALF_UP);
			return df.format(Double.valueOf(obj.toString()));
		}
	}

	/**
	 * 
	 * @Description: 获取String类型
	 * @Title: getString 
	 * @param obj
	 * @return String
	 */
	public static String getString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 
	 * @Description: 获取Date类型
	 * @Title: getDate 
	 * @param obj
	 * @return Date
	 */
	public static Date getDate(Object obj) {
		if (null == obj) {
			return null;
		} else {
			return (Date) obj;
		}
	}

	/**
	 * 
	 * @Description: object 转 map
	 * @Title: getMap 
	 * @param obj
	 * @return Map
	 */
	@SuppressWarnings("rawtypes")
	public static Map getMap(Object obj) {
		if (obj instanceof Map){
			return (Map) obj;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @Description: MAP转Object数组
	 * @Title: mapToArray 
	 * @param map
	 * @return Object[]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object[] mapToArray(Map<String, Object> map){
		List<Object> list = new ArrayList(map.values());
		return (Object[])list.toArray(new Object[list.size()]);
	}
	
	/**
	 * 
	 * @Description: 把集合数据变成有key的map数据
	 * 获取rawData集合中的map，map的key必须包含在keys中
	 * @Title: cvtKeyMap 
	 * @param rawData list数据
	 * @param keys map的主键
	 * @return Map<String,Map<String,Object>>
	 */
	public static Map<String, Map<String, Object>> cvtKeyMap(Collection<Map<String, Object>> rawData,
			String[] keys) {

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
	 * 
	 * @Description: 把集合数据变成有key的map数据
	 * @Title: cvtKeyCMap 
	 * @param rawData list数据
	 * @param keys map的主键
	 * @return Map<String,Collection<Map<String,Object>>>
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
			if (null == ret.get(mapKey)) {
				Collection<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
				c.add(row);
				ret.put(mapKey, c);
			} else {
				ret.get(mapKey).add(row);
			}

		}

		return ret;
	}

	/**
	 * 
	 * @Description: List<Long>转long类型数组
	 * @Title: list2longArr 
	 * @param raw List<Long>参数
	 * @return long[]
	 */
	public static long[] list2longArr(List<Long> raw) {

		if (raw == null)
			return new long[0];

		long[] rtn = new long[raw.size()];
		for (int i = 0; i < raw.size(); i++) {
			rtn[i] = raw.get(i);
		}

		return rtn;
	}

	/**
	 * 
	 * @Description: long类型数组转List<Long>
	 * @Title: longArr2List 
	 * @param rawArr long[]数组
	 * @return List<Long>
	 */
	public static List<Long> longArr2List(long[] rawArr) {

		List<Long> rtn = new ArrayList<>();
		if (rawArr == null)
			return rtn;
		for (long num : rawArr) {
			rtn.add(num);
		}

		return rtn;
	}
	
	/**
	 * 
	 * @Description: 字符串置顶位置字符转*号
	 * @Title: conceal 
	 * @param orignal 字符串
	 * @param start 起始位置 最小等于0
	 * @param end 结束位置 最大等于字符串长度减一
	 * @return String
	 */
	public static String conceal(String orignal, int start, int end) {
		if (orignal != null) {
			int len = orignal.length();
			if (start < 0)
				start = 0;
			if (end >= len)
				end = len - 1;
			if (start < len && start <= end) {
				char[] chars = orignal.toCharArray();
				for (int i = start; i <= end; i++) {
					chars[i] = '*';
				}
				return String.valueOf(chars);
			} else
				return orignal;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Description: 字符串取首字加两个*号
	 * @Title: concealUsername 
	 * @param orignal 字符串
	 * @return String
	 */
	public static String concealUsername(String orignal) {
		if (StringUtils.isNotBlank(orignal)) {
			return orignal.charAt(0) + "**";
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @Description: 生成GUID
	 * @Title: genUUID 
	 * @return: String
	 */
	public static String genUUID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-","");
	}
	
	/**
	 * 
	 * @Description: 生成随机数正整数
	 * @Title: genRandom 
	 * @param count 生成位数
	 * @return String
	 */
	public static String genRandom(int count){
		int n=1;
		if(0<count&&count<19){
			for(int i=1;i<=count;i++){
				n=n*10;
			}
			return (int)(java.lang.Math.random()*n)+"";
		}
		return "";
	}
}
