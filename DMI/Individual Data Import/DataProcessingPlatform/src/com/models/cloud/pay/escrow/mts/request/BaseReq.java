package com.models.cloud.pay.escrow.mts.request;

import java.util.HashMap;
import java.util.Map;

/**
 * MTS接口请求参数基类
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:28:52
 */
public abstract class BaseReq {
	/**
	 * 就诊ID 必填
	 */
	private String visitId;
	/**
	 * 就诊类型  非必填
	 */
	private String visitType;
	/**
	 * 数据来源
	 */
	private String dataSource;
	/**
	 * 聚类
	 */
	private String dataType;
	
	public Map<String,String> getProperties(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("visitId", visitId);
		map.put("visitType", visitType);
		map.put("dataSource", dataSource);
		map.put("dataType", dataType);
		map.putAll(getProp());
		return map;
	}
	
	protected abstract Map<String,String> getProp();
	
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
