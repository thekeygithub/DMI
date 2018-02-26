package com.models.cloud.gw.service.payuser;

import java.util.Map;

/**
 * 人脸识别相关业务
 * @Description: TODO
 * @ClassName: FaceRecGW 
 * @author: li.zhou
 * @date: 2016年8月15日 下午2:19:17
 */
public interface FaceRecServiceGW {
	/**
	 * 人脸识别
	 * @Description: TODO
	 * @Title: FaceRecognition 
	 * @param idcard 当前付款方式 社保卡号  imageStr 上传头像信息
	 * @return
	 */
	public Map<String,Object> FaceRecognition(Map<String,Object> inputMap) throws Exception;
}
