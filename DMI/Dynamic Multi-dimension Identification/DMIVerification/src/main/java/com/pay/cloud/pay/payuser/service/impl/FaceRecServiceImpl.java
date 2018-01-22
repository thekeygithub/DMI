package com.pay.cloud.pay.payuser.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.pay.payuser.service.FaceRecService;
import com.pay.cloud.pay.payuser.service.SocialSecurityService;
import com.pay.cloud.util.DateUtils;
import com.pay.cloud.util.hint.Hint;

@Service("faceRecServiceImpl")
public class FaceRecServiceImpl implements FaceRecService {

	@Resource(name="redisService")
	private RedisService redisService;
	@Resource(name="socialSecurityServiceImpl")
	private SocialSecurityService socialSecurityService;
	
	@Override
	public Map<String,Object> FaceRecognition(Map<String,Object> inputMap) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,String> existCachMap = redisService.getMap("face_recognition_error_count");//缓存中的人脸识别验证次数
		if(existCachMap == null){//首次进入 或者次日缓存清空  无错误次数
			resultMap = doRecognition(inputMap);//进行人脸识别的校验
		}else{
			String cnt = String.valueOf(existCachMap.get(inputMap.get("idCard"))==null?"":existCachMap.get(inputMap.get("idCard")));
			if(cnt.equals("")){//进行人脸识别的校验
				resultMap = doRecognition(inputMap);
			}else if(Integer.parseInt(cnt) <= 3){//进行人脸识别的校验
				resultMap = doRecognition(inputMap);
			}else{//验证错误超过3次  返回相应的错误码和提示语
				resultMap.put("resultCode", Hint.FACE_Recognition_34002_ERROR.getCode());
				resultMap.put("resultDesc",Hint.FACE_Recognition_34002_ERROR.getMessage());
			}
		}
		return resultMap;
	}
	//开始人脸识别的接口校验
	private Map<String,Object> doRecognition(Map<String,Object> inputMap) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String faceRecognitionErrorCountKey = "face_recognition_error_count";//人脸识别错误次数缓存中的总key值
		Map<String,String> cachMap = new HashMap<String,String>();//缓存中key对应的value   Map值
		Map<String,String> existCachMap = redisService.getMap("face_recognition_error_count");//缓存中的人脸识别验证次数
		String errCnt = String.valueOf(existCachMap.get(inputMap.get("idCard"))==null?"0":existCachMap.get(inputMap.get("idCard")));//验证错误次数
		if(inputMap.get("imageStr").equals("1")){//校验成功
			cachMap.put(inputMap.get("idCard").toString(), 0+"");//校验成功  识别错误次数清零
			redisService.setMap(faceRecognitionErrorCountKey,cachMap);
			//人脸识别校验成功  调用绑卡银行卡短信验证
			resultMap = socialSecurityService.socialSecurityRealFu(inputMap);
		}else{//校验失败  当前卡号 当日 校验次数+1
			cachMap.put(inputMap.get("idCard").toString(), (Integer.parseInt(errCnt) + 1 + ""));
			redisService.setMap(faceRecognitionErrorCountKey,cachMap);
			redisService.expire(faceRecognitionErrorCountKey, DateUtils.getDaySurplusSecond());
			resultMap.put("resultCode", Hint.FACE_Recognition_34001_ERROR.getCode());
			resultMap.put("resultDesc", Hint.FACE_Recognition_34001_ERROR.getMessage());
		}
		return resultMap;
	}

}
