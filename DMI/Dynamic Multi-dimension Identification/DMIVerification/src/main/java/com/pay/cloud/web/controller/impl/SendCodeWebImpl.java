package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.SmsSendCodeImpl;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 发送验证码
 * @ClassName: SendCodeWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午7:29:07
 */
@Service("sendCodeWebImpl")
public class SendCodeWebImpl implements DoWebPageService{
	
	@Resource(name="smsSendCodeImpl")
	private SmsSendCodeImpl smsSendCodeImpl;
	
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
			Map<String, Object> SessionMap=(Map<String, Object>)request.getSession().getAttribute(SmsConstant.SESSION_USER_KEY);
			if(!receiveMap.get("operateType").toString().equals("1")&&!receiveMap.get("operateType").toString().equals("3")){
				receiveMap.put("userPhone", SessionMap.get("phone"));
				receiveMap.putAll(SessionMap);
			}
		}
		
		Map<String,Object> returnMap =  smsSendCodeImpl.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("sendmessage", returnMap);
	}

}
