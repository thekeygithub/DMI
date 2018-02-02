package com.models.cloud.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

public class ConvertUtils {

	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串非空后返回该值的字符串
	 * @Title: mapHasKeyValue 
	 * @param map
	 * @param key
	 * @return String
	 */
	public static String mapHasKeyValue(Map<Object,Object> map,Object key){
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
	 * @Description: 获取Short类型
	 * @Title: getShort 
	 * @param obj
	 * @return Short
	 */
	public static Short getShort(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			return Short.parseShort(obj.toString());
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
			String resl=(int)(java.lang.Math.random()*n)+"";
			
			if(resl.length()<count){
				return genRandom(count);
			}else{
				return resl;
			}
		}
		return "";
	}
	

	/**
	 * 
	 * @Description: 生成验证码
	 * @Title: genarateSmsCode
	 * @param length 生成验证码长度
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String genarateSmsCode(int length) {
		String[] before = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		List list = Arrays.asList(before);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String after = sb.toString();
		String result = after.substring(0, length);
		System.out.println(result);
		return result;
	}

	/**
	 * 
	 * @Description: 生成结果信息
	 * @Title: genReturnMap 
	 * @param hint
	 * @return Map<String,Object>
	 */
	public static Map<String,Object> genReturnMap(Hint hint){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", hint.getCodeString());
        returnMap.put("resultDesc", hint.getMessage());
		return returnMap;
	}
	
	/**
	 * 
	 * @Description: 生成结果信息
	 * @Title: genReturnMap 
	 * @param returnMap
	 * @param hint void
	 */
	public static Map<String,Object> genReturnMap(Map<String,Object> returnMap,Hint hint){
		returnMap.put("resultCode", hint.getCodeString());
        returnMap.put("resultDesc", hint.getMessage());
        return returnMap;
	}

	/**
	 * 
	 * @Description: 获取映射提示信息
	 * @Title: getMappingHintMessage 
	 * @param interfaceCode 接口调用名称
	 * @param map 带有原提示信息的map
	 * @return  Map<String,Object>
	 */
	public static Map<String, Object> getMappingHintMessage(String interfaceCode,Map<String, Object> map) {
		String message=Propertie.H5HINT.value(interfaceCode.concat("_").concat(map.get("resultCode").toString()));
		if(!message.trim().equals("")){
			map.put("resultDesc", message);
		}
		return map;
	}
	/**
	 * xx,xx,xx格式字符串转化为map
	 * @param param
	 * @return
	 * @throws Exception
     */
	public static Map<String, String> stringToMap(String param) throws Exception {
		param = String.valueOf(param).trim();
		if(ValidateUtils.isEmpty(param)){
			return null;
		}
		String[] array = param.split(",");
		if(array.length == 0){
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for(String str : array){
			map.put(String.valueOf(str).trim(), str);
		}
		return map;
	}

	/**
	 * 获取接口防止并发Key（存Redis中）
	 * @param interfaceCode 接口代码
	 * @param dataMap 数据map
	 * @return redis key
	 * @throws Exception
     */
	public static String getPreventConcurrentKey(String interfaceCode, Map<String, Object> dataMap) throws Exception {
		String preventConcurrentKey = "";
		interfaceCode = String.valueOf(interfaceCode).trim();
		if(ValidateUtils.isEmpty(interfaceCode) || null == dataMap || dataMap.size() == 0){
			return preventConcurrentKey;
		}
		String preventConcurrentRules = String.valueOf(Propertie.APPLICATION.value("prevent.concurrent.rules")).trim();
		if(ValidateUtils.isEmpty(preventConcurrentRules)){
			return preventConcurrentKey;
		}
		String[] interfaceArr = preventConcurrentRules.split("\\|");
		if(interfaceArr.length == 0){
			return preventConcurrentKey;
		}
		String[] codeAndFieldArr;
		Map<String, String> itFaceMap = new HashMap<String, String>();
		for(String itFace : interfaceArr){
			codeAndFieldArr = itFace.split(":");
			if(codeAndFieldArr.length != 2){
				return preventConcurrentKey;
			}
			itFaceMap.put(String.valueOf(codeAndFieldArr[0]).trim(), String.valueOf(codeAndFieldArr[1]).trim());
		}
		if(itFaceMap.containsKey(interfaceCode)){
			preventConcurrentKey = interfaceCode.concat("_").concat(String.valueOf(dataMap.get(itFaceMap.get(interfaceCode))).trim());
		}
		return preventConcurrentKey;
	}
	
	
	/**
	 * 汉字转码
	 * @param str
	 * @return
	 */
	public static String getEscape(String str) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(str.length() * 6);
		for (i = 0; i < str.length(); i++) {
			j = str.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
	
	//URL参数转换为Map
	public static Map<String,Object> urlParamsToMap(String params) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		if(StringUtils.isBlank(params)){
			return result;
		}
		String[] paramsArray = params.split("&");
		for(int i=0;i<paramsArray.length;i++){
			String[] array = paramsArray[i].split("=");
			if(array.length ==2){
				result.put(array[0], array[1]);
			}else if(array.length ==1){
				result.put(array[0], "");
			}
		}
		return result;
	}
}
