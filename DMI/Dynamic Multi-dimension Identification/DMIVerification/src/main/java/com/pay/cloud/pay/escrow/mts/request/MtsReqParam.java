package com.pay.cloud.pay.escrow.mts.request;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午10:06:35
 */
public class MtsReqParam extends BaseReq{
	/**
	 * 标准化参数
	 */
	private String parameters;
	
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	@Override
	protected Map<String, String> getProp() {
		Map<String, String> map = new HashMap<String,String>();
		map.put("parameters", parameters);
		return map;
	}
}
