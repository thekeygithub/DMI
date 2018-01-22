package com.pay.cloud.pay.escrow.mi.pangu.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BaseParam<T extends BaseReq> {
	
	private int cmd;
	private T param;
	private String fixid;
	private String cycid;
	
	/**
	 * 检查
	 * @return
	 */
	public boolean check(){
		return true;
	}
	/**
	 * 获取属性值
	 * @return
	 */
	public Map<String,String> takeProperties(){
		Map<String,String> map  = new HashMap<String,String>();
		if(param!=null) map.put("param", param.takeJson());
		map.put("cmd", String.valueOf(getCmd()));
		map.put("fixid",getFixid());
		if(StringUtils.isNotEmpty(getCycid()))map.put("cycid", String.valueOf(getCycid()));
		return map;
	}
	
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getFixid() {
		return fixid;
	}
	public void setFixid(String fixid) {
		this.fixid = fixid;
	}
	public String getCycid() {
		return cycid;
	}
	public void setCycid(String cycid) {
		this.cycid = cycid;
	}
}
