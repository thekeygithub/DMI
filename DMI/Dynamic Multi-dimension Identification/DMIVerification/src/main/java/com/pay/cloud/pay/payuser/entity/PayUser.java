package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class PayUser {
    private Long payUserId;

    private String payUserCode;

    private String pwd;

    private Short payUserStatId;

    private Short mobileUnbindFlag;

    private Short payUserTypeId;

    private String userName;

    private String email;

    private String mobile;

    private Long actId;

    private Date updTime;

    private Date crtTime;

    private Integer pwdFailCnt;

    private Date pwdFailTime;

    private String clientVerCode;

    private Date lastLoginTime;

    private Short curLoginFlag;
    
    private String deviceCode;

    private Long chanActId;

    private Short blackFlag;

    public Short getBlackFlag() {
        return blackFlag;
    }

    public void setBlackFlag(Short blackFlag) {
        this.blackFlag = blackFlag;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
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

    public String getPayUserCode() {
        return payUserCode;
    }

    public void setPayUserCode(String payUserCode) {
        this.payUserCode = payUserCode == null ? null : payUserCode.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Short getPayUserStatId() {
        return payUserStatId;
    }

    public void setPayUserStatId(Short payUserStatId) {
        this.payUserStatId = payUserStatId;
    }

    public Short getMobileUnbindFlag() {
        return mobileUnbindFlag;
    }

    public void setMobileUnbindFlag(Short mobileUnbindFlag) {
        this.mobileUnbindFlag = mobileUnbindFlag;
    }

    public Short getPayUserTypeId() {
        return payUserTypeId;
    }

    public void setPayUserTypeId(Short payUserTypeId) {
        this.payUserTypeId = payUserTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Integer getPwdFailCnt() {
        return pwdFailCnt;
    }

    public void setPwdFailCnt(Integer pwdFailCnt) {
        this.pwdFailCnt = pwdFailCnt;
    }

    public Date getPwdFailTime() {
        return pwdFailTime;
    }

    public void setPwdFailTime(Date pwdFailTime) {
        this.pwdFailTime = pwdFailTime;
    }

    public String getClientVerCode() {
        return clientVerCode;
    }

    public void setClientVerCode(String clientVerCode) {
        this.clientVerCode = clientVerCode == null ? null : clientVerCode.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Short getCurLoginFlag() {
		return curLoginFlag;
	}

	public void setCurLoginFlag(Short curLoginFlag) {
		this.curLoginFlag = curLoginFlag;
	}
}