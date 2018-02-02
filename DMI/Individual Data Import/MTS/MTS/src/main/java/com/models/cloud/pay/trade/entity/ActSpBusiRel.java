package com.models.cloud.pay.trade.entity;

import java.util.Date;

public class ActSpBusiRel {

    private Long actId;

    private String busiDetTypeId;

    private Short miPayFlag;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Short getMiPayFlag() {
        return miPayFlag;
    }

    public void setMiPayFlag(Short miPayFlag) {
        this.miPayFlag = miPayFlag;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getBusiDetTypeId() {
        return busiDetTypeId;
    }

    public void setBusiDetTypeId(String busiDetTypeId) {
        this.busiDetTypeId = busiDetTypeId == null ? null : busiDetTypeId.trim();
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