package com.models.cloud.h5.controller.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
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
@Service("payTimeServiceImpl")
public class PayTimeServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(PayTimeServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("计算倒计时");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
    	String createOrderTime= sessionOrderMap.get("createOrderTime")==null?"0":sessionOrderMap.get("createOrderTime").toString();
    	String payTimes = sessionOrderMap.get("payTimes")==null?"0":sessionOrderMap.get("payTimes").toString();
    	if(!createOrderTime.equals("") && !payTimes.equals("")){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date createOrderTime_d;
			try {
				createOrderTime_d = sdf.parse(createOrderTime);
				Long createOrderTime_l = createOrderTime_d.getTime();
				Date now = new Date();
				Long now_l = now.getTime();
				int payTomes_i = Integer.parseInt(payTimes);
				long loseTimes = (payTomes_i - (now_l-createOrderTime_l)/1000);
				resultMap.put("payTimes",loseTimes);
			} catch (ParseException e) {
				if(logger.isInfoEnabled()){
					logger.error("计算倒计时【抛出异常】");
				}
				e.printStackTrace();
			}
    		
    	}
    	
    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    	resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	
    	return resultMap;
		}

}
