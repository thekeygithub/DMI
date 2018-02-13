package com.models.cloud.pay.proplat.entity;

import java.util.Date;

public class PpFundType {

    private String ppFundTypeId;

    private String ppFundTypeName;

    private String dispName;

    private String imageId;

    private Integer dispOrder;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getPpFundTypeId() {
        return ppFundTypeId;
    }

    public void setPpFundTypeId(String ppFundTypeId) {
        this.ppFundTypeId = ppFundTypeId == null ? null : ppFundTypeId.trim();
    }

    public String getPpFundTypeName() {
        return ppFundTypeName;
    }

    public void setPpFundTypeName(String ppFundTypeName) {
        this.ppFundTypeName = ppFundTypeName == null ? null : ppFundTypeName.trim();
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName == null ? null : dispName.trim();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public Integer getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(Integer dispOrder) {
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