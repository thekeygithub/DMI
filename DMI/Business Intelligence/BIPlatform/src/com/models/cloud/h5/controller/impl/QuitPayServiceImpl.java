package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.QuitPayOrderInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
/**
 * 手动放弃支付,需要修改订单状态
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月28日 
 * @time 上午10:02:40
 * @version V1.0
 * @修改记录
 *
 */
@Service("quitPayServiceImpl")
public class QuitPayServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(QuitPayServiceImpl.class);
	
	@Resource
	private QuitPayOrderInterfaceImpl quitPayOrderInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("手动放弃支付");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");

		Map<String,Object> quitMap = new HashMap<String,Object>();
		quitMap.put("interfaceCode", "quitPayOrder");
		quitMap.put("accountId", accountId);
		quitMap.put("payOrderId", payOrderId);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+quitMap.get("interfaceCode")+" 接口，请求参数："+quitMap);
		}
		
		resultMap = quitPayOrderInterfaceImpl.doService(quitMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+quitMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		 //resultMap = ConvertUtils.getMappingHintMessage((String)quitMap.get("interfaceCode"),resultMap);
		return resultMap;
		}

}
