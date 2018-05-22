package com.models.cloud.pay.seq.entity;

import java.util.Date;

public class SeqActRec {
    private Long id;

    private String stub;

    private Date updTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStub() {
        return stub;
    }

    public void setStub(String stub) {
        this.stub = stub == null ? null : stub.trim();
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }
}