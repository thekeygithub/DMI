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
 * 同步用户（H5同步用户数据）
 * Created by yacheng.ji on 2016/6/15.
 */
@Service("syncUserInterfaceImpl")
public class SyncUserInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(SyncUserInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userCode = String.valueOf(receiveMap.get("userCode")).trim();
        if(ValidateUtils.isEmpty(userCode)){
            resultMap.put("resultCode", Hint.SYS_10024_USER_CODE_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10024_USER_CODE_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String password = String.valueOf(receiveMap.get("password")).trim();
        if(ValidateUtils.isEmpty(password)){
            resultMap.put("resultCode", Hint.SYS_10025_PASSWORD_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10025_PASSWORD_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String phone = String.valueOf(receiveMap.get("phone")).trim();
        if(ValidateUtils.isEmpty(phone)){
            resultMap.put("resultCode", Hint.SYS_10024_USER_CODE_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10024_USER_CODE_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isPhoneNumber(phone)){
            resultMap.put("resultCode", Hint.SYS_10026_PHONE_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10026_PHONE_INVALID_ERROR.getMessage());
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("userCode", userCode);
            inputMap.put("password", password);
            inputMap.put("phone", phone);
            inputMap.put("appId", String.valueOf(receiveMap.get("appId")).trim());
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = payUserServiceGWImpl.syncUser(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
