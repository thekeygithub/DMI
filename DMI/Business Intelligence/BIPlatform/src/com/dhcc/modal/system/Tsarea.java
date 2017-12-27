package com.dhcc.modal.system;
public class Tsarea {
    private String id;//
    private String areaname;//所属区域名称
    private String topcorpid;//
    private String remark;//备注信息

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /** 所属区域名称 */
    public String getAreaname() {
        return areaname;
    }
    /** 所属区域名称 */
    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
    /**  */
    public String getTopcorpid() {
        return topcorpid;
    }
    /**  */
    public void setTopcorpid(String topcorpid) {
        this.topcorpid = topcorpid;
    }
    /** 备注信息 */
    public String getRemark() {
        return remark;
    }
    /** 备注信息 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}