package com.models.cloud.pay.escrow.huiyue.utils;

/**
 * 慧阅实人认证错误码对应枚举
 * Created by yacheng.ji on 2017/3/9.
 */
public enum HyErrorCodeEnum {

    ERR1001("ERR1001", "未找到用户"),
    ERR1002("ERR1002", "数据格式不正确"),
    ERR1003("ERR1003", "操作数据库失败(系统错误)"),
    ERR1004("ERR1004", "非法访问,无权限"),
    ERR1005("ERR1005", "没有需要验证的数据,空数据"),
    ERR1006("ERR1006", "数据MD5不正确"),
    ERR1007("ERR1007", "帐号被停用"),
    ERR1008("ERR1008", "参数不完整"),
    ERR1009("ERR1009", "3DES加解密失败"),
    ERR1010("ERR1010", "info信息为空"),
    ERR1011("ERR1011", "照片质量不合格"),
    ERR1012("ERR1012", "身份证正面解析失败"),
    ERR1013("ERR1013", "身份证反面解析失败"),
    ERR1014("ERR1014", "身份证号码格式不正确"),
    ERR1015("ERR1015", "取结果接口info信息为空"),
    ERR1016("ERR1016", "取结果接口正在验证"),
    ERR1017("ERR1017", "姓名不合法"),
    ERR9999("ERR9999", "其他错误");

    HyErrorCodeEnum(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static String getErrorMessageByCode(String errorCode){
        for(HyErrorCodeEnum weChatErrorCodeEnum : HyErrorCodeEnum.values()){
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
