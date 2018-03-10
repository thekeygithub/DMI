package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.FaceRecServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 人脸识别验证
 * @ClassName: FaceRecInterfaceImpl 
 * @author: li.zhou
 * @date: 2016年8月15日 下午3:55:03
 */
@Service("faceRecInterfaceImpl")
public class FaceRecInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(FaceRecInterfaceImpl.class);
	
	@Resource(name="faceRecServiceGWImpl")
	private FaceRecServiceGW faceRecServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String userId = String.valueOf(receiveMap.get("userId")).trim();
		if(ValidateUtils.isEmpty(userId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userId"));
			return resultMap;
		}
		String idCard = String.valueOf(receiveMap.get("idCard")).trim();
		if(ValidateUtils.isEmpty(idCard)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "idCard"));
			return resultMap;
		}
		String imageStr = String.valueOf(receiveMap.get("imageStr")).trim();
		if(ValidateUtils.isEmpty(imageStr)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "imageStr"));
			return resultMap;
		}
		try {
			Map<String,Object> inputMap = new HashMap<String,Object>();
			inputMap.put("userId", userId);
			inputMap.put("idCard", idCard);
			inputMap.put("imageStr", imageStr);
			return faceRecServiceGWImpl.FaceRecognition(inputMap);
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("系统错误：" + e.getMessage(), e);
			}
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		return resultMap;
	}
}
