package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class ActPBank {

    private Long bankActId;

    private String fundId;

    private Short fromActTypeId;

    private Long actId;

    private Short payActTypeId;

    private Long bankId;

    private String bankName;

    private String bankAccount;

    private String actName;

    private String authMobile;

    private String pIdNo;

    private String payPropA;

    private String payPropB;

    private String payPropC;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getpIdNo() {
        return pIdNo;
    }

    public void setpIdNo(String pIdNo) {
        this.pIdNo = pIdNo;
    }

    public Long getBankActId() {
        return bankActId;
    }

    public void setBankActId(Long bankActId) {
        this.bankActId = bankActId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
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

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
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

    public String getPayPropA() {
        return payPropA;
    }

    public void setPayPropA(String payPropA) {
        this.payPropA = payPropA == null ? null : payPropA.trim();
    }

    public String getPayPropB() {
        return payPropB;
    }

    public void setPayPropB(String payPropB) {
        this.payPropB = payPropB == null ? null : payPropB.trim();
    }

    public String getPayPropC() {
        return payPropC;
    }

    public void setPayPropC(String payPropC) {
        this.payPropC = payPropC == null ? null : payPropC.trim();
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
}