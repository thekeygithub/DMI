package com.dhcc.modal.system;
public class Tsbtnresource {
    private String id;//
    private String menuid;//按钮资源id
    private String resourceid;//
    private String resourcename;//按钮资源名称
    private String pagename;//页面URL
    private String pageurl;//页面url
    private String btnevent;//按钮事件
    private Integer btnsort;//排序
    private String remark;//备注

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /** 按钮资源id */
    public String getMenuid() {
        return menuid;
    }
    /** 按钮资源id */
    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
    /**  */
    public String getResourceid() {
        return resourceid;
    }
    /**  */
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
    /** 按钮资源名称 */
    public String getResourcename() {
        return resourcename;
    }
    /** 按钮资源名称 */
    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }
    /** 页面URL */
    public String getPagename() {
        return pagename;
    }
    /** 页面URL */
    public void setPagename(String pagename) {
        this.pagename = pagename;
    }
    /** 页面url */
    public String getPageurl() {
        return pageurl;
    }
    /** 页面url */
    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }
    /** 按钮事件 */
    public String getBtnevent() {
        return btnevent;
    }
    /** 按钮事件 */
    public void setBtnevent(String btnevent) {
        this.btnevent = btnevent;
    }
    /** 排序 */
    public Integer getBtnsort() {
        return btnsort;
    }
    /** 排序 */
    public void setBtnsort(Integer btnsort) {
        this.btnsort = btnsort;
    }
    /** 备注 */
    public String getRemark() {
        return remark;
    }
    /** 备注 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}