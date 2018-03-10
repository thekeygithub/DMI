package com.models.cloud.cert.supplier.ph.info;

import com.models.cloud.cert.info.CertResult;
import com.models.cloud.util.hint.Hint;

/**
 * 普惠实名认证四要素接口返回对象
 * @author yanjie.ji
 * @date 2016年7月13日 
 * @time 下午2:16:01
 */
public class PhResult {
	/**
	 * 为0时表示请求没有错误，大于0时为错误编号
	 */
	private int err;
	/**
	 * err大于0时，错误内容描述
	 */
	private String msg;
	/**
	 * err为0时，受理的的结算订单号
	 */
	private String order_no;
	/**
	 * err为0时，请求返回参数内容
	 */
	private ResultDetail output;
	
	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public ResultDetail getOutput() {
		return output;
	}

	public void setOutput(ResultDetail output) {
		this.output = output;
	}

	public static class ResultDetail{
		/**
		 * 1-认证一致，2-认证不一致
		 */
		private int code;
		/**
		 * 对应的认证描述
		 */
		private String desc;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	public CertResult createCertRes(){
		CertResult result = new CertResult();
		if(this.getErr()!=0||this.getOutput()==null){
			if(this.getErr()>9000){
				result.setCode(String.valueOf(Hint.CERT_30108_PH_SYS_ERROR.getCode()));
				result.setMsg(Hint.CERT_30108_PH_SYS_ERROR.getMessage()+":"+this.getMsg());
			}else{
				int code = getHintCode(this.getErr());
				result.setCode(String.valueOf(code));
				result.setMsg(Hint.getMessage(code));
			}
		}else{
			if(this.getOutput().getCode()==1){
				result.setCode(CertResult.MATCH_CODE);
			}else{
				result.setCode(String.valueOf(Hint.CERT_30124_PH_UNMATCH.getCode()));
				result.setMsg(Hint.getMessage(Hint.CERT_30124_PH_UNMATCH.getCode()));
			}
		}
		return result;
	}
	
	private int getHintCode(int phCode){
		int code = 0;
		switch (phCode) {
		case 1007:
			code = Hint.CERT_30126_PH_INFO_ERROR.getCode();
			break;
		case 1008 :
			code = Hint.CERT_30127_PH_HEAD_ERROR.getCode();
			break;
		case 1009:
			code = Hint.CERT_30128_PH_NOMONEY.getCode();
			break;
		case 1015:
			code = Hint.CERT_30129_PH_CHECK_ERROR.getCode();
			break;
		case 1016:
			code = Hint.CERT_30130_PH_DATA_CHECK_ERROR.getCode();
			break;
		case 1019:
			code = Hint.CERT_30125_PH_BAKNCARD_ERROR.getCode();
			break;
		default:
			code = Hint.CERT_30109_PH_BUSINESS_ERROR.getCode();
			break;
		}
		return code;
	}
	
	private int getHintCode(String desc){
		int code = 0;
		if(desc.indexOf("E0000010")>-1||desc.indexOf("E0000010".toLowerCase())>-1){
			code = Hint.CERT_30110_PH_BUSINESS_MSG_ERROR.getCode();
		}else if(desc.indexOf("E0000011")>-1||desc.indexOf("E0000011".toLowerCase())>-1){
			code = Hint.CERT_30111_PH_BUSINESS_MSGTYPE_ERROR.getCode();
		}else if(desc.indexOf("ER000013")>-1||desc.indexOf("E0000013".toLowerCase())>-1){
			code = Hint.CERT_30112_PH_BUSINESS_IDCARD_ERROR.getCode();
		}else if(desc.indexOf("ER000014")>-1||desc.indexOf("E0000014".toLowerCase())>-1){
			code = Hint.CERT_30113_PH_BUSINESS_NAME_ERROR.getCode();
		}else if(desc.indexOf("ER000016")>-1||desc.indexOf("ER000016".toLowerCase())>-1){
			code = Hint.CERT_30114_PH_BUSINESS_CLIENT_ERROR.getCode();
		}else if(desc.indexOf("ER000021")>-1||desc.indexOf("E0000021".toLowerCase())>-1){
			code = Hint.CERT_30115_PH_BUSINESS_ORDERNO_ERROR.getCode();
		}else if(desc.indexOf("ER000022")>-1||desc.indexOf("E0000022".toLowerCase())>-1){
			code = Hint.CERT_30116_PH_BUSINESS_ORDERNO_ERROR.getCode();
		}else if(desc.indexOf("ER000023")>-1||desc.indexOf("E0000023".toLowerCase())>-1){
			code = Hint.CERT_30117_PH_BUSINESS_BANKTYPE_ERROR.getCode();
		}else if(desc.indexOf("ER000024")>-1||desc.indexOf("E0000024".toLowerCase())>-1){
			code = Hint.CERT_30118_PH_BUSINESS_ROUTE_ERROR.getCode();
		}else if(desc.indexOf("ER000025")>-1||desc.indexOf("E0000025".toLowerCase())>-1){
			code = Hint.CERT_30119_PH_BUSINESS_INFO_ERROR.getCode();
		}else if(desc.indexOf("ER000027")>-1||desc.indexOf("E0000027".toLowerCase())>-1){
			code = Hint.CERT_30120_PH_BUSINESS_CARD_ERROR.getCode();
		}else if(desc.indexOf("ER001001")>-1||desc.indexOf("ER001001".toLowerCase())>-1){
			code = Hint.CERT_30121_PH_BUSINESS_CARDBIN_ERROR.getCode();
		}else if(desc.indexOf("ER001004")>-1||desc.indexOf("ER001004".toLowerCase())>-1){
			code = Hint.CERT_30122_PH_BUSINESS_CARDVALID_ERROR.getCode();
		}else if(desc.indexOf("ER001005")>-1||desc.indexOf("ER001005".toLowerCase())>-1){
			code = Hint.CERT_30123_PH_BUSINESS_CARDTYPE_ERROR.getCode();
		}else {
			code = Hint.CERT_30109_PH_BUSINESS_ERROR.getCode();
		}
		return code;
	}
	
}
