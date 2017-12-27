package com.dhcc.modal.system;
public class Tscorp {
    private String id;//单位id
    private String corpname;//单位名称
    private String epid;//单位编码/代号
    private String pid;//上级单位ID
    private Integer ordernum;//顺序号，同级单位顺序号，从1开始
    private Integer type;//0代表集团，1代表公司，2代表部门
    private String topcorpid;//顶级公司id
    private String remark;//备注信息

    /** 单位id */
    public String getId() {
        return id;
    }
    /** 单位id */
    public void setId(String id) {
        this.id = id;
    }
    /** 单位名称 */
    public String getCorpname() {
        return corpname;
    }
    /** 单位名称 */
    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }
    /** 单位编码/代号 */
    public String getEpid() {
        return epid;
    }
    /** 单位编码/代号 */
    public void setEpid(String epid) {
        this.epid = epid;
    }
    /** 上级单位ID */
    public String getPid() {
        return pid;
    }
    /** 上级单位ID */
    public void setPid(String pid) {
        this.pid = pid;
    }
    /** 顺序号，同级单位顺序号，从1开始 */
    public Integer getOrdernum() {
        return ordernum;
    }
    /** 顺序号，同级单位顺序号，从1开始 */
    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }
    /** 0代表集团，1代表公司，2代表部门 */
    public Integer getType() {
        return type;
    }
    /** 0代表集团，1代表公司，2代表部门 */
    public void setType(Integer type) {
        this.type = type;
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