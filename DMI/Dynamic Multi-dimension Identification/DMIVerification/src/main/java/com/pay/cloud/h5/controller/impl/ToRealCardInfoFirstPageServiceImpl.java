package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.hint.Hint;

/**
 * 
 * @author haiyan.zhang
 *
 */
@Service("toRealCardInfoFirstPageServiceImpl")
public class ToRealCardInfoFirstPageServiceImpl implements DoPageService {

    private static final Logger logger = Logger.getLogger(ToRealCardInfoFirstPageServiceImpl.class);

    public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
    	if(logger.isInfoEnabled()){
    		logger.info("进入实名认证引导第一个页面");
    	}
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	String topage = request.getParameter("topage");
		String phone = request.getParameter("phone");
		String isUnBind = request.getParameter("isUnBind");
		String bindId = request.getParameter("bindId");
		if(logger.isInfoEnabled()){
			logger.info("进入实名认证引导第二个页面，topage："+topage+" ,phone："+phone+" ,isUnBind："+isUnBind+" ,bindId："+bindId);
		}
		resultMap.put("topage", topage);
		resultMap.put("phone", phone);
		resultMap.put("isUnBind", isUnBind);
		resultMap.put("bindId", bindId);
    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    	resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	
    	return resultMap;
    }

    public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
        return null;
    }
}
