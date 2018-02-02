package com.models.cloud.pay.escrow.mts.response;
/**
 * MTS反馈结果
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:17:10
 */
public class MtsResp extends BaseResp{
	/**
	 * 匹配结果
	 */
	private String result;
	/**
	 * 就诊ID
	 */
	private String visitId;
	/**
	 * 就诊类型
	 */
	private String visitType;
	/**
	 * 数据来源
	 */
	private String dataSource;
	/**
	 * 聚类
	 */
	private String dataType ;
	/**
	 * 标准化参数
	 */
	private String parameters;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
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
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
