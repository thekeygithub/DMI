package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SetPaymentPassImpl3;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 重置支付密码（已绑卡）
 * @ClassName: ResetPaymentPassBindCardWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午4:40:58
 */
@Service("resetPaymentPassBindCardWebImpl")
public class ResetPaymentPassBindCardWebImpl implements DoWebPageService{
	
	@Resource(name="setPaymentPassImpl3")
	private SetPaymentPassImpl3 setPaymentPassImpl3;
	
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
			receiveMap.put("interfaceCode","setPayPassword3");
		}
		Map<String,Object> returnMap =  setPaymentPassImpl3.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("setPayPassword3", returnMap);
	}

}
