package com.dhcc.modal.system;
public class Tsstation {
    private String id;//岗位id
    private String stationname;//岗位名称
    private String topcorpid;//顶级公司id
    private String remark;//备注信息

    /** 岗位id */
    public String getId() {
        return id;
    }
    /** 岗位id */
    public void setId(String id) {
        this.id = id;
    }
    /** 岗位名称 */
    public String getStationname() {
        return stationname;
    }
    /** 岗位名称 */
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }
    /** 顶级公司id */
    public String getTopcorpid() {
        return topcorpid;
    }
    /** 顶级公司id */
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