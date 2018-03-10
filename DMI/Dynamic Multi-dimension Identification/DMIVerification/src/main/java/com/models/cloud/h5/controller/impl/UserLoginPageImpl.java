package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.LoginImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 用户登录
 * @ClassName: UserLoginPageImpl 
 * @author: zhengping.hu
 * @date: 2016年5月18日 上午10:37:58
 */
@Service("userLoginPageImpl")
public class UserLoginPageImpl implements DoPageService{
	
	private static final Logger logger = Logger.getLogger(UserLoginPageImpl.class);
	
	@Resource(name="loginImpl")
	private LoginImpl loginImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		try {
			Map<String, Object> returnmap= loginImpl.doService(receiveMap);
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("resultCode", returnmap.get("resultCode"));
			map.put("resultDesc", returnmap.get("resultDesc"));
			map.put("allowPament", returnmap.get("allowPament"));
			map.put("failCount", returnmap.get("failCount"));
			if(returnmap.get("resultCode").toString().equals("0")){
				returnmap.remove("resultCode");
				returnmap.remove("resultDesc");
				returnmap.put("auth", "1");
				request.getSession().setAttribute(SmsConstant.SESSION_USER_KEY, returnmap);
			}
			return ConvertUtils.getMappingHintMessage("userlogin", map);
		} catch (Exception e) {
			logger.error("用户登录错误：" + e.getMessage(), e);
			Map<String,Object> returnMap =  ConvertUtils.genReturnMap(Hint.USER_25027_LOGIN_EXCEPTION);
    		return ConvertUtils.getMappingHintMessage("userlogin", returnMap);
		}
	}

}
