package com.models.cloud.pay.trade.entity;

import java.util.Date;

public class TdOrdExtWithdr {
    private Long tdId;

    private Short tdWithdrStatId;

    private Integer searchCnt;

    private Date nextSearchTime;

    private String fundTdCode;

    private String fundTdMsg;

    private Long bankId;

    private String bankCode;

    private String bankAccount;

    private String bankActName;

    private String payPropA;

    private String payPropB;

    private String payPropC;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public Short getTdWithdrStatId() {
        return tdWithdrStatId;
    }

    public void setTdWithdrStatId(Short tdWithdrStatId) {
        this.tdWithdrStatId = tdWithdrStatId;
    }

    public Integer getSearchCnt() {
        return searchCnt;
    }

    public void setSearchCnt(Integer searchCnt) {
        this.searchCnt = searchCnt;
    }

    public Date getNextSearchTime() {
        return nextSearchTime;
    }

    public void setNextSearchTime(Date nextSearchTime) {
        this.nextSearchTime = nextSearchTime;
    }

    public String getFundTdCode() {
        return fundTdCode;
    }

    public void setFundTdCode(String fundTdCode) {
        this.fundTdCode = fundTdCode == null ? null : fundTdCode.trim();
    }

    public String getFundTdMsg() {
        return fundTdMsg;
    }

    public void setFundTdMsg(String fundTdMsg) {
        this.fundTdMsg = fundTdMsg == null ? null : fundTdMsg.trim();
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankActName() {
        return bankActName;
    }

    public void setBankActName(String bankActName) {
        this.bankActName = bankActName == null ? null : bankActName.trim();
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