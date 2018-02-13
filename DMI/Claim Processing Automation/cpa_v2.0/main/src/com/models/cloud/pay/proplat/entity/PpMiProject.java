package com.models.cloud.pay.proplat.entity;

import java.util.Date;
import java.util.List;

import com.models.cloud.pay.payuser.entity.UcBindAuthCol;

public class PpMiProject {
    private String micId;

    private String micName;

    private String regnId;

    private String projNameL1;

    private String projNameL2;

    private String projNameL3;

    private Integer dispOrder;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;
    
    private List<UcBindAuthCol> ucBindAuthColList;

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId == null ? null : micId.trim();
    }

    public String getMicName() {
        return micName;
    }

    public void setMicName(String micName) {
        this.micName = micName == null ? null : micName.trim();
    }

    public String getRegnId() {
        return regnId;
    }

    public void setRegnId(String regnId) {
        this.regnId = regnId == null ? null : regnId.trim();
    }

    public String getProjNameL1() {
        return projNameL1;
    }

    public void setProjNameL1(String projNameL1) {
        this.projNameL1 = projNameL1 == null ? null : projNameL1.trim();
    }

    public String getProjNameL2() {
        return projNameL2;
    }

    public void setProjNameL2(String projNameL2) {
        this.projNameL2 = projNameL2 == null ? null : projNameL2.trim();
    }

    public String getProjNameL3() {
        return projNameL3;
    }

    public void setProjNameL3(String projNameL3) {
        this.projNameL3 = projNameL3 == null ? null : projNameL3.trim();
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

	public List<UcBindAuthCol> getUcBindAuthColList() {
		return ucBindAuthColList;
	}

	public void setUcBindAuthColList(List<UcBindAuthCol> ucBindAuthColList) {
		this.ucBindAuthColList = ucBindAuthColList;
	}
}