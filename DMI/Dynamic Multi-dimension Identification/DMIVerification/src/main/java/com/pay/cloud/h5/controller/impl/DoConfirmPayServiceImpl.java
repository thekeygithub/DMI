package com.pay.cloud.h5.controller.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yacheng.ji on 2016/5/18.
 */
@Service("doConfirmPayServiceImpl")
public class DoConfirmPayServiceImpl implements DoPageService {

    private static final Logger logger = Logger.getLogger(DoConfirmPayServiceImpl.class);
    @Resource
    private DoServiceInterface confirmPayOrderInterfaceImpl;

    public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
        return null;
    }

    public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
    	if(logger.isInfoEnabled()){
            logger.info("已绑卡，确认支付 ");
        }
    	Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)sessionOrderMap.get("payToken");
		String hardwareId = (String)map.get("hardwareId");
		
		
		Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("interfaceCode", "");
        inputMap.put("validateCode", map.get("validateCode"));
        inputMap.put("validateCode", map.get("validateCode"));
        inputMap.put("accountId", map.get("accountId"));
        if(logger.isInfoEnabled()){
            logger.info("已绑卡，确认支付，输入参数：" + inputMap.toString());
        }
        resultMap = confirmPayOrderInterfaceImpl.doService(inputMap);
        if(logger.isInfoEnabled()){
            logger.info("<<接口处理结果：" + resultMap.toString());
        }
        
        resultMap = ConvertUtils.getMappingHintMessage((String)inputMap.get("interfaceCode"),resultMap);
        return resultMap;
    }
}
