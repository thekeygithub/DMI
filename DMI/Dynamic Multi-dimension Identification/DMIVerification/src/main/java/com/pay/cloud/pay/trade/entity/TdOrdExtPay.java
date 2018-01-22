package com.pay.cloud.pay.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易单扩展表-支付
 */
public class TdOrdExtPay {

    private Long tdId;

    private Short payTypeId;

    private Short tdBusiStatId;

    private Short tdSiStatId;

    private String pIdNo;

    private String pSiCardNo;

    private String siPCode;

    private BigDecimal paySelf;

    private BigDecimal paySi;

    private BigDecimal payPacct;

    private Short payActTypeId;

    private String fundTdCode;

    private String siTdCode;

    private String chanRetCode;

    private String chanRetDesc;

    private String siRetCode;

    private String siRetDesc;

    private Short tdRetFlag;

    private BigDecimal accRetAmt;

    private Date updTime;

    private Date crtTime;

    private String remark;//借用字段，存储医院就诊流水号

    private Long bankId;

    private String bankAccount;

    private String actName;

    private String payPropA;//借用字段，保存合作商户资金平台编码

    private String payPropB;//借用字段，保存医疗类别 1-医院门诊 2-药店购药

    private String payPropC;//借用字段，保存参保人员类别 1-职工 2-居民

    private String hospSeqCode;//支付平台生成就诊流水号

    public String getHospSeqCode() {
        return hospSeqCode;
    }

    public void setHospSeqCode(String hospSeqCode) {
        this.hospSeqCode = hospSeqCode;
    }

    public String getPayPropA() {
        return payPropA;
    }

    public void setPayPropA(String payPropA) {
        this.payPropA = payPropA;
    }

    public String getPayPropB() {
        return payPropB;
    }

    public void setPayPropB(String payPropB) {
        this.payPropB = payPropB;
    }

    public String getPayPropC() {
        return payPropC;
    }

    public void setPayPropC(String payPropC) {
        this.payPropC = payPropC;
    }

    public Short getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Short payTypeId) {
        this.payTypeId = payTypeId;
    }

    public Short getTdBusiStatId() {
        return tdBusiStatId;
    }

    public void setTdBusiStatId(Short tdBusiStatId) {
        this.tdBusiStatId = tdBusiStatId;
    }

    public Short getTdSiStatId() {
        return tdSiStatId;
    }

    public void setTdSiStatId(Short tdSiStatId) {
        this.tdSiStatId = tdSiStatId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public String getpIdNo() {
        return pIdNo;
    }

    public void setpIdNo(String pIdNo) {
        this.pIdNo = pIdNo == null ? null : pIdNo.trim();
    }

    public String getpSiCardNo() {
        return pSiCardNo;
    }

    public void setpSiCardNo(String pSiCardNo) {
        this.pSiCardNo = pSiCardNo == null ? null : pSiCardNo.trim();
    }

    public String getSiPCode() {
        return siPCode;
    }

    public void setSiPCode(String siPCode) {
        this.siPCode = siPCode == null ? null : siPCode.trim();
    }

    public BigDecimal getPaySelf() {
        return paySelf;
    }

    public void setPaySelf(BigDecimal paySelf) {
        this.paySelf = paySelf;
    }

    public BigDecimal getPaySi() {
        return paySi;
    }

    public void setPaySi(BigDecimal paySi) {
        this.paySi = paySi;
    }

    public BigDecimal getPayPacct() {
        return payPacct;
    }

    public void setPayPacct(BigDecimal payPacct) {
        this.payPacct = payPacct;
    }

    public String getFundTdCode() {
        return fundTdCode;
    }

    public void setFundTdCode(String fundTdCode) {
        this.fundTdCode = fundTdCode == null ? null : fundTdCode.trim();
    }

    public String getSiTdCode() {
        return siTdCode;
    }

    public void setSiTdCode(String siTdCode) {
        this.siTdCode = siTdCode == null ? null : siTdCode.trim();
    }

    public String getChanRetCode() {
        return chanRetCode;
    }

    public void setChanRetCode(String chanRetCode) {
        this.chanRetCode = chanRetCode == null ? null : chanRetCode.trim();
    }

    public String getChanRetDesc() {
        return chanRetDesc;
    }

    public void setChanRetDesc(String chanRetDesc) {
        this.chanRetDesc = chanRetDesc == null ? null : chanRetDesc.trim();
    }

    public String getSiRetCode() {
        return siRetCode;
    }

    public void setSiRetCode(String siRetCode) {
        this.siRetCode = siRetCode == null ? null : siRetCode.trim();
    }

    public String getSiRetDesc() {
        return siRetDesc;
    }

    public void setSiRetDesc(String siRetDesc) {
        this.siRetDesc = siRetDesc == null ? null : siRetDesc.trim();
    }

    public Short getTdRetFlag() {
        return tdRetFlag;
    }

    public void setTdRetFlag(Short tdRetFlag) {
        this.tdRetFlag = tdRetFlag;
    }

    public BigDecimal getAccRetAmt() {
        return accRetAmt;
    }

    public void setAccRetAmt(BigDecimal accRetAmt) {
        this.accRetAmt = accRetAmt;
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

    public Short getPayActTypeId() {
        return payActTypeId;
    }

    public void setPayActTypeId(Short payActTypeId) {
        this.payActTypeId = payActTypeId;
    }
}