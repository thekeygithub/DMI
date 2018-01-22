package com.pay.cloud.pay.escrow.alipay.exception;

import java.util.List;

import org.springframework.util.StringUtils;


public class UnmatchedParamException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected List<String> param_names;
	
	private static final String MESSAGE = "参数有误:";
	
	public UnmatchedParamException() {
		super(MESSAGE);
	}
	
	public UnmatchedParamException(String msg) {
		super(MESSAGE+msg);
	}
	
	public UnmatchedParamException(List<String> param_names) {
		super(MESSAGE+getParamStr(param_names));
		this.param_names = param_names;
	}
	
	public UnmatchedParamException(Throwable t) {
		super(MESSAGE, t);
	}
	
	protected static String getParamStr(List<String> param_names){
		StringBuffer paramStr = new StringBuffer();
		if(param_names!=null&&param_names.size()>0){
			for (String string : param_names) {
				if(StringUtils.isEmpty(string)) continue;
				paramStr.append(string).append(";");
			}
		}
		return paramStr.toString();
	}
}
