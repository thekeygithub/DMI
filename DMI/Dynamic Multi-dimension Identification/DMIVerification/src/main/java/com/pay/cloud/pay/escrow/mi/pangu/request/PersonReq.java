package com.pay.cloud.pay.escrow.mi.pangu.request;


/**
 * 查询个人信息参数
 * @author yanjie.ji
 * @date 2016年11月18日 
 * @time 下午5:39:29
 */
public class PersonReq extends BaseReq{
	private String cardID;
	private String perCardID;
	private String perName;
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getPerCardID() {
		return perCardID;
	}
	public void setPerCardID(String perCardID) {
		this.perCardID = perCardID;
	}
	public String getPerName() {
		return perName;
	}
	public void setPerName(String perName) {
		this.perName = perName;
	}
}
