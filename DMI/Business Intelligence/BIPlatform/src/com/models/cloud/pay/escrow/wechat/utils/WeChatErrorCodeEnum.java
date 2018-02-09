package com.models.cloud.pay.escrow.wechat.utils;

/**
 * 微信支付接口错误码对应枚举
 * Created by yacheng.ji on 2017/2/20.
 */
public enum WeChatErrorCodeEnum {

    NOAUTH("NOAUTH", "商户无此接口权限"),
    NOTENOUGH("NOTENOUGH", "余额不足"),
    ORDERPAID("ORDERPAID", "商户订单已支付"),
    ORDERCLOSED("ORDERCLOSED", "订单已关闭"),
    SYSTEMERROR("SYSTEMERROR", "系统错误"),
    APPID_NOT_EXIST("APPID_NOT_EXIST", "APPID不存在"),
    MCHID_NOT_EXIST("MCHID_NOT_EXIST", "MCHID不存在"),
    APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH", "appid和mch_id不匹配"),
    LACK_PARAMS("LACK_PARAMS", "缺少参数"),
    OUT_TRADE_NO_USED("OUT_TRADE_NO_USED", "商户订单号重复"),
    SIGNERROR("SIGNERROR", "签名错误"),
    XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误"),
    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法"),
    POST_DATA_EMPTY("POST_DATA_EMPTY", "post数据为空"),
    NOT_UTF8("NOT_UTF8", "编码格式错误"),
    ORDERNOTEXIST("ORDERNOTEXIST", "此交易订单号不存在"),
    USER_ACCOUNT_ABNORMAL("USER_ACCOUNT_ABNORMAL", "退款请求失败"),
    INVALID_TRANSACTIONID("INVALID_TRANSACTIONID", "无效transaction_id"),
    PARAM_ERROR("PARAM_ERROR", "参数错误"),
    REFUNDNOTEXIST("REFUNDNOTEXIST", "退款订单查询失败");

    WeChatErrorCodeEnum(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static String getErrorMessageByCode(String errorCode){
        for(WeChatErrorCodeEnum weChatErrorCodeEnum : WeChatErrorCodeEnum.values()){
            if(weChatErrorCodeEnum.getErrorCode().equals(errorCode)){
                return weChatErrorCodeEnum.getErrorMessage();
            }
        }
        return null;
    }

    private String errorCode;
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
