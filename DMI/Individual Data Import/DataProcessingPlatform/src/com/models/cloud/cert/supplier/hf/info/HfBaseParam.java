package com.models.cloud.cert.supplier.hf.info;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.cert.exception.CodeException;
import com.models.cloud.constants.PropertiesConstant;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

/**
 * 华付基础参数
 * @author yanjie.ji
 * @date 2016年7月15日 
 * @time 上午10:35:24
 */
public abstract class HfBaseParam {

	/**
	 * 交易流水号
	 */
	private String transNo;
	/**
	 * 商户号
	 */
	private String merId;
	/**
	 * 操作日期
	 */
	private String inputDate;
	/**
	 * 操作时间
	 */
	private String inputTime;
	/**
	 * 私有域
	 */
	private String reserveField;
	/**
	 * 密文偏移值
	 */
	private String msgIv;
	/**
	 * 签名值
	 */
	private String chkValue;
	
	public HfBaseParam(){
		String merId = Propertie.APPLICATION.value(PropertiesConstant.CERT_CONFIG_PH_UID);
		if(StringUtils.isEmpty(merId)) throw new CodeException(String.valueOf(Hint.CERT_30001_CONFIG_PH_UID_ERROR.getCode()));
		this.setMerId(merId);
	}
	
	public boolean checkSelf()throws CodeException{
		if(StringUtils.isEmpty(this.getMerId()))
			throw new CodeException(String.valueOf(Hint.CERT_30101_PH_UID_ERROR.getCode()));
		return check();
	}
	
	public abstract boolean check()throws CodeException;
	
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getInputDate() {
		return inputDate;
	}
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	public String getReserveField() {
		return reserveField;
	}
	public void setReserveField(String reserveField) {
		this.reserveField = reserveField;
	}
	public String getMsgIv() {
		return msgIv;
	}
	public void setMsgIv(String msgIv) {
		this.msgIv = msgIv;
	}
	public String getChkValue() {
		return chkValue;
	}
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
}
