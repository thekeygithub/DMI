package com.pay.cloud.pay.escrow.alipay.param;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

public interface AlipayParam {

	public boolean checkSelf()throws UnmatchedParamException;
	
}
