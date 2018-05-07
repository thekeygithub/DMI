package com.models.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断银行卡号类型 储蓄卡or信用卡
 * Created by yacheng.ji on 2016/4/11.
 */
@Service("bankCardNoTypeInterfaceImpl")
public class BankCardNoTypeInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(BankCardNoTypeInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
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
        String cardNo = String.valueOf(receiveMap.get("cardNo")).trim();//卡号
        if(ValidateUtils.isEmpty(cardNo)){
            resultMap.put("resultCode", Hint.SYS_10031_CARDNO_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10031_CARDNO_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(cardNo.length() < 10){
            resultMap.put("resultCode", Hint.SYS_10032_CARDNO_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10032_CARDNO_INVALID_ERROR.getMessage());
            return resultMap;
        }
        String payToken = String.valueOf(receiveMap.get("payToken")).trim();//支付通行证
        if(ValidateUtils.isEmpty(payToken)){
            resultMap.put("resultCode", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("accountId", accountId);
            inputMap.put("cardNo", EncryptUtils.aesEncrypt(cardNo.replaceAll(" ", ""), CipherAesEnum.CARDNOKEY));
            inputMap.put("payToken", payToken);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = payUserServiceGWImpl.bankCardNoType(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
