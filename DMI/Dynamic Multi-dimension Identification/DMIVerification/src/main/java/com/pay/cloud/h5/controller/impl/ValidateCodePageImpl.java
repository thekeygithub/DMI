package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.ConfirmPaymentInterfaceImpl;
import com.pay.cloud.gw.protocolfactory.impl.QueryTradeInfoInterfaceImpl;
import com.pay.cloud.gw.protocolfactory.impl.SmsValidateCodeImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.DimDictEnum;
import com.pay.cloud.util.hint.Hint;

/**
 * 
 * @Description: 验证验证码
 * @ClassName: ValidateCodePageImpl 
 * @author: zhengping.hu
 * @date: 2016年5月19日 下午7:29:07
 */
@Service("validateCodePageImpl")
public class ValidateCodePageImpl implements DoPageService{
	private Logger logger = Logger.getLogger(ValidateCodePageImpl.class);
	
	@Resource(name="smsValidateCodeImpl")
	private SmsValidateCodeImpl smsValidateCodeImpl;

	@Resource
	private ConfirmPaymentInterfaceImpl confirmPaymentInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		Map<String,Object> returnMap =  smsValidateCodeImpl.doService(receiveMap);
		returnMap = ConvertUtils.getMappingHintMessage("verifymessage", returnMap);
		if(logger.isInfoEnabled()){
			logger.info("请求verifymessage接口，错误码转换结果："+returnMap);
		}
		
		return returnMap;
	}

}
