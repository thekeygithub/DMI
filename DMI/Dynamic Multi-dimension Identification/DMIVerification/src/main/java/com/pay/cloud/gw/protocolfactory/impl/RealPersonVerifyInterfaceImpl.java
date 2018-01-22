package com.pay.cloud.gw.protocolfactory.impl;

import com.pay.cloud.core.common.IdcardValidator;
import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 实人验证【二要素+人脸识别】
 * Created by yacheng.ji on 2017/3/9.
 */
@Service("realPersonVerifyInterfaceImpl")
public class RealPersonVerifyInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(RealPersonVerifyInterfaceImpl.class);

    @Resource
    private PayUserServiceGW payUserServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {

        Map<String, Object> resultMap = new HashMap<>();

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

        String userName = String.valueOf(receiveMap.get("userName")).trim();//用户姓名
        if(ValidateUtils.isEmpty(userName)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "userName"));
            return resultMap;
        }
        if(!ValidateUtils.checkChineseName(userName)){
            resultMap.put("resultCode", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getCodeString());
            resultMap.put("resultDesc", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getMessage());
            return resultMap;
        }

        String idNumber = String.valueOf(receiveMap.get("idNumber")).trim();//身份证号
        if(ValidateUtils.isEmpty(idNumber)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "idNumber"));
            return resultMap;
        }
        IdcardValidator idcardValidator = new IdcardValidator();
        if(!idcardValidator.isIdcard(idNumber)){
            resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
            resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
            return resultMap;
        }
        if(idNumber.length() == 15){
            idNumber = idcardValidator.convertIdcarBy15bit(idNumber);
        }
        if(!idcardValidator.is18Idcard(idNumber)){
            resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
            resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
            return resultMap;
        }
        String photoBase64 = String.valueOf(receiveMap.get("photoBase64")).trim();//用户照片Base64
        if(ValidateUtils.isEmpty(photoBase64)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "photoBase64"));
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("accountId", accountId);
            inputMap.put("userName", userName);
            inputMap.put("idNumber", idNumber);
            inputMap.put("photoBase64", photoBase64);
            inputMap.put("appId", receiveMap.get("appId"));
            resultMap = payUserServiceGWImpl.realPersonVerify(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
