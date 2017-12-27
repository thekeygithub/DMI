package com.ebmi.std.bussiness.dto;

public class SiCardStateInfo {


	/** 姓名*/
	private String name;
	/** 社会保障号*/
	private String idNo;
	/** 卡号*/
	private String siCardNo;
	/**1：状态已受理，2办理中（未完结）、3已挂失（完结）*/
	private String state;
	/** 挂失时间*/
	private String lockDate;
	/** 解挂时间 */
	private String unlockDate;
	/**主键*/
	private String pMiId;

	public String getpMiId() {
		return pMiId;
	}
	public void setpMiId(String pMiId) {
		this.pMiId = pMiId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLockDate() {
		return lockDate;
	}
	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}
	public String getUnlockDate() {
		return unlockDate;
	}
	public void setUnlockDate(String unlockDate) {
		this.unlockDate = unlockDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getSiCardNo() {
		return siCardNo;
	}
	public void setSiCardNo(String siCardNo) {
		this.siCardNo = siCardNo;
	}

	
}
