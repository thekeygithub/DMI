package com.models.cloud.util.hint;

public enum OperateType {
	REGISTER(1), 
	//未绑卡的支付密码找回
	SETPAYMENTPASS(2),
	//密码找回
	CALLBACKPASS(3),
	//实名认证
	REALVALID(4)
	;
 
	private OperateType(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

}
