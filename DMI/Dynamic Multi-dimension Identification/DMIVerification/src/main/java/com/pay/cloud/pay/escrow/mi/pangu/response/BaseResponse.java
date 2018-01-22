package com.pay.cloud.pay.escrow.mi.pangu.response;

import com.pay.cloud.pay.escrow.mi.pangu.utils.PGConstants;

/**
 * 反馈信息基础类
 * @author yanjie.ji
 * @date 2016年11月24日 
 * @time 上午10:09:09
 */
public class BaseResponse {
	
	private int RTNCode = PGConstants.RESP_CODE_SUCCESS;
	private String RTNMsg;
	public int getRTNCode() {
		return RTNCode;
	}
	public void setRTNCode(int rTNCode) {
		RTNCode = rTNCode;
	}
	public String getRTNMsg() {
		return RTNMsg;
	}
	public void setRTNMsg(String rTNMsg) {
		RTNMsg = rTNMsg;
	}
}
