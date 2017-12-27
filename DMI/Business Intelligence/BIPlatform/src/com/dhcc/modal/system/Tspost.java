package com.dhcc.modal.system;
public class Tspost {
    private String id;//职务id
    private String postname;//职务名称
    private String ordernum;//顺序号,从1开始(可标记职务的大小)
    private String topcorpid;//顶级公司id
    private String remark;//备注信息

    /** 职务id */
    public String getId() {
        return id;
    }
    /** 职务id */
    public void setId(String id) {
        this.id = id;
    }
    /** 职务名称 */
    public String getPostname() {
        return postname;
    }
    /** 职务名称 */
    public void setPostname(String postname) {
        this.postname = postname;
    }
    /** 顺序号,从1开始(可标记职务的大小) */
    public String getOrdernum() {
		return ordernum;
	}
    /** 顺序号,从1开始(可标记职务的大小) */
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
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