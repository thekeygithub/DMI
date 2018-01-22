package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class ActPerson {
    private Long actId;

    private String micId;

    private Long actCode;

    private String payPwd;

    private Short pCertTypeId;

    private String pCertNo;

    private String siCardNo;

    private String pName;

    private Short pGendId;

    private Date pBirth;

    private Short siChkFlag;

    private Short realNameFlag;

    private Short actStatId;

    private String saltVal;
    
    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;
    
    private String authBankAccount;
    private String authMobile;
    private Long pCertId;
    

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId == null ? null : micId.trim();
    }

    public Long getActCode() {
        return actCode;
    }

    public void setActCode(Long actCode) {
        this.actCode = actCode;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd == null ? null : payPwd.trim();
    }

    public Short getpCertTypeId() {
        return pCertTypeId;
    }

    public void setpCertTypeId(Short pCertTypeId) {
        this.pCertTypeId = pCertTypeId;
    }

    public String getpCertNo() {
        return pCertNo;
    }

    public void setpCertNo(String pCertNo) {
        this.pCertNo = pCertNo == null ? null : pCertNo.trim();
    }

    public String getSiCardNo() {
        return siCardNo;
    }

    public void setSiCardNo(String siCardNo) {
        this.siCardNo = siCardNo == null ? null : siCardNo.trim();
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public Short getpGendId() {
        return pGendId;
    }

    public void setpGendId(Short pGendId) {
        this.pGendId = pGendId;
    }

    public Date getpBirth() {
        return pBirth;
    }

    public void setpBirth(Date pBirth) {
        this.pBirth = pBirth;
    }

    public Short getSiChkFlag() {
        return siChkFlag;
    }

    public void setSiChkFlag(Short siChkFlag) {
        this.siChkFlag = siChkFlag;
    }

    public Short getRealNameFlag() {
        return realNameFlag;
    }

    public void setRealNameFlag(Short realNameFlag) {
        this.realNameFlag = realNameFlag;
    }

    public Short getActStatId() {
        return actStatId;
    }

    public void setActStatId(Short actStatId) {
        this.actStatId = actStatId;
    }

    public String getSaltVal() {
		return saltVal;
	}

	public void setSaltVal(String saltVal) {
		this.saltVal = saltVal;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getAuthBankAccount() {
		return authBankAccount;
	}

	public void setAuthBankAccount(String authBankAccount) {
		this.authBankAccount = authBankAccount;
	}

	public String getAuthMobile() {
		return authMobile;
	}

	public void setAuthMobile(String authMobile) {
		this.authMobile = authMobile;
	}

	public Long getpCertId() {
		return pCertId;
	}

	public void setpCertId(Long pCertId) {
		this.pCertId = pCertId;
	}
    
    
}