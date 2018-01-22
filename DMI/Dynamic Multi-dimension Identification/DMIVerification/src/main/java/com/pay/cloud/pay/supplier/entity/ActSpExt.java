package com.pay.cloud.pay.supplier.entity;

import java.util.Date;

public class ActSpExt {
    private Long actId;

    private String serverKeyPri;

    private String serverKeyPub;

    private String spKeyPri;

    private String spKeyPub;

    private Date updTime;

    private Date crtTime;

    private String remark;

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getServerKeyPri() {
		return serverKeyPri;
	}

	public void setServerKeyPri(String serverKeyPri) {
		this.serverKeyPri = serverKeyPri;
	}

	public String getServerKeyPub() {
		return serverKeyPub;
	}

	public void setServerKeyPub(String serverKeyPub) {
		this.serverKeyPub = serverKeyPub;
	}

	public String getSpKeyPri() {
		return spKeyPri;
	}

	public void setSpKeyPri(String spKeyPri) {
		this.spKeyPri = spKeyPri;
	}

	public String getSpKeyPub() {
		return spKeyPub;
	}

	public void setSpKeyPub(String spKeyPub) {
		this.spKeyPub = spKeyPub;
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