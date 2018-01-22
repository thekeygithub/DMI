package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class UcBindMi {
    private Long ucBindId;

    private Long actId;
    
    private String micId;

    private Short pCertTypeId;

    private String pCertNo;

    private String pName;

    private String miCardNo;

    private String miPCode;

    private String pMiId;

    private Short mainBindFlag;

    private Short ucBindStatId;

    private Short kfPrivTypeId;
    
    private String phone;

    private Date updTime;

    private Date crtTime;

    private String remark;

    private String orderByUpdTimeDesc;

    public String getOrderByUpdTimeDesc() {
        return orderByUpdTimeDesc;
    }

    public void setOrderByUpdTimeDesc(String orderByUpdTimeDesc) {
        this.orderByUpdTimeDesc = orderByUpdTimeDesc;
    }

    public Long getUcBindId() {
        return ucBindId;
    }

    public void setUcBindId(Long ucBindId) {
        this.ucBindId = ucBindId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public String getMiCardNo() {
        return miCardNo;
    }

    public void setMiCardNo(String miCardNo) {
        this.miCardNo = miCardNo == null ? null : miCardNo.trim();
    }

    public String getMiPCode() {
        return miPCode;
    }

    public void setMiPCode(String miPCode) {
        this.miPCode = miPCode == null ? null : miPCode.trim();
    }

    public String getpMiId() {
        return pMiId;
    }

    public void setpMiId(String pMiId) {
        this.pMiId = pMiId == null ? null : pMiId.trim();
    }

    public Short getMainBindFlag() {
        return mainBindFlag;
    }

    public void setMainBindFlag(Short mainBindFlag) {
        this.mainBindFlag = mainBindFlag;
    }

    public Short getUcBindStatId() {
        return ucBindStatId;
    }

    public void setUcBindStatId(Short ucBindStatId) {
        this.ucBindStatId = ucBindStatId;
    }

    public Short getKfPrivTypeId() {
        return kfPrivTypeId;
    }

    public void setKfPrivTypeId(Short kfPrivTypeId) {
        this.kfPrivTypeId = kfPrivTypeId;
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

	public String getMicId() {
		return micId;
	}

	public void setMicId(String micId) {
		this.micId = micId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}