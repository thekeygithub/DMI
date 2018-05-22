package com.models.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一注册接口
 * @author qingsong.li
 *
 */
@Service("registerInterfaceImpl")
public class RegisterInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(RegisterInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String password = String.valueOf(receiveMap.get("password")).trim();
        if(ValidateUtils.isEmpty(password)){
            resultMap.put("resultCode", Hint.SYS_10049_PASSWORD_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10049_PASSWORD_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
//        if(password.length()>16){			
//        	resultMap.put("resultCode", Hint.USER_25026_PASSWORD_FAILED.getCodeString());
//        	resultMap.put("resultDesc", Hint.USER_25026_PASSWORD_FAILED.getMessage());
//			return resultMap;
//		}
        receiveMap.put("password",password);//去空格
        
        String userCode = String.valueOf(receiveMap.get("userCode")==null?"":receiveMap.get("userCode")).trim();
        if(ValidateUtils.isEmpty(userCode)){
            resultMap.put("resultCode", Hint.SYS_10050_USERCODE_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10050_USERCODE_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isPhoneNumber(userCode)){
            resultMap.put("resultCode", Hint.SYS_10051_USERCODE_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10051_USERCODE_INVALID_ERROR.getMessage());
            return resultMap;
        }
        receiveMap.put("userCode",userCode.trim());
//        String verifyCode = String.valueOf(receiveMap.get("verifyCode")).trim();
//        if(ValidateUtils.isEmpty(verifyCode)){
//            resultMap.put("resultCode", Hint.SYS_10052_VERIFYCODE_NOT_NULL_ERROR.getCodeString());
//            resultMap.put("resultDesc", Hint.SYS_10052_VERIFYCODE_NOT_NULL_ERROR.getMessage());
//            return resultMap;
//        }
        try {
            resultMap = payUserServiceGWImpl.register2(receiveMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
