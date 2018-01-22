package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.ConfirmPayOrderInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;

/**
 * 添加新卡确认支付
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月20日 
 * @time 下午5:38:47
 * @version V1.0
 * @修改记录
 *
 */
@Service("newCardConfirmPayOrderServiceImpl")
public class NewCardConfirmPayOrderServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(UnBindCardServiceImpl.class);

	@Resource
	private ConfirmPayOrderInterfaceImpl confirmPayOrderInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("确认支付 ");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)sessionOrderMap.get("payToken");
		String validThru = (String)map.get("validThru");//
		String cvv2 = (String)map.get("cvv2");//
		String validateCode = (String)map.get("validateCode");//验证码
		String hardwareId = (String)map.get("hardwareId");//硬件id
		
		Map<String,Object> confirmMap = new HashMap<String,Object>();
		confirmMap.put("interfaceCode", "confirmPayOrder");
		confirmMap.put("accountId", accountId);
		confirmMap.put("payToken", payToken);
		confirmMap.put("payOrderId", payOrderId);
		confirmMap.put("validateCode", validateCode);
		confirmMap.put("hardwareId", hardwareId);//硬件id
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+confirmMap.get("interfaceCode")+" 接口，请求参数："+confirmMap);
		}
		confirmMap.put("validThru", validThru);
		confirmMap.put("cvv2", cvv2);
		resultMap = confirmPayOrderInterfaceImpl.doService(confirmMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+confirmMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)confirmMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		return resultMap;
		
	}

}
