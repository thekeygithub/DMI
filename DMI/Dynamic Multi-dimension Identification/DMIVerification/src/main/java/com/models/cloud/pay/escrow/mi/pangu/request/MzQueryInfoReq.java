package com.models.cloud.pay.escrow.mi.pangu.request;

/**
 * 门诊交易记录查询参数
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午11:38:50
 */
public class MzQueryInfoReq extends BaseReq{
	private String serialID; // 门诊流水号
	private String personID; // 个人ID
	
	public String getSerialID() {
		return serialID;
	}
	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	
}
