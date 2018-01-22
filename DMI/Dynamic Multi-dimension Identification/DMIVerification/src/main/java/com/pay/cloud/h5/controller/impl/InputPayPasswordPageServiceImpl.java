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
 * Created by yacheng.ji on 2016/5/18.
 */
@Service("inputPayPasswordPageServiceImpl")
public class InputPayPasswordPageServiceImpl implements DoPageService {

    private static final Logger logger = Logger.getLogger(InputPayPasswordPageServiceImpl.class);

    public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
    	if(logger.isInfoEnabled()){
    		logger.info("进入输入支付密码验证页面");
    	}
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		String cardnums = request.getParameter("cardnums");
		if(logger.isInfoEnabled()){
			logger.info("进入输入支付密码验证页面，cardnums："+cardnums);
		}
		resultMap.put("cardnums", cardnums);
    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    	resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	
    	return resultMap;
    }

    public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
        return null;
    }
}
