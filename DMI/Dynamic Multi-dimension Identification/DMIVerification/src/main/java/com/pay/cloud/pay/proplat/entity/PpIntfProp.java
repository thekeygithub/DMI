package com.pay.cloud.pay.proplat.entity;

import java.util.Date;

/**
 * 资金平台交互属性实体类
 */
public class PpIntfProp {
	
    private String fundId;

    private String ppSpCode;

    private String ppPubKey;

    private String ebaoPubKey;

    private String ebaoPriKey;
    
    private String ebaoDesKey;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
    }

    public String getPpSpCode() {
        return ppSpCode;
    }

    public void setPpSpCode(String ppSpCode) {
        this.ppSpCode = ppSpCode == null ? null : ppSpCode.trim();
    }

    public String getPpPubKey() {
        return ppPubKey;
    }

    public void setPpPubKey(String ppPubKey) {
        this.ppPubKey = ppPubKey == null ? null : ppPubKey.trim();
    }

    public String getEbaoPubKey() {
        return ebaoPubKey;
    }

    public void setEbaoPubKey(String ebaoPubKey) {
        this.ebaoPubKey = ebaoPubKey == null ? null : ebaoPubKey.trim();
    }

    public String getEbaoPriKey() {
        return ebaoPriKey;
    }

    public void setEbaoPriKey(String ebaoPriKey) {
        this.ebaoPriKey = ebaoPriKey == null ? null : ebaoPriKey.trim();
    }
    
    public String getEbaoDesKey() {
		return ebaoDesKey;
	}

	public void setEbaoDesKey(String ebaoDesKey) {
		this.ebaoDesKey = ebaoDesKey == null ? null : ebaoDesKey.trim();
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