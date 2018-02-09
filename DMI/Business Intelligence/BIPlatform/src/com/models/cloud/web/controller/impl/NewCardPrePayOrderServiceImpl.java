package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.PrePaymentVerifyInterfaceImpl;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.web.controller.DoWebPageService;
/**
 *  新输入的银行卡预支付获取短信验证码 
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月20日 
 * @time 下午2:05:09
 * @version V1.0
 * @修改记录
 *
 */
@Service("pcNewCardPrePayOrderServiceImpl")
public class NewCardPrePayOrderServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(NewCardPrePayOrderServiceImpl.class);
	
	@Resource
	private PrePaymentVerifyInterfaceImpl prePaymentVerifyInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("新卡预支付获取短信验证码 ");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payToken = (String)sessionOrderMap.get("payToken");
		String merOrderId = (String)sessionOrderMap.get("merOrderId");//商户订单号
		String cardNo = (String)map.get("cardNo");//银行卡卡号
		String paymentType = (String)map.get("paymentType");//支付类型（2：储蓄卡 3：信用卡）
		String idCard = (String)map.get("idCard");//身份证号
		String owner = (String)map.get("owner");//姓名
		String phone = (String)map.get("phone");//手机号
		String hardwareId = (String)map.get("hardwareId");//硬件id
		String ipInfo = (String)map.get("ipInfo");//ip
		
		//信用卡
		String cvv2 = (String)map.get("cvv2");//cvv2
		String validThru = (String)map.get("validThru");//信用卡有效期，
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
//		reqMap.put("cardNo", cardNo);
		reqMap.put("payToken", payToken);
		reqMap.put("payOrderId", payOrderId);
		reqMap.put("merOrderId", merOrderId);
		reqMap.put("paymentType", paymentType);
		reqMap.put("idCard", idCard);
		reqMap.put("owner", owner);
		reqMap.put("phone", phone);
		reqMap.put("cvv2", cvv2);
		reqMap.put("hardwareId", hardwareId);//硬件id
		reqMap.put("remoteAddr", ipInfo); // ip
		
		if(logger.isInfoEnabled()){
			logger.info("新卡预支付获取短信验证码，传入参数："+reqMap);
		}
		
		Map<String,Object> prePayMap = new HashMap<String,Object>();
		prePayMap.put("interfaceCode", "prePaymentVerify");
		prePayMap.put("accountId", accountId);
		prePayMap.put("cardNo", cardNo);
		prePayMap.put("payToken", payToken);
		prePayMap.put("payOrderId", payOrderId);
		prePayMap.put("merOrderId", merOrderId);
		prePayMap.put("paymentType", paymentType);
		prePayMap.put("idCard", idCard);
		prePayMap.put("owner", owner);
		prePayMap.put("phone", phone);
		prePayMap.put("cvv2", cvv2);
		prePayMap.put("validThru", validThru);
		prePayMap.put("idCardType", BaseDictConstant.CERT_TYPE_ID_SHENFENZHENG);
		prePayMap.put("hardwareId", hardwareId);//硬件id
		prePayMap.put("remoteAddr", ipInfo); // ip
		
//		if(logger.isInfoEnabled()){
//			logger.info("请求 "+prePayMap.get("interfaceCode")+" 接口，请求参数："+prePayMap);
//		}
		resultMap = prePaymentVerifyInterfaceImpl.doService(prePayMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+prePayMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)prePayMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		return resultMap;
	}

}
