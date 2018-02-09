package com.models.cloud.pay.supplier.entity;

import java.util.Date;

public class SpServIntf {
    private Long spIntfId;

    private String intfName;

    private Long paId;

    private String intfUrl;

    private Short intfLvl;

    private Short childFlag;

    private Short dispOrder;

    private Short validFlag;

    private Short intfLogFlag;

    private Date updTime;

    private Date crtTime;

    private Long updUser;

    private Long crtUser;

    private String remark;

    public Long getSpIntfId() {
        return spIntfId;
    }

    public void setSpIntfId(Long spIntfId) {
        this.spIntfId = spIntfId;
    }

    public String getIntfName() {
        return intfName;
    }

    public void setIntfName(String intfName) {
        this.intfName = intfName == null ? null : intfName.trim();
    }

    public Long getPaId() {
        return paId;
    }

    public void setPaId(Long paId) {
        this.paId = paId;
    }

    public String getIntfUrl() {
        return intfUrl;
    }

    public void setIntfUrl(String intfUrl) {
        this.intfUrl = intfUrl == null ? null : intfUrl.trim();
    }

    public Short getIntfLvl() {
        return intfLvl;
    }

    public void setIntfLvl(Short intfLvl) {
        this.intfLvl = intfLvl;
    }

    public Short getChildFlag() {
        return childFlag;
    }

    public void setChildFlag(Short childFlag) {
        this.childFlag = childFlag;
    }

    public Short getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(Short dispOrder) {
        this.dispOrder = dispOrder;
    }

    public Short getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Short validFlag) {
        this.validFlag = validFlag;
    }

    public Short getIntfLogFlag() {
        return intfLogFlag;
    }

    public void setIntfLogFlag(Short intfLogFlag) {
        this.intfLogFlag = intfLogFlag;
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

    public Long getUpdUser() {
        return updUser;
    }

    public void setUpdUser(Long updUser) {
        this.updUser = updUser;
    }

    public Long getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(Long crtUser) {
        this.crtUser = crtUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}