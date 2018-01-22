package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SetPaymentPassImpl;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 设置支付密码（注册）
 * @ClassName: SetPaymentPassWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("setPaymentPassWebImpl")
public class SetPaymentPassWebImpl implements DoWebPageService{
	
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
		}
		Map<String,Object> returnMap =  setPaymentPassImpl.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("setPayPassword", returnMap);
	}

}
