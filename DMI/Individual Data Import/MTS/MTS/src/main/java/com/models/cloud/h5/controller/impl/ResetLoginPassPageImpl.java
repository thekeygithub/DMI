package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.LoginPassImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;

/**
 * 
 * @Description: 重置登录密码
 * @ClassName: resetLoginPassPageImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("resetLoginPassPageImpl")
public class ResetLoginPassPageImpl implements DoPageService{
	
	@Resource(name="loginPassImpl")
	private LoginPassImpl loginPassImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		Map<String,Object> returnMap = loginPassImpl.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("resetloginpassword", returnMap);
	}

}
