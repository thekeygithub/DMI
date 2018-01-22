package com.pay.cloud.web.controller.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.gw.protocolfactory.impl.VerifyCodeListInterfaceImpl;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.web.controller.DoWebPageService;
/***
 * 登录账号错误时，读取验证码信息
 * @author enjuan.ren
 *
 */
@Service("getWebVerifyCodeListImpl")
public class GetWebVerifyCodeListImpl implements DoWebPageService{
	private Logger logger = Logger.getLogger(GetWebVerifyCodeListImpl.class);
	
	@Resource(name="verifyCodeListInterfaceImpl")
	VerifyCodeListInterfaceImpl verifyCodeListInterfaceImpl;
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("获取登录验证码 ");
		}
		String phone=(String)receiveMap.get("phone");
		String hardwareId = request.getSession().getId();
		receiveMap.put("interfaceCode", "verifyCodeList");
		receiveMap.put("hardwareId", hardwareId);//硬件id
		receiveMap.put("phone", phone);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，请求参数："+receiveMap);
		}
		Map<String,Object> returnMap =  verifyCodeListInterfaceImpl.doService(receiveMap);
		
		@SuppressWarnings("unchecked")
		List<String> verCodeList = (List<String>) returnMap.get("verifyCodeList");
		if(verCodeList!=null && verCodeList.size()>0){
			request.getSession().setAttribute("verCodeStatus", "1");
			request.getSession().setAttribute("verCodeList", verCodeList);
			returnMap.put("verCodeCount", verCodeList.size());
		}else{
			returnMap.put("verCodeCount", 0);
		}
		returnMap.remove("verifyCodeList");
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return ConvertUtils.getMappingHintMessage("getWebVerifyCodeList", returnMap);
	}

}
