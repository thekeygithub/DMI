package com.pay.cloud.common.dict.entity;

import java.util.Date;

/**
 * 系统基础配置表Model类
 */
public class DimSysConf {
	
    private String confId;

    private String confName;

    private String confValue;

    private String confInfo;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getConfId() {
        return confId;
    }

    public void setConfId(String confId) {
        this.confId = confId == null ? null : confId.trim();
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName == null ? null : confName.trim();
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue == null ? null : confValue.trim();
    }

    public String getConfInfo() {
        return confInfo;
    }

    public void setConfInfo(String confInfo) {
        this.confInfo = confInfo == null ? null : confInfo.trim();
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