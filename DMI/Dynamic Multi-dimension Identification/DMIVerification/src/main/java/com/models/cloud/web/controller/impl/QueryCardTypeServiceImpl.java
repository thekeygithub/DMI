package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.BankCardNoTypeInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;

/**
 * 根据银行卡号，查询卡类型（判断跳转到添加储蓄卡或者信用卡页面）
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月19日 
 * @time 下午4:00:12
 * @version V1.0
 * @修改记录
 *
 */
@Service("pcQueryCardTypeServiceImpl")
public class QueryCardTypeServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(QueryCardTypeServiceImpl.class);
	
	@Resource
	private BankCardNoTypeInterfaceImpl bankCardNoTypeInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("查询银行卡类型");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)sessionOrderMap.get("payToken");
		String cardNo = (String)map.get("cardNo");//银行卡卡号
		String hardwareId = (String)map.get("hardwareId");//硬件id
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
//		reqMap.put("cardNo", cardNo);
		reqMap.put("payToken", payToken);
		reqMap.put("payOrderId", payOrderId);
		if(logger.isInfoEnabled()){
			logger.info("查询银行卡类型，传入参数："+reqMap);
		}
		
		//1.查询银行卡类型
		Map<String,Object> queryCardMap = new HashMap<String,Object>();
		queryCardMap.put("interfaceCode", "cardNoType");
		queryCardMap.put("accountId", accountId);
		queryCardMap.put("cardNo", cardNo);
		queryCardMap.put("payToken", payToken);
		queryCardMap.put("hardwareId", hardwareId);//硬件id
//		if(logger.isInfoEnabled()){
//			logger.info("请求 "+queryCardMap.get("interfaceCode")+" 接口，请求参数："+queryCardMap);
//		}
		
		resultMap = bankCardNoTypeInterfaceImpl.doService(queryCardMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+queryCardMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)queryCardMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		
		
		return resultMap;
	}

}
