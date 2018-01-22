package com.pay.cloud.pay.escrow.mi.pangu.request;


/**
 * 查询慢性病信息
 * @author yanjie.ji
 * @date 2016年11月18日 
 * @time 下午5:40:51
 */
public class MxbInfoReq extends BaseReq {
	private String personID;
	
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
}
