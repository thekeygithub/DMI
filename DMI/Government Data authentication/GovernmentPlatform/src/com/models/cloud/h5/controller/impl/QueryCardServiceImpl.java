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
import com.models.cloud.gw.protocolfactory.impl.QueryCardListInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.pay.payuser.entity.ActPerson;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 查询绑卡列表
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月13日 
 * @time 下午3:26:50
 * @version V1.0
 * @修改记录
 *
 */
@Service("queryCardServiceImpl")
public class QueryCardServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(QueryCardServiceImpl.class);
	
	@Resource
	QueryCardListInterfaceImpl queryCardListInterfaceImpl;
	@Resource
	PayUserServiceGWImpl payUserServiceGWImpl;
	
	@Resource
	VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		
		return null;
	}
	
	
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("查询银行卡列表");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)map.get("payToken");
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payToken", payToken);
		reqMap.put("payOrderId", payOrderId);
		
		if(logger.isInfoEnabled()){
			logger.info("查询银行卡列表，传入参数："+reqMap);
		}
		
		resultMap = payUserServiceGWImpl.checkPayPwd((Long) sessionMap.get("accountId"));
	
		if(logger.isInfoEnabled()){
			logger.info("请求接口验证是否设置支付密码，返回结果："+resultMap);
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
		
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("interfaceCode", "queryCardList");
		queryMap.put("accountId", accountId);
		queryMap.put("payToken", payToken);
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			if(logger.isInfoEnabled()){
				logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，请求参数："+queryMap);
			}
			
			resultMap = queryCardListInterfaceImpl.doService(queryMap);
			if(logger.isInfoEnabled()){
				logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
			}
			
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)queryMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		
		return resultMap;
	}
	
	
}
