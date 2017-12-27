package com.dhcc.modal.system;
public class Tsportalres {
    private String id;//
    private String url;//
    private String pname;//模块名称
    private String remark;//备注

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /**  */
    public String getUrl() {
        return url;
    }
    /**  */
    public void setUrl(String url) {
        this.url = url;
    }
    /** 模块名称 */
    public String getPname() {
        return pname;
    }
    /** 模块名称 */
    public void setPname(String pname) {
        this.pname = pname;
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