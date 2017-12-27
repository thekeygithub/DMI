package com.ebmi.std.bussiness.dto;

public class SiCardStateQueryInfo {
	
	/**社保卡号    */
	private String siCardNo;
	/**当前状态*/
	private String currentState;
	/**姓名*/
	private String name;
	/**经办日期*/
	private String operTime;
	/**经办日期*/
	private String place;
	
	public String getSiCardNo() {
		return siCardNo;
	}
	public void setSiCardNo(String siCardNo) {
		this.siCardNo = siCardNo;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
}
