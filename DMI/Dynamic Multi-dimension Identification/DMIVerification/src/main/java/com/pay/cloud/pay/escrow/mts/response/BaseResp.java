package com.pay.cloud.pay.escrow.mts.response;

/**
 * MTS返回结果基类
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:21:28
 */
public class BaseResp {
	/**
	 * 状态码
	 */
	private String status;
	/**
	 * 怀疑诊断标识
	 */
	private String doubtDiag;
	/**
	 * 批次号
	 */
	private String batchNum;
	/**
	 * 标化类型标识
	 */
	private String dataClass;
	/**
	 * 生成时间
	 */
	private String operTime;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDoubtDiag() {
		return doubtDiag;
	}
	public void setDoubtDiag(String doubtDiag) {
		this.doubtDiag = doubtDiag;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public String getDataClass() {
		return dataClass;
	}
	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
}
