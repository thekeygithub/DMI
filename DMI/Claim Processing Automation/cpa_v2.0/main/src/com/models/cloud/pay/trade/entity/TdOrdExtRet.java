package com.models.cloud.pay.trade.entity;

import java.util.Date;

public class TdOrdExtRet {
    private Long tdId;

    private Long retTdId;

    private Short tdRetStatId;

    private Integer searchCnt;

    private Date nextSearchTime;

    private String fundTdCode;

    private String chanRetCode;

    private String chanRetDesc;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public Long getRetTdId() {
        return retTdId;
    }

    public void setRetTdId(Long retTdId) {
        this.retTdId = retTdId;
    }

    public Short getTdRetStatId() {
        return tdRetStatId;
    }

    public void setTdRetStatId(Short tdRetStatId) {
        this.tdRetStatId = tdRetStatId;
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

    public String getFundTdCode() {
        return fundTdCode;
    }

    public void setFundTdCode(String fundTdCode) {
        this.fundTdCode = fundTdCode == null ? null : fundTdCode.trim();
    }

    public String getChanRetCode() {
        return chanRetCode;
    }

    public void setChanRetCode(String chanRetCode) {
        this.chanRetCode = chanRetCode == null ? null : chanRetCode.trim();
    }

    public String getChanRetDesc() {
        return chanRetDesc;
    }

    public void setChanRetDesc(String chanRetDesc) {
        this.chanRetDesc = chanRetDesc == null ? null : chanRetDesc.trim();
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