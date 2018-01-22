package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class PayUserDevice {

    private Long actId;

    private String userAlias;

    private String deviceCode;
    
    private Long payUserId;

    private String deviceLabel;

    private Short pushFlag;
    
    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private Date lastLoginTime;

	public Long getActId() {
		return actId;
	}

	public void setActId(Long actId) {
		this.actId = actId;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public Long getPayUserId() {
		return payUserId;
	}

	public void setPayUserId(Long payUserId) {
		this.payUserId = payUserId;
	}

	public String getDeviceLabel() {
		return deviceLabel;
	}

	public void setDeviceLabel(String deviceLabel) {
		this.deviceLabel = deviceLabel;
	}

	public Short getPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(Short pushFlag) {
		this.pushFlag = pushFlag;
	}

	public Short getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(Short validFlag) {
		this.validFlag = validFlag;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public Date getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}
