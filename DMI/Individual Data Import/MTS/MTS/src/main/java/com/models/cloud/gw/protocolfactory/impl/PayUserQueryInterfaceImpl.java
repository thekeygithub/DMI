package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;

@Service("payUserQueryInterfaceImpl")
public class PayUserQueryInterfaceImpl  implements DoServiceInterface{

	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;

	//开始验证各种信息
	//验证通过
	//验证不通过返回错误码
	public Map<String,Object> doService(Map<String, Object> receiveMap){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

}
