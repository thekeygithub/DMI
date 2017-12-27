package com.dhcc.modal.system;
public class Tslog {
    private String id;//
    private String userid;//操作用户id
    private String logdate;//操作时间
    private String ipaddress;//操作时的ip地址
    private String otherMessage;//操作时其他信息(什么浏览器什么操作系统)
    private String action;//执行动作
    private String message;//动作明细

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /** 操作用户id */
    public String getUserid() {
        return userid;
    }
    /** 操作用户id */
    public void setUserid(String userid) {
        this.userid = userid;
    }
    /** 操作时间 */
    public String getLogdate() {
        return logdate;
    }
    /** 操作时间 */
    public void setLogdate(String logdate) {
        this.logdate = logdate;
    }
    /** 操作时的ip地址 */
    public String getIpaddress() {
        return ipaddress;
    }
    /** 操作时的ip地址 */
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    /** 操作时其他信息(什么浏览器什么操作系统) */
    public String getOtherMessage() {
        return otherMessage;
    }
    /** 操作时其他信息(什么浏览器什么操作系统) */
    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }
    /** 执行动作 */
    public String getAction() {
        return action;
    }
    /** 执行动作 */
    public void setAction(String action) {
        this.action = action;
    }
    /** 动作明细 */
    public String getMessage() {
        return message;
    }
    /** 动作明细 */
    public void setMessage(String message) {
        this.message = message;
    }
}