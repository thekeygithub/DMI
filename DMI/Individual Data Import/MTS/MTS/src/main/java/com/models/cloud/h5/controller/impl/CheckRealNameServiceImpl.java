package com.models.cloud.h5.controller.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.hint.Hint;
/**
 * 倒计时
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月28日 
 * @time 上午10:02:40
 * @version V1.0
 * @修改记录
 *
 */
@Service("checkRealNameServiceImpl")
public class CheckRealNameServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(CheckRealNameServiceImpl.class);
	
	@Resource
	PayUserServiceGWImpl payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("检验是否已实名认证");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
    	resultMap = payUserServiceGWImpl.checkPayPwd((Long) sessionMap.get("accountId"));
    	if(logger.isInfoEnabled()){
    		logger.info("请求接口验证是否设置支付密码，返回结果："+resultMap);
    		logger.info("订单号payOrderId："+(String) sessionOrderMap.get("payOrderId"));
    	}
    	if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
    		return resultMap;
    	}
    	//检查是否实名认证(0否，1已实名认证)
		if(sessionMap.get("realNameStatus") == null || sessionMap.get("realNameStatus").equals("0")){
			//未实名认证
			if(logger.isInfoEnabled()){
				logger.info("未实名认证");
			}
			resultMap.put("resultCode", "8888");
			return resultMap;
		}
		if(logger.isInfoEnabled()){
			logger.info("已实名认证");
		}
		
		
    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    	resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	return resultMap;
		}

}
