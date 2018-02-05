package com.models.cloud.pay.trade.entity;

import java.util.Date;

public class TdMiPara {

    private Long tdId;

    private String preClmPara;

    private String preClmRet;

    private String clmPara;

    private String clmRet;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public String getPreClmPara() {
        return preClmPara;
    }

    public void setPreClmPara(String preClmPara) {
        this.preClmPara = preClmPara == null ? null : preClmPara.trim();
    }

    public String getClmPara() {
        return clmPara;
    }

    public void setClmPara(String clmPara) {
        this.clmPara = clmPara == null ? null : clmPara.trim();
    }

    public String getPreClmRet() {
        return preClmRet;
    }

    public void setPreClmRet(String preClmRet) {
        this.preClmRet = preClmRet == null ? null : preClmRet.trim();
    }

    public String getClmRet() {
        return clmRet;
    }

    public void setClmRet(String clmRet) {
        this.clmRet = clmRet == null ? null : clmRet.trim();
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