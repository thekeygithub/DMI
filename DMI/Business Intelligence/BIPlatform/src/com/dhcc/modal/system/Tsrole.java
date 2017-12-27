package com.dhcc.modal.system;
public class Tsrole {
    private String id;//角色id
    private String rolename;//角色名称
    private String topcorpid;//顶级公司id
    private String remark;//备注信息
    private Integer sysid;//所属系统id，不同的系统使用不同的角色，以此进行识别，每个系统有固定的id

    /** 角色id */
    public String getId() {
        return id;
    }
    /** 角色id */
    public void setId(String id) {
        this.id = id;
    }
    /** 角色名称 */
    public String getRolename() {
        return rolename;
    }
    /** 角色名称 */
    public void setRolename(String rolename) {
        this.rolename = rolename;
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
    /** 所属系统id，不同的系统使用不同的角色，以此进行识别，每个系统有固定的id */
    public Integer getSysid() {
        return sysid;
    }
    /** 所属系统id，不同的系统使用不同的角色，以此进行识别，每个系统有固定的id */
    public void setSysid(Integer sysid) {
        this.sysid = sysid;
    }
}