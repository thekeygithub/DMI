package com.ts.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ts.entity.app.SysParamCheck;
import com.ts.util.app.SessionAppMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ValidateParams {
	
	static Logger logger = Logger.getLogger(ValidateParams.class);
	/**参数校验
	 * @param inputJson
	 * @return
	 */
	public static Map<String, String> validateParams(JSONObject data, Set<String> showField){
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "true");
		if(showField != null){//这里判断下这个接口必传参数
			Iterator<String> iterator = showField.iterator();  
			while (iterator.hasNext()) {  
				String colName = iterator.next();  
				Set<String> set = SessionAppMap.dataParamRuleMapMap.get(colName);
				if(set == null || set.size() <= 0){
					continue;
				}
				for (String checkId : set) {//遍历不为空参数规则
					SysParamCheck sysParamCheck = SessionAppMap.paramRuleMap.get(checkId);
					if(sysParamCheck == null){
						continue;
					}
					if(ParamCheckType.T2Notnull.getCheckType().equals(sysParamCheck.getCheckType())){
						if(!data.toString().contains(colName)){//这里只看是否传参数
							map.put("result", "false");
							map.put("msg", colName + ParamCheckType.T2Notnull.getFailMsg());
							logger.info(String.format("参数：%s 校验规则：%s ；", colName, sysParamCheck.getCheckName()));
		            		return map;
						}
					}
				}
			} 
		}
		map = whileJson(data, map);
		return map;
	}
	private static Map<String, String> whileJson(JSONObject data, Map<String, String> map) {
		Iterator iterator = data.keys();
		while(iterator.hasNext()){
            String key = (String) iterator.next();
            Object paramValue = data.get(key);
            if(paramValue == null){
            	continue;
            }
            String classStr = paramValue.getClass().getName();
            if(classStr.contains("JSONObject")){//json类型   
            	JSONObject json = (JSONObject)paramValue;
            	map = whileJson(json, map);
            }
            if(classStr.contains("JSONArray")){
            	JSONArray jsonarr = (JSONArray)paramValue;
            	map = whileJsonAarray(jsonarr, map);
            }
            map = validOne(map, key, paramValue);
            if("false".equals(map.get("result"))){
            	return map;
            }
		}
		return map;
	}
	private static Map<String, String> whileJsonAarray(JSONArray jsonarr, Map<String, String> map) {
		if(jsonarr == null){
			return map;
		}
		for (Object paramValue : jsonarr) {
			if(paramValue == null){
            	continue;
            }
            String classStr = paramValue.getClass().getName();
            if(classStr.contains("JSONObject")){//json类型   
            	JSONObject json = (JSONObject)paramValue;
            	map = whileJson(json, map);
            }
            if(classStr.contains("JSONArray")){
            	JSONArray jsonarrTmp = (JSONArray)paramValue;
            	map = whileJsonAarray(jsonarrTmp, map);
            }
		}
		return map;
	}
	private static Map<String, String> validOne(Map<String, String> map, String key, Object paramValue) {
		Set<String> set = SessionAppMap.dataParamRuleMapMap.get(key);
		if(set == null || set.size() <= 0){
			return map;
		}
		for (String checkId : set) {
			SysParamCheck sysParamCheck = SessionAppMap.paramRuleMap.get(checkId);
			if(sysParamCheck == null){
				continue;
			}
			ParamCheckType thisCheckType = ParamCheckType.map.get(sysParamCheck.getCheckType());
			String thisCheckValue = sysParamCheck.getCheckValue();
			if(!ParamCheckType.validateOneParam(thisCheckType, thisCheckValue, paramValue)){
				map.put("result", "false");
				map.put("msg", key + thisCheckType.getFailMsg());
				logger.info(String.format("参数：%s 校验规则：%s 失败；", key, sysParamCheck.getCheckName()));
				return map;
			}
		}
		return map;
	}
}