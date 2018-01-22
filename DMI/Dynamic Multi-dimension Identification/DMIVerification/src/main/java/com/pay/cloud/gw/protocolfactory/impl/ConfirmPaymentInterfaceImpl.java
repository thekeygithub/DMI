package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.trade.TdOrderServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 订单确认支付
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年8月18日 
 * @time 上午11:30:08
 * @version V1.0
 * @修改记录
 *
 */
@Service("confirmPaymentInterfaceImpl")
public class ConfirmPaymentInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(ConfirmPaymentInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
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
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("accountId", accountId);
            inputMap.put("payToken", payToken);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = tdOrderServiceGWImpl.confirmPayment(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
