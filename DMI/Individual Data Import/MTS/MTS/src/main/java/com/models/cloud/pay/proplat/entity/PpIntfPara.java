package com.models.cloud.pay.proplat.entity;

import java.util.Date;

public class PpIntfPara extends PpIntfParaKey {
    private String val;

    private Short encryptFlag;

    private String paraDesc;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val == null ? null : val.trim();
    }

    public Short getEncryptFlag() {
        return encryptFlag;
    }

    public void setEncryptFlag(Short encryptFlag) {
        this.encryptFlag = encryptFlag;
    }

    public String getParaDesc() {
        return paraDesc;
    }

    public void setParaDesc(String paraDesc) {
        this.paraDesc = paraDesc == null ? null : paraDesc.trim();
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