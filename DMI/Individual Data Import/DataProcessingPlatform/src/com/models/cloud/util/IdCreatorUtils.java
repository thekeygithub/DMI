package com.models.cloud.util;

import java.util.Date;
import java.util.Hashtable;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.util.hint.Propertie;

/**
 * ID生成器
 */
public class IdCreatorUtils {

    private static Hashtable<String, Object> idHashtable;

    static {
        idHashtable = new Hashtable<>();
        //支付通行证
        IdCreatorVo payTokenVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.PAY_TOKEN_STRING_FLAG);
        //支付订单号
        IdCreatorVo payOrderIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.PAY_ORDER_ID_FLAG);
        //账户交易流水号
        IdCreatorVo accountTradeRecordIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.ACCOUNT_TRADE_RECORD_ID_FLAG);
        //私人银行账号ID
        IdCreatorVo personBankAccountIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.PERSON_BANK_ACCOUNT_ID_FLAG);
        //发送短信唯一ID
        IdCreatorVo smsIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.SMS_ID_FLAG);
        // 生成银行(第三方)账号表-商户标识
        IdCreatorVo spBankActIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.SP_BANK_ACT_ID);
        //提现交易流水号
        IdCreatorVo cashTradeRecordIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.CASH_TRADE_RECORD_ID_FLAG);
        //提现交易单
        IdCreatorVo cashTradeOrderIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.CASH_TRADE_ORDER_ID_FLAG);
        //退款订单号
        IdCreatorVo refundPayOrderIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.REFUND_ORDER_ID_FLAG);
        //短信日志主键
        IdCreatorVo logSmsIdVo = new IdCreatorVo(0, DateUtils.dateToString(new Date(), "yyyyMMdd"), BaseDictConstant.LOG_SMS_REC_ID_FLAG);

        idHashtable.put(BaseDictConstant.PAY_TOKEN_STRING_FLAG, payTokenVo);
        idHashtable.put(BaseDictConstant.PAY_ORDER_ID_FLAG, payOrderIdVo);
        idHashtable.put(BaseDictConstant.SP_BANK_ACT_ID, spBankActIdVo);
        idHashtable.put(BaseDictConstant.ACCOUNT_TRADE_RECORD_ID_FLAG, accountTradeRecordIdVo);
        idHashtable.put(BaseDictConstant.PERSON_BANK_ACCOUNT_ID_FLAG, personBankAccountIdVo);
        idHashtable.put(BaseDictConstant.SMS_ID_FLAG, smsIdVo);
        idHashtable.put(BaseDictConstant.CASH_TRADE_RECORD_ID_FLAG, cashTradeRecordIdVo);
        idHashtable.put(BaseDictConstant.CASH_TRADE_ORDER_ID_FLAG, cashTradeOrderIdVo);
        idHashtable.put(BaseDictConstant.REFUND_ORDER_ID_FLAG, refundPayOrderIdVo);
        idHashtable.put(BaseDictConstant.LOG_SMS_REC_ID_FLAG, logSmsIdVo);
    }

    private static String createId(IdCreatorVo idvo, String id){
        String curDate = DateUtils.dateToString(new Date(), "yyyyMMdd");
        if (curDate.equals(idvo.getCurDate())) {
            long num = idvo.getNum() + 1;
            if (String.valueOf(num).length() > BaseDictConstant.EOP_FORMAT_SEQ_DIGIT) {
                num = 1;
            }
            idvo.setNum(num);
            id = id + String.format("%0" + BaseDictConstant.EOP_FORMAT_SEQ_DIGIT + "d", num);
        } else {
            idvo.setNum(1);
            idvo.setCurDate(curDate);
            id = id + String.format("%0" + BaseDictConstant.EOP_FORMAT_SEQ_DIGIT + "d", idvo.getNum());
        }
        return Propertie.APPLICATION.value("tomcatNumber").concat(id);
    }

    /**
     * 支付通行证，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getPayToken() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.PAY_TOKEN_STRING_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.PAY_TOKEN_STRING_FLAG, idvo);
        return BaseDictConstant.PAY_TOKEN_STRING_FLAG_NO+id;
    }

    /**
     * 支付订单号，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getPayOrderId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.PAY_ORDER_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.PAY_ORDER_ID_FLAG, idvo);
        return BaseDictConstant.PAY_ORDER_ID_FLAG_NO+id;
    }

    /**
     * 退款订单号，生成规则：1位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getRefundOrderId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.REFUND_ORDER_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.REFUND_ORDER_ID_FLAG, idvo);
        return BaseDictConstant.REFUND_ORDER_ID_FLAG_NO+id;
    }

    /**
     * 支付通行证，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static Long getSpBankActId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.SP_BANK_ACT_ID);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.SP_BANK_ACT_ID, idvo);
        return Long.parseLong(BaseDictConstant.SP_BANK_ACT_ID_NO+id);
    }

    /**
     * 账户交易流水号，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getAccountRecordId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.ACCOUNT_TRADE_RECORD_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.ACCOUNT_TRADE_RECORD_ID_FLAG, idvo);
        return BaseDictConstant.ACCOUNT_TRADE_RECORD_ID_FLAG_NO+id;
    }
    
    /**
     * 发送短信唯一id，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getSMSId() {
    	String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
    	IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.SMS_ID_FLAG);
    	id = createId(idvo, id);
    	idHashtable.put(BaseDictConstant.SMS_ID_FLAG, idvo);
    	return BaseDictConstant.SMS_ID_FLAG_NO+id;
    }

    /**
     * 私人银行账号ID，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getPersonBankAccountId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.PERSON_BANK_ACCOUNT_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.PERSON_BANK_ACCOUNT_ID_FLAG, idvo);
        return BaseDictConstant.PERSON_BANK_ACCOUNT_ID_FLAG_NO+id;
    }
    
    /**
     * 提现交易流水号ID，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getCashTradeRecordId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.CASH_TRADE_RECORD_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.CASH_TRADE_RECORD_ID_FLAG, idvo);
        return BaseDictConstant.CASH_TRADE_RECORD_ID_FLAG_NO+id;
    }
    
    /**
     * 提现交易单ID，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getCashTradeOrderId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.CASH_TRADE_ORDER_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.CASH_TRADE_ORDER_ID_FLAG, idvo);
        return BaseDictConstant.CASH_TRADE_ORDER_ID_FLAG_NO+id;
    }

    /**
     * 短信日志记录ID，生成规则：2位数前缀+yyMMddHHmmss+3位数字
     * @return
     */
    public synchronized static String getLogSmsRecId() {
        String id = DateUtils.dateToString(new Date(), "yyMMddHHmmss");
        IdCreatorVo idvo = (IdCreatorVo) idHashtable.get(BaseDictConstant.LOG_SMS_REC_ID_FLAG);
        id = createId(idvo, id);
        idHashtable.put(BaseDictConstant.LOG_SMS_REC_ID_FLAG, idvo);
        return BaseDictConstant.LOG_SMS_REC_ID_FLAG_NO+id;
    }
}

class IdCreatorVo {

    private long num = 1;//订单序号
    private String curDate;//当前日期
    private String serviceType;//业务类型


    public IdCreatorVo(long num, String curDate, String serviceType) {
        this.num = num;
        this.curDate = curDate;
        this.serviceType = serviceType;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
