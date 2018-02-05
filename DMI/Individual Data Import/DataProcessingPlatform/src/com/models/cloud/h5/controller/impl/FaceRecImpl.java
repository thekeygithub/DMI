package com.models.cloud.h5.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.FaceRecInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
/***
 * 人脸识别验证
 * @author enjuan.ren
 *
 */
@Service("faceRecImpl")
public class FaceRecImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(FaceRecImpl.class);
	
	@Resource(name="faceRecInterfaceImpl")
	FaceRecInterfaceImpl faceRecInterImpl;
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		String socialSecurityId=(String)receiveMap.get("socialSecurityId");
		String imageStr=(String)receiveMap.get("imageStr");
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		Long userId=(Long) sessionMap.get("userId");
		receiveMap.put("idCard", socialSecurityId);
		receiveMap.put("imageStr", imageStr);
		receiveMap.put("userId", userId);
		receiveMap.put("interfaceCode", "faceRec");
		if(logger.isInfoEnabled()){
			logger.info("人脸识别验证");
		}
		Map<String, Object> returnMap=faceRecInterImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		return ConvertUtils.getMappingHintMessage("faceRec", returnMap);
	}

}
