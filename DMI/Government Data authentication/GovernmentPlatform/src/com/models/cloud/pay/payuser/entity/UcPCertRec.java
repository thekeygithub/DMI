package com.models.cloud.pay.payuser.entity;

import java.util.Date;

public class UcPCertRec {
    private Long pCertId;

    private Short pCertTypeId;

    private String pCertNo;

    private String pName;

    private Short pGendId;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    private Long certRecId;

    public Long getpCertId() {
        return pCertId;
    }

    public void setpCertId(Long pCertId) {
        this.pCertId = pCertId;
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

    public Short getpGendId() {
        return pGendId;
    }

    public void setpGendId(Short pGendId) {
        this.pGendId = pGendId;
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

    public Long getCertRecId() {
        return certRecId;
    }

    public void setCertRecId(Long certRecId) {
        this.certRecId = certRecId;
    }
}