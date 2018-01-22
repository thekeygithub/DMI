package com.pay.cloud.pay.trade.entity;

import java.util.Date;

public class DimBusiDetType {
    private String busiDetTypeId;

    private String dispName;

    private String fullName;

    private Short tdBusiTypeId;

    private Short busiInFlag;

    private Short useMiFlag;

    private Short dispOrder;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getBusiDetTypeId() {
        return busiDetTypeId;
    }

    public void setBusiDetTypeId(String busiDetTypeId) {
        this.busiDetTypeId = busiDetTypeId == null ? null : busiDetTypeId.trim();
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName == null ? null : dispName.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public Short getTdBusiTypeId() {
        return tdBusiTypeId;
    }

    public void setTdBusiTypeId(Short tdBusiTypeId) {
        this.tdBusiTypeId = tdBusiTypeId;
    }

    public Short getBusiInFlag() {
        return busiInFlag;
    }

    public void setBusiInFlag(Short busiInFlag) {
        this.busiInFlag = busiInFlag;
    }

    public Short getUseMiFlag() {
        return useMiFlag;
    }

    public void setUseMiFlag(Short useMiFlag) {
        this.useMiFlag = useMiFlag;
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