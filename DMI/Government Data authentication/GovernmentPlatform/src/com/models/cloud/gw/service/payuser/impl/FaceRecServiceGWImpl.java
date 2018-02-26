package com.models.cloud.gw.service.payuser.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.service.payuser.FaceRecServiceGW;
import com.models.cloud.pay.payuser.service.FaceRecService;

@Service("faceRecServiceGWImpl")
public class FaceRecServiceGWImpl implements FaceRecServiceGW {
	
	private static final Logger logger = Logger.getLogger(FaceRecServiceGWImpl.class);
	
	@Resource(name="faceRecServiceImpl")
	private FaceRecService faceRecService;
	
	@Override
	public Map<String,Object> FaceRecognition(Map<String,Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("人脸识别接口 FaceRecServiceGWImpl --> 请求参数："+inputMap);
		}
		return faceRecService.FaceRecognition(inputMap);
	}

}
