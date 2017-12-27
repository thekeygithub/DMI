package com.aghit.task.common.entity.klbase;

/**
 * 元素类型表
 */
public class KlElemType {

	//元素类型ID
	private long elem_type_id;
    //元素类型名称
	private String elem_type_name;
    //取值类型：0常量 1字典表 2其他实体表
	private long val_type;
	//数据类型：0数字 1字符 2日期
	private long data_type;
	//是否有单位：1有单位 0无单位
	private int unit_flag;
	//关联表名
	private String rel_tab;
	//取值字段
	private String val_col;
	//显示字段
	private String disp_col;
	//过滤条件
	private String filt_cond;
	//备注
	private String remark;
	public long getElem_type_id() {
		return elem_type_id;
	}
	public void setElem_type_id(long elem_type_id) {
		this.elem_type_id = elem_type_id;
	}
	public String getElem_type_name() {
		return elem_type_name;
	}
	public void setElem_type_name(String elem_type_name) {
		this.elem_type_name = elem_type_name;
	}
	public long getVal_type() {
		return val_type;
	}
	public void setVal_type(long val_type) {
		this.val_type = val_type;
	}
	public long getData_type() {
		return data_type;
	}
	public void setData_type(long data_type) {
		this.data_type = data_type;
	}
	public int getUnit_flag() {
		return unit_flag;
	}
	public void setUnit_flag(int unit_flag) {
		this.unit_flag = unit_flag;
	}
	public String getRel_tab() {
		return rel_tab;
	}
	public void setRel_tab(String rel_tab) {
		this.rel_tab = rel_tab;
	}
	public String getVal_col() {
		return val_col;
	}
	public void setVal_col(String val_col) {
		this.val_col = val_col;
	}
	public String getDisp_col() {
		return disp_col;
	}
	public void setDisp_col(String disp_col) {
		this.disp_col = disp_col;
	}
	public String getFilt_cond() {
		return filt_cond;
	}
	public void setFilt_cond(String filt_cond) {
		this.filt_cond = filt_cond;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

    
}
