package com.models.cloud.pay.payuser.entity;

import java.util.Date;

public class LogPayUserLogin {
    private Long logId;

    private Short loginTypeId;

    private Long payUserId;

    private String payUserCode;

    private Long actId;

    private String mobile;

    private Short loginResFlag;

    private Short loginFailTypeId;

    private String failDesc;

    private String userAlias;

    private String deviceCode;

    private String deviceLabel;

    private Date logTime;

    private String clientIp;

    private String clientVer;

    private String phoneModel;

    private String phoneScreen;

    private String phoneSysVer;

    private Short phoneOsTypeId;

    private String phoneOper;

    private String phoneNetType;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Short getLoginTypeId() {
        return loginTypeId;
    }

    public void setLoginTypeId(Short loginTypeId) {
        this.loginTypeId = loginTypeId;
    }

    public Long getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(Long payUserId) {
        this.payUserId = payUserId;
    }

    public String getPayUserCode() {
        return payUserCode;
    }

    public void setPayUserCode(String payUserCode) {
        this.payUserCode = payUserCode == null ? null : payUserCode.trim();
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Short getLoginResFlag() {
        return loginResFlag;
    }

    public void setLoginResFlag(Short loginResFlag) {
        this.loginResFlag = loginResFlag;
    }

    public Short getLoginFailTypeId() {
        return loginFailTypeId;
    }

    public void setLoginFailTypeId(Short loginFailTypeId) {
        this.loginFailTypeId = loginFailTypeId;
    }

    public String getFailDesc() {
        return failDesc;
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc == null ? null : failDesc.trim();
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias == null ? null : userAlias.trim();
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getDeviceLabel() {
        return deviceLabel;
    }

    public void setDeviceLabel(String deviceLabel) {
        this.deviceLabel = deviceLabel == null ? null : deviceLabel.trim();
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getClientVer() {
        return clientVer;
    }

    public void setClientVer(String clientVer) {
        this.clientVer = clientVer == null ? null : clientVer.trim();
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel == null ? null : phoneModel.trim();
    }

    public String getPhoneScreen() {
        return phoneScreen;
    }

    public void setPhoneScreen(String phoneScreen) {
        this.phoneScreen = phoneScreen == null ? null : phoneScreen.trim();
    }

    public String getPhoneSysVer() {
        return phoneSysVer;
    }

    public void setPhoneSysVer(String phoneSysVer) {
        this.phoneSysVer = phoneSysVer == null ? null : phoneSysVer.trim();
    }

    public Short getPhoneOsTypeId() {
        return phoneOsTypeId;
    }

    public void setPhoneOsTypeId(Short phoneOsTypeId) {
        this.phoneOsTypeId = phoneOsTypeId;
    }

    public String getPhoneOper() {
        return phoneOper;
    }

    public void setPhoneOper(String phoneOper) {
        this.phoneOper = phoneOper == null ? null : phoneOper.trim();
    }

    public String getPhoneNetType() {
        return phoneNetType;
    }

    public void setPhoneNetType(String phoneNetType) {
        this.phoneNetType = phoneNetType == null ? null : phoneNetType.trim();
    }
}