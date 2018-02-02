package com.models.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.TdOrderServiceGW;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付-创建订单(订单信息存入redis)
 * Created by yacheng.ji on 2016/4/11.
 */
@Service("createPayOrderInterfaceImpl")
public class CreatePayOrderInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(CreatePayOrderInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String terminalType = String.valueOf(receiveMap.get("terminalType")).trim();
        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
        if(terminalType.equals(String.valueOf(DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCode())) ||
           terminalType.equals(String.valueOf(DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCode()))){
            if(ValidateUtils.isNotEmpty(accountId)){
                if(!ValidateUtils.isPositiveInteger(accountId)){
                    resultMap.put("resultCode", Hint.SYS_10029_ACCOUNTID_INVALID_ERROR.getCodeString());
                    resultMap.put("resultDesc", Hint.SYS_10029_ACCOUNTID_INVALID_ERROR.getMessage());
                    return resultMap;
                }
            }else{
                accountId = "";
            }
        }else{
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
        }
        String merOrderId = String.valueOf(receiveMap.get("merOrderId")).trim();//商户订单号
        if(ValidateUtils.isEmpty(merOrderId)){
            resultMap.put("resultCode", Hint.SYS_10034_MERORDERID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10034_MERORDERID_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String merOrderExpire = String.valueOf(receiveMap.get("merOrderExpire")).trim();//商户订单有效期，单位：秒
        if(ValidateUtils.isEmpty(merOrderExpire)){
            merOrderExpire = "";
        }else {
            if(!ValidateUtils.isPositiveInteger(merOrderExpire)){
                resultMap.put("resultCode", Hint.SYS_10035_MERORDEREXPIRE_INVALID_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10035_MERORDEREXPIRE_INVALID_ERROR.getMessage());
                return resultMap;
            }
            if(Long.valueOf(merOrderExpire).toString().length() > 4){
                merOrderExpire = "";
            }else{
                if(Integer.parseInt(merOrderExpire) <= 0){
                    resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
                    resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
                    return resultMap;
                }
            }
        }

        String goodsDesc = String.valueOf(receiveMap.get("goodsDesc")).trim();//商品描述信息
        if(ValidateUtils.isEmpty(goodsDesc)){
            resultMap.put("resultCode", Hint.SYS_10036_GOODSDESC_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10036_GOODSDESC_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String workSpActId = String.valueOf(receiveMap.get("workSpActId")).trim();//合作商户账号ID
        if(ValidateUtils.isEmpty(workSpActId)){
            resultMap.put("resultCode", Hint.SYS_10037_WORKSPACTID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10037_WORKSPACTID_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(workSpActId)){
            resultMap.put("resultCode", Hint.SYS_10038_WORKSPACTID_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10038_WORKSPACTID_INVALID_ERROR.getMessage());
            return resultMap;
        }
        String transBusiType = String.valueOf(receiveMap.get("transBusiType")).trim();//交易业务类型
        if(ValidateUtils.isEmpty(transBusiType)){
            resultMap.put("resultCode", Hint.SYS_10039_TRANSBUSITYPE_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10039_TRANSBUSITYPE_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(transBusiType)){
            resultMap.put("resultCode", Hint.SYS_10040_TRANSBUSITYPE_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10040_TRANSBUSITYPE_INVALID_ERROR.getMessage());
            return resultMap;
        }
        if(!DimDictEnum.checkTransBusiType(Integer.parseInt(transBusiType))){
            resultMap.put("resultCode", Hint.SYS_10040_TRANSBUSITYPE_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10040_TRANSBUSITYPE_INVALID_ERROR.getMessage());
            return resultMap;
        }
        String payMoney = String.valueOf(receiveMap.get("payMoney")).trim();//支付金额
        if(ValidateUtils.isEmpty(payMoney)){
            resultMap.put("resultCode", Hint.SYS_10041_PAYMONEY_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10041_PAYMONEY_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        if(!ValidateUtils.isMoney(payMoney, false)){//支付金额必须大于0
            resultMap.put("resultCode", Hint.SYS_10042_PAYMONEY_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10042_PAYMONEY_INVALID_ERROR.getMessage());
            return resultMap;
        }
        String callbackUrl = String.valueOf(receiveMap.get("callbackUrl")).trim();//商户后台系统的回调地址
        if(ValidateUtils.isEmpty(callbackUrl)){
            resultMap.put("resultCode", Hint.SYS_10043_CALLBACKURL_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10043_CALLBACKURL_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("accountId", accountId);
            inputMap.put("merOrderId", merOrderId);
            inputMap.put("merOrderExpire", merOrderExpire);
            inputMap.put("goodsDesc", goodsDesc);
            inputMap.put("workSpActId", workSpActId);
            inputMap.put("transBusiType", transBusiType);
            inputMap.put("payMoney", payMoney);
            inputMap.put("appId", String.valueOf(receiveMap.get("appId")).trim());
            inputMap.put("terminalType", terminalType);
            inputMap.put("callbackUrl", callbackUrl);
            inputMap.put("systemId", String.valueOf(receiveMap.get("systemId")).trim());
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            resultMap = tdOrderServiceGWImpl.createPayOrder(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
