package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.SmsValidateCodeImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 验证验证码
 * @ClassName: ValidateCodeWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午7:29:07
 */
@Service("validateCodeWebImpl")
public class ValidateCodeWebImpl implements DoWebPageService{
	
	@Resource(name="smsValidateCodeImpl")
	private SmsValidateCodeImpl smsValidateCodeImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		Map<String,Object> returnMap =  smsValidateCodeImpl.doService(receiveMap);
		return ConvertUtils.getMappingHintMessage("verifymessage", returnMap);
	}

}
