package com.models.cloud.pay.trade.entity;

import java.util.Date;

public class TdSearchTask {

    private Long tdId;

    private String fundId;

    private Short tdTypeId;

    private Short tdStatId;

    private Integer searchCnt;

    private Date nextSearchTime;

    private Short tdProcStatId;

    private String errCode;

    private String errInfo;

    private Date updTime;

    private Date crtTime;

    private Date procBeginTime;

    private Date procEndTime;

    private String remark;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
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

    public Short getTdStatId() {
        return tdStatId;
    }

    public void setTdStatId(Short tdStatId) {
        this.tdStatId = tdStatId;
    }

    public Integer getSearchCnt() {
        return searchCnt;
    }

    public void setSearchCnt(Integer searchCnt) {
        this.searchCnt = searchCnt;
    }

    public Date getNextSearchTime() {
        return nextSearchTime;
    }

    public void setNextSearchTime(Date nextSearchTime) {
        this.nextSearchTime = nextSearchTime;
    }

    public Short getTdProcStatId() {
        return tdProcStatId;
    }

    public void setTdProcStatId(Short tdProcStatId) {
        this.tdProcStatId = tdProcStatId;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo == null ? null : errInfo.trim();
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

    public Date getProcBeginTime() {
        return procBeginTime;
    }

    public void setProcBeginTime(Date procBeginTime) {
        this.procBeginTime = procBeginTime;
    }

    public Date getProcEndTime() {
        return procEndTime;
    }

    public void setProcEndTime(Date procEndTime) {
        this.procEndTime = procEndTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}