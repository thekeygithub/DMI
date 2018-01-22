package com.pay.cloud.pay.escrow.alipay.exception;

import java.util.List;


public class ParamLengthException extends UnmatchedParamException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "参数长度有误:";
	
	public ParamLengthException(List<String> param_names) {
		super(MESSAGE+getParamStr(param_names));
		this.param_names = param_names;
	}
}
