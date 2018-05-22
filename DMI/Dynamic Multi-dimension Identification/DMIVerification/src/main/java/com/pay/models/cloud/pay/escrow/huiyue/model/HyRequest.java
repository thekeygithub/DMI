package com.models.cloud.pay.escrow.huiyue.model;

/**
 * Created by yacheng.ji on 2017/3/9.
 */
public class HyRequest {

    private String userName;

    private String idNumber;

    private String userPhotoBase64;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserPhotoBase64() {
        return userPhotoBase64;
    }

    public void setUserPhotoBase64(String userPhotoBase64) {
        this.userPhotoBase64 = userPhotoBase64;
    }
}
