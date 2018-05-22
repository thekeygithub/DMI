package com.models.cloud.pay.escrow.alipay.exception;

import java.util.List;


public class ParamEmptyException extends UnmatchedParamException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "参数不能为空：";
	
	public ParamEmptyException(List<String> param_names) {
		super(MESSAGE+getParamStr(param_names));
		this.param_names = param_names;
	}
}
