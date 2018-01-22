package com.pay.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取登录Token（H5自动登录）
 * Created by yacheng.ji on 2016/6/15.
 */
@Service("loginForTokenInterfaceImpl")
public class LoginForTokenInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(LoginForTokenInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = String.valueOf(receiveMap.get("userId")).trim();
        if(ValidateUtils.isEmpty(userId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userId"));
            return resultMap;
        }
        String userCode = String.valueOf(receiveMap.get("userCode")).trim();
        if(ValidateUtils.isEmpty(userCode)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userCode"));
            return resultMap;
        }
        if(!ValidateUtils.isPhoneNumber(userCode)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "userCode"));
            return resultMap;
        }
        String hardwareId = String.valueOf(receiveMap.get("hardwareId")).trim();
        if(ValidateUtils.isEmpty(hardwareId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "hardwareId"));
            return resultMap;
        }
        String token = String.valueOf(receiveMap.get("token")).trim();
        if(ValidateUtils.isEmpty(token)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "token"));
            return resultMap;
        }
        try {
            resultMap = payUserServiceGWImpl.loginForToken(receiveMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
