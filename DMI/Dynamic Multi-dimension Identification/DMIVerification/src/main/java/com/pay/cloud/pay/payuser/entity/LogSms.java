package com.pay.cloud.pay.payuser.entity;

import java.util.Date;

public class LogSms {

    private Long logId;

    private String phoneNo;

    private Long actId;

    private Long chanActId;

    private String busiTypeCode;

    private String smsChanCode;

    private Integer retryCnt;

    private Short smsSendFlag;

    private Date logTime;

    private String remark;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo == null ? null : phoneNo.trim();
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
    }

    public String getBusiTypeCode() {
        return busiTypeCode;
    }

    public void setBusiTypeCode(String busiTypeCode) {
        this.busiTypeCode = busiTypeCode == null ? null : busiTypeCode.trim();
    }

    public String getSmsChanCode() {
        return smsChanCode;
    }

    public void setSmsChanCode(String smsChanCode) {
        this.smsChanCode = smsChanCode == null ? null : smsChanCode.trim();
    }

    public Integer getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
    }

    public Short getSmsSendFlag() {
        return smsSendFlag;
    }

    public void setSmsSendFlag(Short smsSendFlag) {
        this.smsSendFlag = smsSendFlag;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}