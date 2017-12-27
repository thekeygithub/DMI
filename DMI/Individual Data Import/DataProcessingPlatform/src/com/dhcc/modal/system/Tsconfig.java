package com.dhcc.modal.system;
public class Tsconfig {
    private String id;//
    private String dtype;//配置项类型
    private String dkey;//配置项KEY
    private String dvalue;//配置项VALUE
    private String remark;//备注，配置项的描述

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /** 配置项类型 */
    public String getDtype() {
        return dtype;
    }
    /** 配置项类型 */
    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
    /** 配置项KEY */
    public String getDkey() {
        return dkey;
    }
    /** 配置项KEY */
    public void setDkey(String dkey) {
        this.dkey = dkey;
    }
    /** 配置项VALUE */
    public String getDvalue() {
        return dvalue;
    }
    /** 配置项VALUE */
    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }
    /** 备注，配置项的描述 */
    public String getRemark() {
        return remark;
    }
    /** 备注，配置项的描述 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}