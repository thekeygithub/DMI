package com.pay.cloud.cert.supplier.ph.info;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.cert.exception.CodeException;
import com.pay.cloud.constants.PropertiesConstant;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;
/**
 * 普惠实名认证接口基础参数
 * @author yanjie.ji
 * @date 2016年7月13日 
 * @time 下午2:53:16
 */
public abstract class PhBaseParam {
	
	/**
	 * 合作商户id
	 */
	private String uid;
	/**
	 * 签名
	 */
	private String sign;
	
	public PhBaseParam(){
		String uid = Propertie.APPLICATION.value(PropertiesConstant.CERT_CONFIG_PH_UID);
		if(StringUtils.isEmpty(uid)) throw new CodeException(String.valueOf(Hint.CERT_30001_CONFIG_PH_UID_ERROR.getCode()));
		this.setUid(uid);
	}
	
	public boolean checkSelf()throws CodeException{
		if(StringUtils.isEmpty(this.getUid()))
			throw new CodeException(String.valueOf(Hint.CERT_30101_PH_UID_ERROR.getCode()));
		return check();
	}
	
	public abstract boolean check()throws CodeException;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public TreeMap<String,Object> getProperties(){
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		if(StringUtils.isNotEmpty(getUid()))
			map.put("uid", getUid());
		return map;
	}
	
	public String getParam(){
		TreeMap<String,Object> map = getProperties();
		if(map==null||map.isEmpty()) return "";
		StringBuffer paramStr = new StringBuffer();
		boolean first = true;
		for (Entry<String, Object> entry : map.entrySet()) {
			if(entry.getValue()==null) continue;
			if(first){
				paramStr.append(entry.getKey()).append("=").append(entry.getValue());
				first=false;
			}else{
				paramStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
		}
		return paramStr.toString();
	}
}
