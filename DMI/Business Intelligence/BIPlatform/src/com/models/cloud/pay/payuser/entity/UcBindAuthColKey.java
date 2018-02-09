package com.models.cloud.pay.payuser.entity;

public class UcBindAuthColKey {
    private String micId;

    private String colName;

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId == null ? null : micId.trim();
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName == null ? null : colName.trim();
    }
}