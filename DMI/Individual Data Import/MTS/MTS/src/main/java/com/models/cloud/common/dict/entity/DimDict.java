package com.models.cloud.common.dict.entity;

import java.util.Date;

/**
 * 数据字典Model类
 */
public class DimDict {
	
	private String dictTypeId;

	private Short dictId;
	
    private String dictTypeName;

    private String dispName;

    private String fullName;

    private Short dispOrder;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;
    
    public String getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(String dictTypeId) {
        this.dictTypeId = dictTypeId == null ? null : dictTypeId.trim();
    }

    public Short getDictId() {
        return dictId;
    }

    public void setDictId(Short dictId) {
        this.dictId = dictId;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName == null ? null : dictTypeName.trim();
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