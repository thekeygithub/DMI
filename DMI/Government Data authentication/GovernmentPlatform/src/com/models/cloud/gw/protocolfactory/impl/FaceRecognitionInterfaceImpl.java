package com.models.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 人脸识别（博宏人脸识别平台接口）
 * Created by yacheng.ji on 2016/12/19.
 */
@Service("faceRecognitionInterfaceImpl")
public class FaceRecognitionInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(FaceRecognitionInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户编号
        if(ValidateUtils.isEmpty(accountId)){
            resultMap.put("resultCode", Hint.SYS_10028_ACCOUNTID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10028_ACCOUNTID_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(accountId)){
            resultMap.put("resultCode", Hint.SYS_10029_ACCOUNTID_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10029_ACCOUNTID_INVALID_ERROR.getMessage());
            return resultMap;
        }
        String imageBase64 = String.valueOf(receiveMap.get("imageBase64")).trim();//图片base64信息
        if(ValidateUtils.isEmpty(imageBase64)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "imageBase64"));
            return resultMap;
        }
        String terminalType = String.valueOf(receiveMap.get("terminalType")).trim();
        if(!(DimDictEnum.TD_OPER_CHAN_TYPE_ANDROID.getCodeString().equals(terminalType) ||
           DimDictEnum.TD_OPER_CHAN_TYPE_IOS.getCodeString().equals(terminalType))){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "terminalType"));
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("accountId", accountId);
            inputMap.put("imageBase64", imageBase64);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            inputMap.put("terminalType", terminalType);
            resultMap = payUserServiceGWImpl.faceRecognition(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
