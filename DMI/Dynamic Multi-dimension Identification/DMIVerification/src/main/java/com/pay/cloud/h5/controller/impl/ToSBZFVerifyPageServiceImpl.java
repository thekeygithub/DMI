package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
@Service("toSBZFVerifyPageServiceImpl")
public class ToSBZFVerifyPageServiceImpl implements DoPageService {

    private static final Logger logger = Logger.getLogger(ToSBZFVerifyPageServiceImpl.class);

    public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
    	if(logger.isInfoEnabled()){
    		logger.info("进入验证医保支付账户页面");
    	}
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		String bankIcon = request.getParameter("bankIcon");
		String bankTypeName = request.getParameter("bankTypeName");
		String bankName = request.getParameter("bankName");
		String realNameBankNo = request.getParameter("realNameBankNo");
		String realNamePhone = request.getParameter("realNamePhone");
		
		String phone = "";
		if(StringUtils.isNotBlank(realNamePhone)){
			phone = realNamePhone.substring(0, 3)+"****"+realNamePhone.substring(realNamePhone.length()-4, realNamePhone.length());
			resultMap.put("phone", phone);
		}
		
		resultMap.put("bankIcon", bankIcon);
		resultMap.put("bankTypeName", bankTypeName);
		resultMap.put("bankName", bankName);
		resultMap.put("realNameBankNo", realNameBankNo);
		resultMap.put("realNamePhone", realNamePhone);
    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    	resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	
    	return resultMap;
    }

    public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
        return null;
    }
}
