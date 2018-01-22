package com.pay.cloud.pay.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台账户交易流水表
 */
public class TdActRec {

    private Long actRecId;

    private Short actTypeId;

    private Long actId;

    private String fundId;

    private Short mainFlag;

    private Short inOutFlag;

    private BigDecimal curAmt;

    private BigDecimal oldBanlance;

    private BigDecimal newBanlance;

    private Long tdId;

    private Short tdTypeId;

    private Short tdBusiTypeId;

    private Long chanActId;

    private Short specFlag1;

    private Short specFlag2;

    private Short specFlag3;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getActRecId() {
        return actRecId;
    }

    public void setActRecId(Long actRecId) {
        this.actRecId = actRecId;
    }

    public Short getActTypeId() {
        return actTypeId;
    }

    public void setActTypeId(Short actTypeId) {
        this.actTypeId = actTypeId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public Short getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(Short mainFlag) {
        this.mainFlag = mainFlag;
    }

    public Short getInOutFlag() {
        return inOutFlag;
    }

    public void setInOutFlag(Short inOutFlag) {
        this.inOutFlag = inOutFlag;
    }

    public BigDecimal getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(BigDecimal curAmt) {
        this.curAmt = curAmt;
    }

    public BigDecimal getOldBanlance() {
        return oldBanlance;
    }

    public void setOldBanlance(BigDecimal oldBanlance) {
        this.oldBanlance = oldBanlance;
    }

    public BigDecimal getNewBanlance() {
        return newBanlance;
    }

    public void setNewBanlance(BigDecimal newBanlance) {
        this.newBanlance = newBanlance;
    }

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public Short getTdTypeId() {
        return tdTypeId;
    }

    public void setTdTypeId(Short tdTypeId) {
        this.tdTypeId = tdTypeId;
    }

    public Short getTdBusiTypeId() {
        return tdBusiTypeId;
    }

    public void setTdBusiTypeId(Short tdBusiTypeId) {
        this.tdBusiTypeId = tdBusiTypeId;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
    }

    public Short getSpecFlag1() {
        return specFlag1;
    }

    public void setSpecFlag1(Short specFlag1) {
        this.specFlag1 = specFlag1;
    }

    public Short getSpecFlag2() {
        return specFlag2;
    }

    public void setSpecFlag2(Short specFlag2) {
        this.specFlag2 = specFlag2;
    }

    public Short getSpecFlag3() {
        return specFlag3;
    }

    public void setSpecFlag3(Short specFlag3) {
        this.specFlag3 = specFlag3;
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