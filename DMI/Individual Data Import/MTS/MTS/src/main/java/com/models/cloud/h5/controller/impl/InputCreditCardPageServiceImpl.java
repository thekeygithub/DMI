package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.hint.Hint;
import com.mysql.jdbc.StringUtils;
/**
 * 填写信用卡信息
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月22日 
 * @time 下午3:49:44
 * @version V1.0
 * @修改记录
 *
 */
@Service("inputCreditCardPageServiceImpl")
public class InputCreditCardPageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(InputCreditCardPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("进入输入信用卡信息页面");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String bankIconUrl = request.getParameter("bankIconUrl");
		String cardTypeName = request.getParameter("cardTypeName");
		String bankName = request.getParameter("bankName");
		String bankCode = request.getParameter("bankCode");
		String cardNo = request.getParameter("cardNo");
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("bankIconUrl", bankIconUrl);
		reqMap.put("cardTypeName", cardTypeName);
		reqMap.put("bankName", bankName);
		reqMap.put("bankCode", bankCode);
//		reqMap.put("cardNo", cardNo);
		
		if(logger.isInfoEnabled()){
			logger.info("进入输入信用卡信息页面，传入参数:"+reqMap);
		}
		
		if(StringUtils.isNullOrEmpty(cardNo)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString().replace("{param}", "cardNo"));
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage());
			return resultMap;
		}
		
		cardNo = cardNo.replaceAll(" ", "");
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("bankIconUrl", bankIconUrl);
		resultMap.put("cardTypeName", cardTypeName);
		resultMap.put("bankName", bankName);
		resultMap.put("bankCode", bankCode);
		resultMap.put("cardNo", cardNo);
		resultMap.put("paymentType", BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD);
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
