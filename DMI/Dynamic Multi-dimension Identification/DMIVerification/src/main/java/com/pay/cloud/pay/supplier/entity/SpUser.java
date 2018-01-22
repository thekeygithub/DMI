package com.pay.cloud.pay.supplier.entity;

import java.util.Date;

public class SpUser {
    private Long spUserId;

    private String spUserCode;

    private String pwd;

    private Short spUserStatId;

    private Short spUserTypeId;

    private String userName;

    private String email;

    private String mobile;

    private Long actId;

    private Date updTime;

    private Date crtTime;

    private Integer pwdFailCnt;

    private Date pwdFailTime;

    private String clientVerCode;

    public Long getSpUserId() {
        return spUserId;
    }

    public void setSpUserId(Long spUserId) {
        this.spUserId = spUserId;
    }

    public String getSpUserCode() {
        return spUserCode;
    }

    public void setSpUserCode(String spUserCode) {
        this.spUserCode = spUserCode == null ? null : spUserCode.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Short getSpUserStatId() {
        return spUserStatId;
    }

    public void setSpUserStatId(Short spUserStatId) {
        this.spUserStatId = spUserStatId;
    }

    public Short getSpUserTypeId() {
        return spUserTypeId;
    }

    public void setSpUserTypeId(Short spUserTypeId) {
        this.spUserTypeId = spUserTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Integer getPwdFailCnt() {
        return pwdFailCnt;
    }

    public void setPwdFailCnt(Integer pwdFailCnt) {
        this.pwdFailCnt = pwdFailCnt;
    }

    public Date getPwdFailTime() {
        return pwdFailTime;
    }

    public void setPwdFailTime(Date pwdFailTime) {
        this.pwdFailTime = pwdFailTime;
    }

    public String getClientVerCode() {
        return clientVerCode;
    }

    public void setClientVerCode(String clientVerCode) {
        this.clientVerCode = clientVerCode == null ? null : clientVerCode.trim();
    }
}