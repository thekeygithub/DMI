package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.SmsSendCodeImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;

/**
 * 实名认证发送短信验证码
 * @author haiyan.zhang
 *
 */
@Service("sendCodeToSMRZPageImpl")
public class SendCodeToSMRZPageImpl implements DoPageService{
	private Logger logger = Logger.getLogger(QueryCardServiceImpl.class);
	
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
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("实名认证发送短信验证码");
		}
		Map<String,Object> sendMap = new HashMap<>();
		sendMap.put("interfaceCode", "sendmessage");
		sendMap.put("phone", String.valueOf(map.get("phone")).trim());
		sendMap.put("userPhone", String.valueOf(map.get("phone")).trim());
		sendMap.put("operateType", 5);
		sendMap.put("hardwareId", String.valueOf(map.get("hardwareId")).trim());
		sendMap.put("appId", String.valueOf(map.get("appId")).trim());
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+sendMap.get("interfaceCode")+" 接口，请求参数："+sendMap);
		}

		Map<String, Object> resultMap = smsSendCodeImpl.doService(sendMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+sendMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage("sendmessage", resultMap);
		if(logger.isInfoEnabled()){
			logger.info("转换后的结果："+resultMap);
		}
		return resultMap;
	}
}
