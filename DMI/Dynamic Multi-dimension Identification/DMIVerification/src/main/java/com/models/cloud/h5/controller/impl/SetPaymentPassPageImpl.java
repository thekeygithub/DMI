package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.SetPaymentPassImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;

/**
 * 
 * @Description: 设置支付密码（注册）
 * @ClassName: SetPaymentPassPageImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("setPaymentPassPageImpl")
public class SetPaymentPassPageImpl implements DoPageService{
	
	@Resource(name="setPaymentPassImpl")
	private SetPaymentPassImpl setPaymentPassImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		if(request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY)!=null){
			Map<String,Object> sessionMap = (Map<String, Object>) request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY);
			receiveMap.put("userId", sessionMap.get("userId"));
			receiveMap.put("interfaceCode","setPayPassword");
			receiveMap.put("phone", sessionMap.get("userCode"));
		}
		Map<String,Object> returnMap =  setPaymentPassImpl.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("setPayPassword", returnMap);
	}

}
