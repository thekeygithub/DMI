package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.util.DimDictEnum;
import com.pay.cloud.util.IdCreatorUtils;
import com.pay.cloud.util.Md5SaltUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
/**
 * 
 * @author qingsong.li
 *
 */
@Service("createTradeOrderInfoInterfaceImpl")
public class CreateTradeOrderInfoInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(CreateTradeOrderInfoInterfaceImpl.class);

    @Resource
	private RedisService redisService;
    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<>();

        String origOrderId = String.valueOf(receiveMap.get("origOrderId")).trim();//原始交易单编号
        if(ValidateUtils.isEmpty(origOrderId)){
            origOrderId = "";
        }

//        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
//        if(ValidateUtils.isEmpty(accountId)){
//            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
//            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "accountId"));
//            return resultMap;
//        }
//        if(!ValidateUtils.isPositiveInteger(accountId)){
//            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
//            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
//            return resultMap;
//        }

        String merOrderId = String.valueOf(receiveMap.get("merOrderId")).trim();//商户订单号
        if(ValidateUtils.isEmpty(merOrderId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "merOrderId"));
            return resultMap;
        }

        String merOrderExpire = String.valueOf(receiveMap.get("merOrderExpire")).trim();//商户订单有效期，单位：秒
        if(ValidateUtils.isNotEmpty(merOrderExpire)){
            if(!ValidateUtils.isPositiveInteger(merOrderExpire)){
                resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "merOrderExpire"));
                return resultMap;
            }
            if(Long.valueOf(merOrderExpire).toString().length() <= 4){
                if(Integer.parseInt(merOrderExpire) <= 0){
                    resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
                    resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
                    return resultMap;
                }
            }else{
                merOrderExpire = "";
            }
        }else {
            merOrderExpire = "";
        }

        String goodsDesc = String.valueOf(receiveMap.get("goodsDesc")).trim();//商品描述信息
        if(ValidateUtils.isEmpty(goodsDesc)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "goodsDesc"));
            return resultMap;
        }

        String workSpActId = String.valueOf(receiveMap.get("workSpActId")).trim();//合作商户账号ID
        if(ValidateUtils.isEmpty(workSpActId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "workSpActId"));
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(workSpActId)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "workSpActId"));
            return resultMap;
        }

        String transBusiType = String.valueOf(receiveMap.get("transBusiType")).trim();//交易一级业务类型
        if(ValidateUtils.isEmpty(transBusiType)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "transBusiType"));
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(transBusiType)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "transBusiType"));
            return resultMap;
        }
        if(!DimDictEnum.checkTransBusiType(Integer.parseInt(transBusiType))){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "transBusiType"));
            return resultMap;
        }

        String transSecBusiType = String.valueOf(receiveMap.get("transSecBusiType")).trim();//交易二级业务类型
        if(ValidateUtils.isEmpty(transSecBusiType)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "transSecBusiType"));
            return resultMap;
        }

        String totalPayMoney = String.valueOf(receiveMap.get("totalPayMoney")).trim();//支付总额
        if(ValidateUtils.isEmpty(totalPayMoney)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "totalPayMoney"));
            return resultMap;
        }
        if(!ValidateUtils.isMoney(totalPayMoney, false)){//支付总额必须大于0
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "totalPayMoney"));
            return resultMap;
        }

        String socialSecurityBindId = String.valueOf(receiveMap.get("socialSecurityBindId")).trim();//社保卡绑卡ID
        if(ValidateUtils.isEmpty(socialSecurityBindId)){
            socialSecurityBindId = "";
        }else{
            if(!ValidateUtils.isPositiveInteger(socialSecurityBindId)){
                resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "socialSecurityBindId"));
                return resultMap;
            }
        }

        String buttonShowName = String.valueOf(receiveMap.get("buttonShowName")).trim();//付款结果页返回按钮显示文案
        if(ValidateUtils.isEmpty(buttonShowName)){
            buttonShowName = "";
        }

        String callbackUrl = String.valueOf(receiveMap.get("callbackUrl")).trim();//商户后台系统的回调地址
        if(ValidateUtils.isEmpty(callbackUrl)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "callbackUrl"));
            return resultMap;
        }

        String buySelf = String.valueOf(receiveMap.get("buySelf")).trim();//自费标识 0-全部自费 1-部分自费或全部医保
        if(ValidateUtils.isEmpty(buySelf)){
            buySelf = DimDictEnum.BUY_SELF_NO.getCodeString();
        }
        if(!(buySelf.equals(DimDictEnum.BUY_SELF_YES.getCodeString()) || buySelf.equals(DimDictEnum.BUY_SELF_NO.getCodeString()))){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "buySelf"));
            return resultMap;
        }

        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("origOrderId", origOrderId);
            //inputMap.put("accountId", accountId);
            inputMap.put("merOrderId", merOrderId);
            inputMap.put("buySelf", buySelf);
            inputMap.put("merOrderExpire", merOrderExpire);
            inputMap.put("goodsDesc", goodsDesc);
            inputMap.put("workSpActId", workSpActId);
            inputMap.put("transBusiType", transBusiType);
            inputMap.put("transSecBusiType", transSecBusiType);
            inputMap.put("orderExtendInfo", receiveMap.get("orderExtendInfo"));
            inputMap.put("totalPayMoney", totalPayMoney);
            inputMap.put("socialSecurityBindId", socialSecurityBindId);
            inputMap.put("callbackUrl", callbackUrl);
            inputMap.put("appId", String.valueOf(receiveMap.get("appId")).trim());
            inputMap.put("terminalType", String.valueOf(receiveMap.get("terminalType")).trim());
            inputMap.put("systemId", String.valueOf(receiveMap.get("systemId")).trim());
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            inputMap.put("buttonShowName", buttonShowName);
            inputMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
            inputMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            
            String payOrderId = IdCreatorUtils.getPayOrderId();
            inputMap.put("webTerminalOrderId", payOrderId);
            
            inputMap.put("payOrderId", payOrderId);
            String ramdomStr = Md5SaltUtils.getRandomString(32);
            String json = JSON.toJSONString(inputMap);
            redisService.set(ramdomStr, json);
            redisService.expire(ramdomStr, Integer.parseInt(Propertie.APPLICATION.value("payOrder.expire")));

            redisService.set(BaseDictConstant.WEB_TERMINAL_ACCESS_REDIS_KEY, BaseDictConstant.WEB_TERMINAL_ACCESS_REDIS_KEY);
            redisService.expire(BaseDictConstant.WEB_TERMINAL_ACCESS_REDIS_KEY, Integer.parseInt(Propertie.APPLICATION.value("payOrder.expire")));
            
            resultMap.put("appId", String.valueOf(receiveMap.get("appId")).trim());
            resultMap.put("payOrderId", payOrderId);
            resultMap.put("key", ramdomStr);
            resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
