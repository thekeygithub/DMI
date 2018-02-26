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
 * C端用户易保支付密码验证接口
 * Created by yacheng.ji on 2016/4/7.
 */
@Service("verifyUserPaymentPwdInterfaceImpl")
public class VerifyUserPaymentPwdInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(VerifyUserPaymentPwdInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String payPassword = String.valueOf(receiveMap.get("payPassword")).trim();//支付密码
        if(ValidateUtils.isEmpty(payPassword)){
            resultMap.put("resultCode", Hint.SYS_10027_PAY_PASSWORD_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10027_PAY_PASSWORD_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
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
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("payPassword", payPassword);
            inputMap.put("accountId", accountId);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = payUserServiceGWImpl.verifyUserPaymentPassword(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
