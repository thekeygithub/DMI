package com.pay.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.protocolfactory.impl.JBCardInterfaceImpl;
import com.pay.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.Md5SaltUtils;
import com.pay.cloud.util.hint.Hint;
/**
 * 验证支付密码，解绑银行卡
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月19日 
 * @time 下午1:36:15
 * @version V1.0
 * @修改记录
 *
 */
@Service("unBindCardServiceImpl")
public class UnBindCardServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(UnBindCardServiceImpl.class);
	
	@Resource
	JBCardInterfaceImpl jbcardInterfaceImpl;
	@Resource
	VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("解绑银行卡");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		String accountId =String.valueOf((Long) sessionMap.get("accountId"));
		String payOrderId = (String) sessionOrderMap.get("payOrderId");
		String payPassword = (String)map.get("payPassword");//支付密码
		String bindId = (String)map.get("bindId");//绑卡id
		String hardwareId  = (String)map.get("hardwareId");

		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("accountId", accountId);
		reqMap.put("payPassword", payPassword);
		reqMap.put("payOrderId", payOrderId);
		reqMap.put("bindId", bindId);
		reqMap.put("hardwareId", hardwareId);
		if(logger.isInfoEnabled()){
			logger.info("解绑银行卡，传入参数："+reqMap);
		}
		
		
		//1.验证支付密码
		Map<String,Object> verifyPwdMap = new HashMap<String,Object>();
		verifyPwdMap.put("interfaceCode", "verifyPayPassword");
		verifyPwdMap.put("accountId", accountId);
		verifyPwdMap.put("payPassword", payPassword);
		verifyPwdMap.put("hardwareId", hardwareId);//硬件id
		if(logger.isInfoEnabled()){
			logger.info("先请求 "+verifyPwdMap.get("interfaceCode")+" 接口，请求参数："+verifyPwdMap);
		}
		
		resultMap = verifyUserPaymentPwdInterfaceImpl.doService(verifyPwdMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		resultMap = ConvertUtils.getMappingHintMessage((String)verifyPwdMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		
		Map<String,Object> unBindMap = new HashMap<String,Object>();
		if(resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			//2.解绑银行卡
			unBindMap.put("interfaceCode", "jbCard");
			unBindMap.put("accountId", accountId);
			unBindMap.put("bindId", bindId);
			unBindMap.put("payToken", resultMap.get("payToken"));
			unBindMap.put("hardwareId", hardwareId);//硬件id
			if(logger.isInfoEnabled()){
				logger.info("再请求 "+unBindMap.get("interfaceCode")+" 接口，请求参数："+unBindMap);
			}
			
			resultMap = jbcardInterfaceImpl.doService(unBindMap);
				
			if(logger.isInfoEnabled()){
				logger.info("请求 "+unBindMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
			}
			
			resultMap = ConvertUtils.getMappingHintMessage((String)unBindMap.get("interfaceCode"),resultMap);
			if(logger.isInfoEnabled()){
				logger.info("错误码转换结果："+resultMap);
			}
			
		}
		
		return resultMap;
	}

}
