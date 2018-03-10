package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.RegisterImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 用户注册
 * @ClassName: RegistWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("registWebImpl")
public class RegistWebImpl implements DoWebPageService{
	
	@Resource(name="registerImpl")
	private RegisterImpl registerImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		Map<String, Object> returnmap= registerImpl.doService(receiveMap);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("resultCode", returnmap.get("resultCode"));
		map.put("resultDesc", returnmap.get("resultDesc"));
		
		if(returnmap.get("resultCode").toString().equals("0")){
			returnmap.remove("resultCode");
			returnmap.remove("resultDesc");
			returnmap.put("auth", "1");
			request.getSession().setAttribute(SmsConstant.SESSION_USER_KEY, returnmap);
		}
		return ConvertUtils.getMappingHintMessage("registeruser", map);
	}

}
