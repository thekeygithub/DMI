package com.pay.cloud.h5.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.pay.cloud.h5.controller.DoPageService;
/***
 * 把参数代入到社保卡信息页面
 * @author enjuan.ren
 *
 */
@Service("fillOutSocialInfoImpl")
public class FillOutSocialInfoImpl implements DoPageService{
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		String cityId=request.getParameter("cityId");
		String cityName=request.getParameter("cityName")==null?"":request.getParameter("cityName");
		String cardName=request.getParameter("cardName");
		String fieldList=request.getParameter("fieldList");
		String[] ss=fieldList.split(",");
		receiveMap.put("cityId", cityId);
		receiveMap.put("cityName", "");
		try{
			receiveMap.put("cityName", new String(cityName.getBytes("iso8859-1"),"utf-8"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		receiveMap.put("cardName", cardName);
		receiveMap.put("fieldListLength", ss.length);
		receiveMap.put("resultCode", "0");
		receiveMap.put("resultDesc", "操作成功");
		return receiveMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
