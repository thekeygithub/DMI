package com.models.cloud.pay.escrow.mi.pangu.request;


/**
 * 结算记录状态查询参数
 * @author yanjie.ji
 * @date 2016年12月14日 
 * @time 下午5:45:06
 */
public class TollInfoReq extends BaseReq {

	private String serialID;//流水号
	private String personID;//个人编号
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
