package com.pay.cloud.pay.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易订单表
 */
public class TdOrder {

    private Long tdId;

    private BigDecimal tdOrder;

    private String micId;

    private Short tdTypeId;

    private Short tdBusiTypeId;

    private String busiDetTypeId;

    private Long chanActId;

    private String chanAppid;

    private Short tdOperChanTypeId;

    private Short fromActTypeId;

    private Long fromActId;

    private Long payUserId;

    private Short toActTypeId;

    private Long toActId;

    private Long dispSpActId;

    private String dispSpName;

    private String goodsDesc;

    private String fundId;

    private String fundTdCode;

    private String cbAddr;

    private BigDecimal servChrgRatio;

    private BigDecimal servChrgAmt;

    private BigDecimal payTot;

    private BigDecimal recvCur;

    private String origOrdCode;

    private String spSubSysCode;

    private Integer tdLimitSec;

    private Short tdStatId;

    private String tdDate;

    private Date tdStartTime;

    private Date tdEndTime;

    private String errCode;

    private Short confirmStatId;

    private Date confirmTime;

    private Date updTime;

    private Date crtTime;

    private String remark;

    private String longitude;

    private String latitude;

    private Short paySafeFlag;

    private Integer safeDist;

    private BigDecimal realDist;

    public Short getPaySafeFlag() {
        return paySafeFlag;
    }

    public void setPaySafeFlag(Short paySafeFlag) {
        this.paySafeFlag = paySafeFlag;
    }

    public Integer getSafeDist() {
        return safeDist;
    }

    public void setSafeDist(Integer safeDist) {
        this.safeDist = safeDist;
    }

    public BigDecimal getRealDist() {
        return realDist;
    }

    public void setRealDist(BigDecimal realDist) {
        this.realDist = realDist;
    }

    public String getBusiDetTypeId() {
        return busiDetTypeId;
    }

    public void setBusiDetTypeId(String busiDetTypeId) {
        this.busiDetTypeId = busiDetTypeId;
    }

    public BigDecimal getTdOrder() {
        return tdOrder;
    }

    public void setTdOrder(BigDecimal tdOrder) {
        this.tdOrder = tdOrder;
    }

    public Long getTdId() {
        return tdId;
    }

    public void setTdId(Long tdId) {
        this.tdId = tdId;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId == null ? null : micId.trim();
    }

    public Short getTdTypeId() {
        return tdTypeId;
    }

    public void setTdTypeId(Short tdTypeId) {
        this.tdTypeId = tdTypeId;
    }

    public Short getTdBusiTypeId() {
        return tdBusiTypeId;
    }

    public void setTdBusiTypeId(Short tdBusiTypeId) {
        this.tdBusiTypeId = tdBusiTypeId;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
    }

    public String getChanAppid() {
        return chanAppid;
    }

    public void setChanAppid(String chanAppid) {
        this.chanAppid = chanAppid;
    }

    public Short getTdOperChanTypeId() {
        return tdOperChanTypeId;
    }

    public void setTdOperChanTypeId(Short tdOperChanTypeId) {
        this.tdOperChanTypeId = tdOperChanTypeId;
    }

    public Short getFromActTypeId() {
        return fromActTypeId;
    }

    public void setFromActTypeId(Short fromActTypeId) {
        this.fromActTypeId = fromActTypeId;
    }

    public Long getFromActId() {
        return fromActId;
    }

    public void setFromActId(Long fromActId) {
        this.fromActId = fromActId;
    }

    public Long getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(Long payUserId) {
        this.payUserId = payUserId;
    }

    public Short getToActTypeId() {
        return toActTypeId;
    }

    public void setToActTypeId(Short toActTypeId) {
        this.toActTypeId = toActTypeId;
    }

    public Long getToActId() {
        return toActId;
    }

    public void setToActId(Long toActId) {
        this.toActId = toActId;
    }

    public Long getDispSpActId() {
        return dispSpActId;
    }

    public void setDispSpActId(Long dispSpActId) {
        this.dispSpActId = dispSpActId;
    }

    public String getDispSpName() {
        return dispSpName;
    }

    public void setDispSpName(String dispSpName) {
        this.dispSpName = dispSpName == null ? null : dispSpName.trim();
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId == null ? null : fundId.trim();
    }

    public String getFundTdCode() {
        return fundTdCode;
    }

    public void setFundTdCode(String fundTdCode) {
        this.fundTdCode = fundTdCode == null ? null : fundTdCode.trim();
    }

    public String getCbAddr() {
        return cbAddr;
    }

    public void setCbAddr(String cbAddr) {
        this.cbAddr = cbAddr == null ? null : cbAddr.trim();
    }

    public BigDecimal getServChrgRatio() {
        return servChrgRatio;
    }

    public void setServChrgRatio(BigDecimal servChrgRatio) {
        this.servChrgRatio = servChrgRatio;
    }

    public BigDecimal getServChrgAmt() {
        return servChrgAmt;
    }

    public void setServChrgAmt(BigDecimal servChrgAmt) {
        this.servChrgAmt = servChrgAmt;
    }

    public BigDecimal getPayTot() {
        return payTot;
    }

    public void setPayTot(BigDecimal payTot) {
        this.payTot = payTot;
    }

    public BigDecimal getRecvCur() {
        return recvCur;
    }

    public void setRecvCur(BigDecimal recvCur) {
        this.recvCur = recvCur;
    }

    public String getOrigOrdCode() {
        return origOrdCode;
    }

    public void setOrigOrdCode(String origOrdCode) {
        this.origOrdCode = origOrdCode == null ? null : origOrdCode.trim();
    }

    public String getSpSubSysCode() {
        return spSubSysCode;
    }

    public void setSpSubSysCode(String spSubSysCode) {
        this.spSubSysCode = spSubSysCode;
    }

    public Integer getTdLimitSec() {
        return tdLimitSec;
    }

    public void setTdLimitSec(Integer tdLimitSec) {
        this.tdLimitSec = tdLimitSec;
    }

    public Short getTdStatId() {
        return tdStatId;
    }

    public void setTdStatId(Short tdStatId) {
        this.tdStatId = tdStatId;
    }

    public String getTdDate() {
        return tdDate;
    }

    public void setTdDate(String tdDate) {
        this.tdDate = tdDate == null ? null : tdDate.trim();
    }

    public Date getTdStartTime() {
        return tdStartTime;
    }

    public void setTdStartTime(Date tdStartTime) {
        this.tdStartTime = tdStartTime;
    }

    public Date getTdEndTime() {
        return tdEndTime;
    }

    public void setTdEndTime(Date tdEndTime) {
        this.tdEndTime = tdEndTime;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public Short getConfirmStatId() {
        return confirmStatId;
    }

    public void setConfirmStatId(Short confirmStatId) {
        this.confirmStatId = confirmStatId;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}