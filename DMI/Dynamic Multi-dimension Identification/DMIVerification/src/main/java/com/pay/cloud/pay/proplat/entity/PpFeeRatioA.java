package com.pay.cloud.pay.proplat.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PpFeeRatioA {

    private String fundId;

    private String frVer;

    private BigDecimal creditFeeRatio;

    private BigDecimal creditFeeMin;

    private BigDecimal debitFeeRatio;

    private BigDecimal debitFeeMin;

    private BigDecimal withdrFee;

    private String beginDate;

    private String endDate;

    private Date crtTime;

    private String remark;

    private BigDecimal curCreditFeeRatio;

    private BigDecimal curCreditFeeMin;

    private BigDecimal curDebitFeeRatio;

    private BigDecimal curDebitFeeMin;

    public BigDecimal getCreditFeeMin() {
        return creditFeeMin;
    }

    public void setCreditFeeMin(BigDecimal creditFeeMin) {
        this.creditFeeMin = creditFeeMin;
    }

    public BigDecimal getDebitFeeMin() {
        return debitFeeMin;
    }

    public void setDebitFeeMin(BigDecimal debitFeeMin) {
        this.debitFeeMin = debitFeeMin;
    }

    public BigDecimal getCurCreditFeeRatio() {
        return curCreditFeeRatio;
    }

    public void setCurCreditFeeRatio(BigDecimal curCreditFeeRatio) {
        this.curCreditFeeRatio = curCreditFeeRatio;
    }

    public BigDecimal getCurCreditFeeMin() {
        return curCreditFeeMin;
    }

    public void setCurCreditFeeMin(BigDecimal curCreditFeeMin) {
        this.curCreditFeeMin = curCreditFeeMin;
    }

    public BigDecimal getCurDebitFeeRatio() {
        return curDebitFeeRatio;
    }

    public void setCurDebitFeeRatio(BigDecimal curDebitFeeRatio) {
        this.curDebitFeeRatio = curDebitFeeRatio;
    }

    public BigDecimal getCurDebitFeeMin() {
        return curDebitFeeMin;
    }

    public void setCurDebitFeeMin(BigDecimal curDebitFeeMin) {
        this.curDebitFeeMin = curDebitFeeMin;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
    }

    public String getFrVer() {
        return frVer;
    }

    public void setFrVer(String frVer) {
        this.frVer = frVer == null ? null : frVer.trim();
    }

    public BigDecimal getCreditFeeRatio() {
        return creditFeeRatio;
    }

    public void setCreditFeeRatio(BigDecimal creditFeeRatio) {
        this.creditFeeRatio = creditFeeRatio;
    }

    public BigDecimal getDebitFeeRatio() {
        return debitFeeRatio;
    }

    public void setDebitFeeRatio(BigDecimal debitFeeRatio) {
        this.debitFeeRatio = debitFeeRatio;
    }

    public BigDecimal getWithdrFee() {
        return withdrFee;
    }

    public void setWithdrFee(BigDecimal withdrFee) {
        this.withdrFee = withdrFee;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate == null ? null : beginDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
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