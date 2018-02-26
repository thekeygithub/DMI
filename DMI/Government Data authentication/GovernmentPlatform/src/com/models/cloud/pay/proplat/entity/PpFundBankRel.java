package com.models.cloud.pay.proplat.entity;

import java.util.Date;

public class PpFundBankRel {
    private Long ppBkRelId;

    private String fundId;

    private Long bankId;

    private String bankName;

    private String ppBankCode;

    private Short payActTypeId;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getPpBkRelId() {
        return ppBkRelId;
    }

    public void setPpBkRelId(Long ppBkRelId) {
        this.ppBkRelId = ppBkRelId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
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

    public String getPpBankCode() {
        return ppBankCode;
    }

    public void setPpBankCode(String ppBankCode) {
        this.ppBankCode = ppBankCode == null ? null : ppBankCode.trim();
    }

    public Short getPayActTypeId() {
        return payActTypeId;
    }

    public void setPayActTypeId(Short payActTypeId) {
        this.payActTypeId = payActTypeId;
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