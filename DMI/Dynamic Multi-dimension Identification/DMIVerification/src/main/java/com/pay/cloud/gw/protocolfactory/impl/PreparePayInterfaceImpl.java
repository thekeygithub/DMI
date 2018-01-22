package com.pay.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.trade.TdOrderServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 预支付（非YeePay方式）
 * Created by yacheng.ji on 2016/8/29.
 */
@Service("preparePayInterfaceImpl")
public class PreparePayInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(PreparePayInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String payOrderId = String.valueOf(receiveMap.get("payOrderId")).trim();//支付平台订单号
        if(ValidateUtils.isEmpty(payOrderId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
            return resultMap;
        }

        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
        if(ValidateUtils.isEmpty(accountId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "accountId"));
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(accountId)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
            return resultMap;
        }

        String payToken = String.valueOf(receiveMap.get("payToken")).trim();//通行证
        if(ValidateUtils.isEmpty(payToken)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payToken"));
            return resultMap;
        }

        String ccbFlag = String.valueOf(receiveMap.get("ccbFlag")).trim();//建行通道标识 可空 1-建行 2-其他行
        if(ValidateUtils.isEmpty(ccbFlag)){
            ccbFlag = "";
        }

        String userIp = String.valueOf(receiveMap.get("remoteAddr")).trim();
        if(ValidateUtils.isEmpty(userIp)){
            userIp = String.valueOf(receiveMap.get("ipInfo")).trim();
            if(ValidateUtils.isEmpty(userIp)){
                userIp = "";
            }
        }

        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("accountId", accountId);
            inputMap.put("payToken", payToken);
            inputMap.put("ccbFlag", ccbFlag);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            inputMap.put("userIp", userIp);
            inputMap.put("terminalType", String.valueOf(receiveMap.get("terminalType")).trim());
            resultMap = tdOrderServiceGWImpl.preparePay(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
