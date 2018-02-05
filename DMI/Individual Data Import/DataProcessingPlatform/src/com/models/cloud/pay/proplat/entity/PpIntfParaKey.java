package com.models.cloud.pay.proplat.entity;

public class PpIntfParaKey {
    private String fundId;

    private String paraType;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
    }

    public String getParaType() {
        return paraType;
    }

    public void setParaType(String paraType) {
        this.paraType = paraType == null ? null : paraType.trim();
    }
}