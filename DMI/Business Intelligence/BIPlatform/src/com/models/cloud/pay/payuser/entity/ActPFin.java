package com.models.cloud.pay.payuser.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ActPFin {
    private Long actId;

    private BigDecimal bal;

    private BigDecimal avalBal;

    private BigDecimal guartInit;

    private BigDecimal guartBal;

    private BigDecimal travel;

    private BigDecimal frz;

    private BigDecimal limPT;

    private BigDecimal limPDay;

    private BigDecimal limWT;

    private BigDecimal limWDay;

    private BigDecimal withDayAmt;

    private Date lastWithTime;

    private String fundInfo1;

    private String fundInfo2;

    private String fundInfo3;

    private Date updTime;

    private Date crtTime;

    private String remark;

	public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public BigDecimal getAvalBal() {
        return avalBal;
    }

    public void setAvalBal(BigDecimal avalBal) {
        this.avalBal = avalBal;
    }

    public BigDecimal getGuartInit() {
        return guartInit;
    }

    public void setGuartInit(BigDecimal guartInit) {
        this.guartInit = guartInit;
    }

    public BigDecimal getGuartBal() {
        return guartBal;
    }

    public void setGuartBal(BigDecimal guartBal) {
        this.guartBal = guartBal;
    }

    public BigDecimal getTravel() {
        return travel;
    }

    public void setTravel(BigDecimal travel) {
        this.travel = travel;
    }

    public BigDecimal getFrz() {
        return frz;
    }

    public void setFrz(BigDecimal frz) {
        this.frz = frz;
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

    public BigDecimal getWithDayAmt() {
        return withDayAmt;
    }

    public void setWithDayAmt(BigDecimal withDayAmt) {
        this.withDayAmt = withDayAmt;
    }

    public Date getLastWithTime() {
        return lastWithTime;
    }

    public void setLastWithTime(Date lastWithTime) {
        this.lastWithTime = lastWithTime;
    }

    public String getFundInfo1() {
        return fundInfo1;
    }

    public void setFundInfo1(String fundInfo1) {
        this.fundInfo1 = fundInfo1 == null ? null : fundInfo1.trim();
    }

    public String getFundInfo2() {
        return fundInfo2;
    }

    public void setFundInfo2(String fundInfo2) {
        this.fundInfo2 = fundInfo2 == null ? null : fundInfo2.trim();
    }

    public String getFundInfo3() {
        return fundInfo3;
    }

    public void setFundInfo3(String fundInfo3) {
        this.fundInfo3 = fundInfo3 == null ? null : fundInfo3.trim();
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