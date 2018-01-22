package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class LogOperAct {
    private Long logId;

    private Short actTypeId;

    private Long actId;

    private Short operCreFlag;

    private String operColName;

    private String valOld;

    private String valNew;

    private Date logTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
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

    public Short getOperCreFlag() {
        return operCreFlag;
    }

    public void setOperCreFlag(Short operCreFlag) {
        this.operCreFlag = operCreFlag;
    }

    public String getOperColName() {
        return operColName;
    }

    public void setOperColName(String operColName) {
        this.operColName = operColName == null ? null : operColName.trim();
    }

    public String getValOld() {
        return valOld;
    }

    public void setValOld(String valOld) {
        this.valOld = valOld == null ? null : valOld.trim();
    }

    public String getValNew() {
        return valNew;
    }

    public void setValNew(String valNew) {
        this.valNew = valNew == null ? null : valNew.trim();
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}