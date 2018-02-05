package com.models.cloud.pay.payuser.entity;

import java.util.Date;

public class UcUserAddr {
	/***
	 * 地址ID
	 */
    private String ucAddrId;
    /***
     * 账户ID
     */
    private Long actId;
    /***
     * 省ID
     */
    private String provId;
    /***
     * 省名称
     */
    private String provName;
    /***
     * 市ID
     */
    private String cityId;
    /***
     * 市名称
     */
    private String cityName;
    /***
     * 区ID
     */
    private String distId;
    /***
     *区名称
     */
    private String distName;
    /***
     * 社区ID
     */
    private String commId;
    /***
     * 社区名称
     */
    private String commName;
    /***
     * 收货地址
     */
    private String recvAddrDet;
    /***
     * 经度
     */
    private String longitude;
    /***
     * 纬度
     */
    private String latitude;
    /***
     * 联系电话
     */
    private String phone;
    /***
     * 收货人
     */
    private String recvMan;
    /***
     * 收货地址状态
     */
    private Short ucAddrStatId;
    /***
     * 性别
     */
    private Short pGendId;
    /***
     * 更新用渠道账户ID
     */
    private Long chanActId;
    /***
     *更新时间
     */
    private Date updTime;
    /***
     * 创建时间
     */
    private Date crtTime;
    public String getUcAddrId() {
        return ucAddrId;
    }

    public void setUcAddrId(String ucAddrId) {
        this.ucAddrId = ucAddrId == null ? null : ucAddrId.trim();
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId == null ? null : provId.trim();
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName == null ? null : provName.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId == null ? null : distId.trim();
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName == null ? null : distName.trim();
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId == null ? null : commId.trim();
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName == null ? null : commName.trim();
    }

    public String getRecvAddrDet() {
        return recvAddrDet;
    }

    public void setRecvAddrDet(String recvAddrDet) {
        this.recvAddrDet = recvAddrDet == null ? null : recvAddrDet.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getRecvMan() {
        return recvMan;
    }

    public void setRecvMan(String recvMan) {
        this.recvMan = recvMan == null ? null : recvMan.trim();
    }

    public Short getUcAddrStatId() {
        return ucAddrStatId;
    }

    public void setUcAddrStatId(Short ucAddrStatId) {
        this.ucAddrStatId = ucAddrStatId;
    }

    public Short getpGendId() {
        return pGendId;
    }

    public void setpGendId(Short pGendId) {
        this.pGendId = pGendId;
    }

    public Long getChanActId() {
        return chanActId;
    }

    public void setChanActId(Long chanActId) {
        this.chanActId = chanActId;
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
}