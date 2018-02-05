package com.models.cloud.pay.supplier.entity;

import java.util.Date;

public class ActSp {

    private Long actId;

    private String micId;

    private Long corpId;

    private Long actCode;

    private Short chanActFlag;

    private Long chanActId;

    private Short fundModelId;

    private String fundId;

    private String spFundCode;

    private String chanAppid;

    private String entName;

    private Short entTypeId;

    private String entPrivCode;

    private String miEntCode;

    private Short miPayFlag;

    private String entAddr;

    private String longitude;

    private String latitude;

    private Integer safeDist;

    private Integer zipcode;

    private String linkMan;

    private String phone1;

    private String phone2;

    private String phone3;

    private String entEmail;

    private String entScope;

    private String entStartDate;

    private String entEndDate;

    private String regCode;

    private String regImageId;

    private String orgCode;

    private String orgImageId;

    private String imageId;

    private Short actStatId;

    private Short validFlag;

    private Date updTime;

    private Date crtTime;

    private String remark;
    
    private Short spOpenChkFlag;
    
    private String saltVal;

    public Short getMiPayFlag() {
        return miPayFlag;
    }

    public void setMiPayFlag(Short miPayFlag) {
        this.miPayFlag = miPayFlag;
    }

    public Short getFundModelId() {
        return fundModelId;
    }

    public void setFundModelId(Short fundModelId) {
        this.fundModelId = fundModelId;
    }

    public Long getCorpId() {
        return corpId;
    }

    public void setCorpId(Long corpId) {
        this.corpId = corpId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getSpFundCode() {
        return spFundCode;
    }

    public void setSpFundCode(String spFundCode) {
        this.spFundCode = spFundCode;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId == null ? null : micId.trim();
    }

    public Long getActCode() {
        return actCode;
    }

    public void setActCode(Long actCode) {
        this.actCode = actCode;
    }

    public Short getChanActFlag() {
        return chanActFlag;
    }

    public void setChanActFlag(Short chanActFlag) {
        this.chanActFlag = chanActFlag;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
    }

    public String getChanAppid() {
        return chanAppid;
    }

    public void setChanAppid(String chanAppid) {
        this.chanAppid = chanAppid == null ? null : chanAppid.trim();
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName == null ? null : entName.trim();
    }

    public Short getEntTypeId() {
        return entTypeId;
    }

    public void setEntTypeId(Short entTypeId) {
        this.entTypeId = entTypeId;
    }

    public String getEntPrivCode() {
        return entPrivCode;
    }

    public void setEntPrivCode(String entPrivCode) {
        this.entPrivCode = entPrivCode == null ? null : entPrivCode.trim();
    }

    public String getMiEntCode() {
        return miEntCode;
    }

    public void setMiEntCode(String miEntCode) {
        this.miEntCode = miEntCode == null ? null : miEntCode.trim();
    }

    public String getEntAddr() {
        return entAddr;
    }

    public void setEntAddr(String entAddr) {
        this.entAddr = entAddr == null ? null : entAddr.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public Integer getSafeDist() {
        return safeDist;
    }

    public void setSafeDist(Integer safeDist) {
        this.safeDist = safeDist;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan == null ? null : linkMan.trim();
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1 == null ? null : phone1.trim();
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2 == null ? null : phone2.trim();
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3 == null ? null : phone3.trim();
    }

    public String getEntEmail() {
        return entEmail;
    }

    public void setEntEmail(String entEmail) {
        this.entEmail = entEmail == null ? null : entEmail.trim();
    }

    public String getEntScope() {
        return entScope;
    }

    public void setEntScope(String entScope) {
        this.entScope = entScope == null ? null : entScope.trim();
    }

    public String getEntStartDate() {
        return entStartDate;
    }

    public void setEntStartDate(String entStartDate) {
        this.entStartDate = entStartDate == null ? null : entStartDate.trim();
    }

    public String getEntEndDate() {
        return entEndDate;
    }

    public void setEntEndDate(String entEndDate) {
        this.entEndDate = entEndDate == null ? null : entEndDate.trim();
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode == null ? null : regCode.trim();
    }

    public String getRegImageId() {
        return regImageId;
    }

    public void setRegImageId(String regImageId) {
        this.regImageId = regImageId == null ? null : regImageId.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getOrgImageId() {
        return orgImageId;
    }

    public void setOrgImageId(String orgImageId) {
        this.orgImageId = orgImageId == null ? null : orgImageId.trim();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public Short getActStatId() {
        return actStatId;
    }

    public void setActStatId(Short actStatId) {
        this.actStatId = actStatId;
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

	public Short getSpOpenChkFlag() {
		return spOpenChkFlag;
	}

	public void setSpOpenChkFlag(Short spOpenChkFlag) {
		this.spOpenChkFlag = spOpenChkFlag;
	}

	public String getSaltVal() {
		return saltVal;
	}

	public void setSaltVal(String saltVal) {
		this.saltVal = saltVal;
	}
	
}