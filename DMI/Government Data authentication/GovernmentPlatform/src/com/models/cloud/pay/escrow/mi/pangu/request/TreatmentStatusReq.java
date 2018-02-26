package com.models.cloud.pay.escrow.mi.pangu.request;
/**
 * 医疗待遇封锁信息参数
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午11:58:52
 */
public class TreatmentStatusReq extends BaseReq {
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
