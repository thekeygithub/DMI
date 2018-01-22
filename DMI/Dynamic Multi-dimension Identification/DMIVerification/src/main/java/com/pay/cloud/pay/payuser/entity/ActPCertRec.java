package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class ActPCertRec {

    private Long certRecId;

    private Short fromActTypeId;

    private Long actId;

    private Short payActTypeId;

    private String bankAccount;

    private String actName;

    private String authMobile;

    private String pIdNo;

    private Short certResFlag;

    private String errInfo;

    private Date updTime;

    private Date crtTime;

    private String remark;

    private Long chanActId;

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
    }

    public Long getCertRecId() {
        return certRecId;
    }

    public void setCertRecId(Long certRecId) {
        this.certRecId = certRecId;
    }

    public Short getFromActTypeId() {
        return fromActTypeId;
    }

    public void setFromActTypeId(Short fromActTypeId) {
        this.fromActTypeId = fromActTypeId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Short getPayActTypeId() {
        return payActTypeId;
    }

    public void setPayActTypeId(Short payActTypeId) {
        this.payActTypeId = payActTypeId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName == null ? null : actName.trim();
    }

    public String getAuthMobile() {
        return authMobile;
    }

    public void setAuthMobile(String authMobile) {
        this.authMobile = authMobile == null ? null : authMobile.trim();
    }

    public String getpIdNo() {
        return pIdNo;
    }

    public void setpIdNo(String pIdNo) {
        this.pIdNo = pIdNo == null ? null : pIdNo.trim();
    }

    public Short getCertResFlag() {
        return certResFlag;
    }

    public void setCertResFlag(Short certResFlag) {
        this.certResFlag = certResFlag;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo == null ? null : errInfo.trim();
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
}