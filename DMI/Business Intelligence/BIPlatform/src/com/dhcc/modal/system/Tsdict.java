package com.dhcc.modal.system;
public class Tsdict {
    private String id;//
    private String dtype;//数据字典类型
    private String dkey;//数据字典KEY
    private String dvalue;//数据字典VALUE
    private String remark;//备注信息，写入数据字典的用途

    /**  */
    public String getId() {
        return id;
    }
    /**  */
    public void setId(String id) {
        this.id = id;
    }
    /** 数据字典类型 */
    public String getDtype() {
        return dtype;
    }
    /** 数据字典类型 */
    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
    /** 数据字典KEY */
    public String getDkey() {
        return dkey;
    }
    /** 数据字典KEY */
    public void setDkey(String dkey) {
        this.dkey = dkey;
    }
    /** 数据字典VALUE */
    public String getDvalue() {
        return dvalue;
    }
    /** 数据字典VALUE */
    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }
    /** 备注信息，写入数据字典的用途 */
    public String getRemark() {
        return remark;
    }
    /** 备注信息，写入数据字典的用途 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}