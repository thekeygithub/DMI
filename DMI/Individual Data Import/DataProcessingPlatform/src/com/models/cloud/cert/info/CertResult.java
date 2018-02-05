package com.models.cloud.cert.info;
/**
 * 实名认证结果
 * @author yanjie.ji
 * @date 2016年7月14日 
 * @time 上午10:41:49
 */
public class CertResult {
	/**
	 * 实名认证匹配结果：一致
	 */
	public static final String MATCH_CODE="0";

	/**
	 * 匹配结果编码
	 */
	private String code;
	/**
	 * 匹配结果描述
	 */
	private String msg;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
