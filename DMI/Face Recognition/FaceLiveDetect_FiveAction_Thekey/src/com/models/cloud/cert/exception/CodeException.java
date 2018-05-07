package com.models.cloud.cert.exception;
/**
 * 异常信息类
 * @author yanjie.ji
 * @date 2016年7月12日 
 * @time 上午10:23:56
 */
public class CodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	/**
	 * 异常错误码
	 */
	private String code;
	/**
	 * 异常描述
	 */
	private String err_msg;
	
	public CodeException(String code){
		super(code);
		this.code=code;
	}
	
	public CodeException(String code,String err_msg){
		super(code+err_msg);
		this.code=code;
		this.err_msg=err_msg;
	}
	
	public CodeException(String code, Throwable cause) {
        super(code, cause);
        this.code=code;
    }
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
}
