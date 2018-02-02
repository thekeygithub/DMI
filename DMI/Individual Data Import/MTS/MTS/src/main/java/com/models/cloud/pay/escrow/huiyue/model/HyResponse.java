package com.models.cloud.pay.escrow.huiyue.model;

/**
 * Created by yacheng.ji on 2017/3/9.
 */
public class HyResponse {

    public HyResponse(String result, String errmsg) {
        this.result = result;
        this.errmsg = errmsg;
    }

    public HyResponse() {}

    private String result;//验证结果

    private String cpserialnum;//请求方订单号

    private String sysserialnum;//系统验证流水号

    private String errmsg;//错误代码

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCpserialnum() {
        return cpserialnum;
    }

    public void setCpserialnum(String cpserialnum) {
        this.cpserialnum = cpserialnum;
    }

    public String getSysserialnum() {
        return sysserialnum;
    }

    public void setSysserialnum(String sysserialnum) {
        this.sysserialnum = sysserialnum;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
