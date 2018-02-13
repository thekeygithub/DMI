package com.models.cloud.common.cache.account.entity;


/**
 * 商户账户信息Model类
 */
public class ActSpInfo {
	
	//商户账户ID
    private Long actId;
    //渠道账户APPID
    private String chanAppId;
    //是否有效
    private int validFlag;
    //平台私钥
    private String serverKeyPri;
    //平台公钥
    private String serverKeyPub;
    //商户私钥
    private String spKeyPri;
    //商户公钥
    private String spKeyPub;

    public Long getActId() {
        return actId;
    }
    
    public void setActId(Long actId) {
        this.actId = actId;
    }
    
    public String getChanAppId() {
		return chanAppId;
	}
    
	public void setChanAppId(String chanAppId) {
		this.chanAppId = chanAppId == null ? null : chanAppId.trim();
	}

	public int getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(int validFlag) {
		this.validFlag = validFlag;
	}

	public String getServerKeyPri() {
        return serverKeyPri;
    }

    public void setServerKeyPri(String serverKeyPri) {
        this.serverKeyPri = serverKeyPri == null ? null : serverKeyPri.trim();
    }

    public String getServerKeyPub() {
        return serverKeyPub;
    }

    public void setServerKeyPub(String serverKeyPub) {
        this.serverKeyPub = serverKeyPub == null ? null : serverKeyPub.trim();
    }

    public String getSpKeyPri() {
        return spKeyPri;
    }

    public void setSpKeyPri(String spKeyPri) {
        this.spKeyPri = spKeyPri == null ? null : spKeyPri.trim();
    }

    public String getSpKeyPub() {
        return spKeyPub;
    }

    public void setSpKeyPub(String spKeyPub) {
        this.spKeyPub = spKeyPub == null ? null : spKeyPub.trim();
    }

}