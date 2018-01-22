package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.trade.TdOrderServiceGW;
import com.pay.cloud.util.CipherAesEnum;
import com.pay.cloud.util.DateUtils;
import com.pay.cloud.util.EncryptUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 订单确认支付
 * Created by yacheng.ji on 2016/4/12.
 */
@Service("confirmPayOrderInterfaceImpl")
public class ConfirmPayOrderInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(ConfirmPayOrderInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<>();
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
        String payToken = String.valueOf(receiveMap.get("payToken")).trim();//通行证
        if(ValidateUtils.isEmpty(payToken)){
            resultMap.put("resultCode", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String validThru = String.valueOf(receiveMap.get("validThru")).trim();//有效期
        if(ValidateUtils.isNotEmpty(validThru)){
            if(validThru.length() != 4 || !ValidateUtils.isPositiveInteger(validThru)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13059_600024.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13059_600024.getMessage());
                return resultMap;
            }
            String month = validThru.substring(0, 2);
            if(Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1){
                resultMap.put("resultCode", Hint.PAY_ORDER_13059_600024.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13059_600024.getMessage());
                return resultMap;
            }
            String currentDate = DateUtils.getNowDate("yyMM");
            String year = validThru.substring(validThru.length() - 2);
            int validThruInt = Integer.parseInt(year.concat(month));
            if(validThruInt < Integer.parseInt(currentDate)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13079_CREDIT_DATE_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13079_CREDIT_DATE_INVALID.getMessage());
                return resultMap;
            }
        }
        String cvv2 = String.valueOf(receiveMap.get("cvv2")).trim();//cvv2，信用卡背后的3位数字
        if(ValidateUtils.isNotEmpty(cvv2)){
            if(cvv2.length() != 3 || !ValidateUtils.isPositiveInteger(cvv2)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13058_600023.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13058_600023.getMessage());
                return resultMap;
            }
        }
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("validateCode", String.valueOf(receiveMap.get("validateCode")).trim());
            inputMap.put("accountId", accountId);
            inputMap.put("payToken", payToken);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            if(ValidateUtils.isNotEmpty(validThru)){
                inputMap.put("validThru", EncryptUtils.aesEncrypt(validThru, CipherAesEnum.CARDNOKEY));
            }else {
                inputMap.put("validThru", "");
            }
            if(ValidateUtils.isNotEmpty(cvv2)){
                inputMap.put("cvv2", EncryptUtils.aesEncrypt(cvv2, CipherAesEnum.CARDNOKEY));
            }else {
                inputMap.put("cvv2", "");
            }
            resultMap = tdOrderServiceGWImpl.confirmPaymentOrder(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
