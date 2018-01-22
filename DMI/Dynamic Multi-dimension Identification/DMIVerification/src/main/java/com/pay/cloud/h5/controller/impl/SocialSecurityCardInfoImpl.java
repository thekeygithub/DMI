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
import com.pay.cloud.gw.protocolfactory.impl.SocialSecurityQueryMoneyInterfaceImpl;
import com.pay.cloud.h5.controller.DoPageService;
/**
 * 社保卡信息
 * @author enjuan.ren
 *
 */
@Service("socialSecurityCardInfoImpl")
public class SocialSecurityCardInfoImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SocialSecurityCardInfoImpl.class);
	
	@Resource(name="socialSecurityQueryMoneyInterfaceImpl")
	SocialSecurityQueryMoneyInterfaceImpl cialSecurityQueryMoneyInterfaceImpl;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String cardNo=request.getParameter("cardNo");//身份证号
		String cardName=request.getParameter("cardName");//姓名
		String  socialSecurityNo=request.getParameter("socialSecurityNo");//社保卡号
		String socialSecurityId=request.getParameter("socialSecurityId");//社保卡id
		String socialNo="";
		if (socialSecurityNo.length()>4) {
			//截取社保卡后4位
			socialNo=socialSecurityNo.substring(socialSecurityNo.length()-4, socialSecurityNo.length());
		}
		//通过用户id和社保卡卡号id,获取医保卡账户余额
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		receiveMap.put("socialSecurityId", socialSecurityId);
		receiveMap.put("userId", userId);
		
		if(logger.isInfoEnabled()){
			logger.info("社保卡信息查询");
		}
		Map<String, Object> returnMap=cialSecurityQueryMoneyInterfaceImpl.doService(receiveMap);
		
		resultMap.put("cardNo", cardNo);
		resultMap.put("cardName", "");
		if(cardName!=null){
			try{
				resultMap.put("cardName", new String(cardName.getBytes("iso8859-1"),"utf-8"));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		resultMap.put("socialSecurityNo",  socialNo);
		resultMap.put("money", returnMap.get("money"));
		resultMap.put("socialSecurityId", socialSecurityId);
		resultMap.put("resultCode", "0");
		resultMap.put("resultDesc", "操作成功");
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}

}
