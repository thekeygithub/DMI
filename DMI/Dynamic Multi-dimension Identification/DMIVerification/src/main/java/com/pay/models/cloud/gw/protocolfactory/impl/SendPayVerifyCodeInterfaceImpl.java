package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.TdOrderServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 支付-下发支付短信验证码
 * Created by yacheng.ji on 2016/4/13.
 */
@Service("sendPayVerifyCodeInterfaceImpl")
public class SendPayVerifyCodeInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(SendPayVerifyCodeInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String payOrderId = String.valueOf(receiveMap.get("payOrderId")).trim();//支付平台订单号
        if(ValidateUtils.isEmpty(payOrderId)){
            resultMap.put("resultCode", Hint.SYS_10048_PAYORDERID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10048_PAYORDERID_NOT_NULL_ERROR.getMessage());
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
        String payToken = String.valueOf(receiveMap.get("payToken")).trim();//支付通行证
        if(ValidateUtils.isEmpty(payToken)){
            resultMap.put("resultCode", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("accountId", accountId);
            inputMap.put("payToken", payToken);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = tdOrderServiceGWImpl.sendPayVerifyCode(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
