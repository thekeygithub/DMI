package com.models.cloud.pay.payuser.service;

import java.util.Map;

/**
 * 人脸识别功能业务接口
 * @Description: TODO
 * @ClassName: FaceRecService 
 * @author: li.zhou
 * @date: 2016年8月15日 下午2:15:35
 */
public interface FaceRecService {
	/**
	 * 人脸识别
	 * @Description: TODO
	 * @Title: FaceRecognition 
	 * @param inputMap
	 * @return 
	 */
	public Map<String,Object> FaceRecognition(Map<String,Object> inputMap) throws Exception;
}
