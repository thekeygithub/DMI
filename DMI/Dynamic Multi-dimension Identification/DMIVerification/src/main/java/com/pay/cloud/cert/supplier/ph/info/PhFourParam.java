package com.pay.cloud.cert.supplier.ph.info;

import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.cert.exception.CodeException;
import com.pay.cloud.cert.info.EbaoFourParam;
import com.pay.cloud.cert.utils.CheckUtils;
import com.pay.cloud.cert.utils.IdCardNoUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;
/**
 * 普惠实名认证--四要素
 * @author yanjie.ji
 * @date 2016年7月12日 
 * @time 上午10:07:18
 */
public class PhFourParam extends PhBaseParam{

	private String realname;
	private String idcard;
	private String bankcard;
	private String mobile;
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public PhFourParam(EbaoFourParam param){
		super();
		if(param!=null){
			this.setRealname(param.getRealname());
			this.setIdcard(param.getIdcard());
			this.setBankcard(param.getBankcard());
			this.setMobile(param.getMobile());
		}
	}
	
	@Override
	public boolean check() throws CodeException{
		if(!CheckUtils.checkChineseName(this.getRealname()))
			throw new CodeException(String.valueOf(Hint.CERT_31004_REAL_NAME_ERROR.getCode()));
		if(!IdCardNoUtils.isIdCard(this.getIdcard()))
			throw new CodeException(String.valueOf(Hint.CERT_31001_ID_CARD_ERROR.getCode()));
		if(!ValidateUtils.isNumber(this.getBankcard()))
			throw new CodeException(String.valueOf(Hint.CERT_31002_BANK_CARD_ERROR.getCode()));
		if(!CheckUtils.checkPhone(this.getMobile()))
			throw new CodeException(String.valueOf(Hint.CERT_31003_PHONE_NO_ERROR.getCode()));
		
		return true;
	}
	
	public TreeMap<String,Object> getProperties(){
		TreeMap<String,Object> map = super.getProperties();
		if(map==null) map = new TreeMap<String,Object>();
		if(StringUtils.isNotEmpty(this.getRealname()))
			map.put("realname", this.getRealname());
		if(StringUtils.isNotEmpty(this.getIdcard()))
			map.put("idcard", this.getIdcard());
		if(StringUtils.isNotEmpty(this.getBankcard()))
			map.put("bankcard", this.getBankcard());
		if(StringUtils.isNotEmpty(this.getMobile()))
			map.put("mobile", this.getMobile());
		return map;
	}
}
