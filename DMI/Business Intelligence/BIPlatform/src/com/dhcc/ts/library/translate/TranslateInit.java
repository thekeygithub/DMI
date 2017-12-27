package com.dhcc.ts.library.translate;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TranslateInit {

	// 在平台申请的APP_ID 详见
	// http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
	private static final String APP_ID = "20170420000045233";
	private static final String SECURITY_KEY = "HskZ2OtF6gUxIcc8AVgD";

	/**
	 * @描述：中英文互译接口
	 * @作者：lwh
	 * @时间：2017年4月20日 下午3:31:04
	 * @param str
	 * resultCode 返回编码 52000 :成功 其他都失败
	 * resultZh 返回中文
	 * resultEn 返回英文
	 *
	 * @return
	 */
	public static Map<String, String> getTransResult(String str) {
		Map<String, String> map = new HashMap<String, String>();
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
		String resultZhJson = api.getTransResult(str, "en", "zh");		//调用接口返回翻译后中文Json
		String resultEnJson = api.getTransResult(str, "zh", "en");		//调用接口返回翻译后英文Json
		JSONObject jsStr = JSONObject.fromObject(resultZhJson);
		JSONObject jsStr1 = JSONObject.fromObject(resultEnJson);
		String code = "52000";
		if (jsStr.has("error_code")||jsStr1.has("error_code")) {		//两次调用接口有任意一次失败返回失败
			code = jsStr.getString("error_code");
		} else {
			JSONArray jsonArray = jsStr.getJSONArray("trans_result");
			String resultZh = jsonArray.getJSONObject(0).get("dst").toString();		//取出翻译后的中文
			JSONArray jsonArray1 = jsStr1.getJSONArray("trans_result");
			String resultEn = jsonArray1.getJSONObject(0).get("dst").toString();	//取出翻译后的英文
			map.put("resultZh", resultZh);
			map.put("resultEn", resultEn);
		}
		map.put("resultCode", code);
		return map;
	}
	
	public static void main(String[] args) {
		
		System.out.println(getTransResult(""));
	}
}
