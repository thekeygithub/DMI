package com.models.cloud.pay.escrow.alipay.param;

import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

public interface AlipayParam {

	public boolean checkSelf()throws UnmatchedParamException;
	
}
