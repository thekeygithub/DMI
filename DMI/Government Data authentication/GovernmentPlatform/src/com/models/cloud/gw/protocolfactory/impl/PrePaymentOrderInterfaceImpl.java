package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.common.IdcardValidator;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.TdOrderServiceGW;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.DateUtils;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 订单预支付
 * Created by yacheng.ji on 2016/4/12.
 */
@Service("prePaymentOrderInterfaceImpl")
public class PrePaymentOrderInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(PrePaymentOrderInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String paymentType = String.valueOf(receiveMap.get("paymentType")).trim();//支付类型 1：绑卡支付 2：首次或绑卡过期储蓄卡 3：首次或绑卡过期信用卡
        if(ValidateUtils.isEmpty(paymentType)){
            resultMap.put("resultCode", Hint.SYS_10044_PAYMENTTYPE_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10044_PAYMENTTYPE_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String bindId = "";
        String cardNo = "";
        String idCardType = "";
        String idCard = "";
        String idCard18 = "";
        String owner = "";
        String phone = "";
        String validThru = "";
        String cvv2 = "";
        IdcardValidator idcardValidator = new IdcardValidator();
        if(BaseDictConstant.PAYMENT_TYPE_BIND_CARD.equals(paymentType)){//绑卡支付
            bindId = String.valueOf(receiveMap.get("bindId")).trim();//绑卡ID
            if(ValidateUtils.isEmpty(bindId)){
                resultMap.put("resultCode", Hint.SYS_10045_BINDID_NOT_NULL_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10045_BINDID_NOT_NULL_ERROR.getMessage());
                return resultMap;
            }
        }else if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType)){//首次或绑卡过期储蓄卡
            cardNo = String.valueOf(receiveMap.get("cardNo")).trim();//卡号
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
            idCardType = String.valueOf(receiveMap.get("idCardType")).trim();//证件类型 01-身份证
            if(ValidateUtils.isEmpty(idCardType)){
                resultMap.put("resultCode", Hint.SYS_10045_IDCARDTYPE_NOT_NULL_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10045_IDCARDTYPE_NOT_NULL_ERROR.getMessage());
                return resultMap;
            }
            if(!"01".equals(idCardType)){//暂时只支持身份证
                resultMap.put("resultCode", Hint.SYS_10046_IDCARDTYPE_INVALID_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10046_IDCARDTYPE_INVALID_ERROR.getMessage());
                return resultMap;
            }
            idCard = String.valueOf(receiveMap.get("idCard")).trim();//证件号
            if(ValidateUtils.isEmpty(idCard)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            if(!idcardValidator.isIdcard(idCard)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            if(idCard.length() == 15){
                idCard18 = idcardValidator.convertIdcarBy15bit(idCard);
            }else {
                idCard18 = idCard;
            }
            if(!idcardValidator.is18Idcard(idCard18)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            owner = String.valueOf(receiveMap.get("owner")).trim();//持卡人姓名
            if(ValidateUtils.isEmpty(owner)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getMessage());
                return resultMap;
            }
            if(!ValidateUtils.checkChineseName(owner)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getMessage());
                return resultMap;
            }
            phone = String.valueOf(receiveMap.get("phone")).trim();//借记卡/信用卡预留手机号
            if(ValidateUtils.isEmpty(phone)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getMessage());
                return resultMap;
            }
            if(!ValidateUtils.isPhoneNumber(phone)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getMessage());
                return resultMap;
            }
        }else if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD.equals(paymentType)){//首次或绑卡过期信用卡
            cardNo = String.valueOf(receiveMap.get("cardNo")).trim();//卡号
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
            idCardType = String.valueOf(receiveMap.get("idCardType")).trim();//证件类型 01-身份证
            if(ValidateUtils.isEmpty(idCardType)){
                resultMap.put("resultCode", Hint.SYS_10045_IDCARDTYPE_NOT_NULL_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10045_IDCARDTYPE_NOT_NULL_ERROR.getMessage());
                return resultMap;
            }
            if(!"01".equals(idCardType)){//暂时只支持身份证
                resultMap.put("resultCode", Hint.SYS_10046_IDCARDTYPE_INVALID_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10046_IDCARDTYPE_INVALID_ERROR.getMessage());
                return resultMap;
            }
            idCard = String.valueOf(receiveMap.get("idCard")).trim();//证件号
            if(ValidateUtils.isEmpty(idCard)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            if(!idcardValidator.isIdcard(idCard)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            if(idCard.length() == 15){
                idCard18 = idcardValidator.convertIdcarBy15bit(idCard);
            }else {
                idCard18 = idCard;
            }
            if(!idcardValidator.is18Idcard(idCard18)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13060_600119.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13060_600119.getMessage());
                return resultMap;
            }
            owner = String.valueOf(receiveMap.get("owner")).trim();//持卡人姓名
            if(ValidateUtils.isEmpty(owner)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getMessage());
                return resultMap;
            }
            if(!ValidateUtils.checkChineseName(owner)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13068_USER_NAME_INVALID.getMessage());
                return resultMap;
            }
            phone = String.valueOf(receiveMap.get("phone")).trim();//借记卡/信用卡预留手机号
            if(ValidateUtils.isEmpty(phone)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getMessage());
                return resultMap;
            }
            if(!ValidateUtils.isPhoneNumber(phone)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13070_USER_PHONE_INVALID.getMessage());
                return resultMap;
            }
            validThru = String.valueOf(receiveMap.get("validThru")).trim();//有效期
            if(ValidateUtils.isEmpty(validThru)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13059_600024.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13059_600024.getMessage());
                return resultMap;
            }
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
            cvv2 = String.valueOf(receiveMap.get("cvv2")).trim();//cvv2，信用卡背后的3位数字
            if(ValidateUtils.isEmpty(cvv2)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13058_600023.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13058_600023.getMessage());
                return resultMap;
            }
            if(cvv2.length() != 3 || !ValidateUtils.isPositiveInteger(cvv2)){
                resultMap.put("resultCode", Hint.PAY_ORDER_13058_600023.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13058_600023.getMessage());
                return resultMap;
            }
        }else{
            resultMap.put("resultCode", Hint.SYS_10047_PAYMENTTYPE_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10047_PAYMENTTYPE_INVALID_ERROR.getMessage());
            return resultMap;
        }

        String payToken = String.valueOf(receiveMap.get("payToken")).trim();//通行证
        if(ValidateUtils.isEmpty(payToken)){
            resultMap.put("resultCode", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10030_PAYTOKEN_NOT_NULL_ERROR.getMessage());
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
        String merOrderId = String.valueOf(receiveMap.get("merOrderId")).trim();//商户订单号
        if(ValidateUtils.isEmpty(merOrderId)){
            resultMap.put("resultCode", Hint.SYS_10034_MERORDERID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10034_MERORDERID_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        String payOrderId = String.valueOf(receiveMap.get("payOrderId")).trim();//支付平台订单号
        if(ValidateUtils.isEmpty(payOrderId)){
            resultMap.put("resultCode", Hint.SYS_10048_PAYORDERID_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10048_PAYORDERID_NOT_NULL_ERROR.getMessage());
            return resultMap;
        }
        try {
            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("bindId", bindId);
            inputMap.put("payToken", payToken);
            if(cardNo.contains("|")){
                String[] cardNoArray = cardNo.split("\\|");
                cardNo = EncryptUtils.aesDecrypt(cardNoArray[0], CipherAesEnum.CARDNOKEY).concat(cardNoArray[1]);
            }
            inputMap.put("cardNo", EncryptUtils.aesEncrypt(cardNo, CipherAesEnum.CARDNOKEY));
            inputMap.put("idCardType", idCardType);
            inputMap.put("idCard", idCard);
            inputMap.put("idCard18", idCard18);
            inputMap.put("owner", owner);
            inputMap.put("phone", phone);
            inputMap.put("validThru", EncryptUtils.aesEncrypt(validThru, CipherAesEnum.CARDNOKEY));
            inputMap.put("cvv2", EncryptUtils.aesEncrypt(cvv2, CipherAesEnum.CARDNOKEY));
            inputMap.put("paymentType", paymentType);
            inputMap.put("accountId", accountId);
            inputMap.put("merOrderId", merOrderId);
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("hardwareId", String.valueOf(receiveMap.get("hardwareId")).trim());
            String userIp = String.valueOf(receiveMap.get("remoteAddr")).trim();
            if(ValidateUtils.isEmpty(userIp)){
                userIp = String.valueOf(receiveMap.get("ipInfo")).trim();
                if(ValidateUtils.isEmpty(userIp)){
                    userIp = "";
                }
            }
            inputMap.put("userIp", userIp);
            resultMap = tdOrderServiceGWImpl.prePaymentOrder(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}
