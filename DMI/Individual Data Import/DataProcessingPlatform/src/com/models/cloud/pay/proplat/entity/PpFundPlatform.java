package com.models.cloud.pay.proplat.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PpFundPlatform {

    private String fundId;

    private String ppFundTypeId;

    private Long spActId;

    private String fundName;

    private String actName;

    private String actCode;

    private String imageId;

    private Short fundVirtualFlag;

    private String feeTabSuffix;

    private BigDecimal limPT;

    private BigDecimal limPDay;

    private BigDecimal limWT;

    private BigDecimal limWDay;

    private Integer dispOrder;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getPpFundTypeId() {
        return ppFundTypeId;
    }

    public void setPpFundTypeId(String ppFundTypeId) {
        this.ppFundTypeId = ppFundTypeId;
    }

    public Long getSpActId() {
        return spActId;
    }

    public void setSpActId(Long spActId) {
        this.spActId = spActId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName == null ? null : fundName.trim();
    }

    public Short getFundVirtualFlag() {
        return fundVirtualFlag;
    }

    public void setFundVirtualFlag(Short fundVirtualFlag) {
        this.fundVirtualFlag = fundVirtualFlag;
    }

    public String getFeeTabSuffix() {
        return feeTabSuffix;
    }

    public void setFeeTabSuffix(String feeTabSuffix) {
        this.feeTabSuffix = feeTabSuffix == null ? null : feeTabSuffix.trim();
    }

    public BigDecimal getLimPT() {
        return limPT;
    }

    public void setLimPT(BigDecimal limPT) {
        this.limPT = limPT;
    }

    public BigDecimal getLimPDay() {
        return limPDay;
    }

    public void setLimPDay(BigDecimal limPDay) {
        this.limPDay = limPDay;
    }

    public BigDecimal getLimWT() {
        return limWT;
    }

    public void setLimWT(BigDecimal limWT) {
        this.limWT = limWT;
    }

    public BigDecimal getLimWDay() {
        return limWDay;
    }

    public void setLimWDay(BigDecimal limWDay) {
        this.limWDay = limWDay;
    }

    public Integer getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(Integer dispOrder) {
        this.dispOrder = dispOrder;
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