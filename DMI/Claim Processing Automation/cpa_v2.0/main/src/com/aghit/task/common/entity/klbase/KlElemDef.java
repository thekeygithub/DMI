package com.aghit.task.common.entity.klbase;

import java.util.List;

/**
 * 元素定义表
 */
public class KlElemDef {

	//元素ID
	private long elem_id;
	//知识ID
	private long k_id;
	//元素类型ID
	private int elem_type_id;
	//分类ID
	private long category_id;
	//符号
	private String elem_symb;
	//是否用于场景 1是 0否
	private int scene_flag;
	//是否用于条件 1是 0否
	private int cond_flag;
	//元素取值数组：条件项
	private String[] elemVals;
//	//元素是否为true or false
//	private boolean elem_state;
	//日期区间 -1-9 后一天到前9天
	private String intv_range;
	//日期单位 日，时，分
	private String intv_unit;
	//是否在条件项目中排除审核项目
	private int ex_obj_flag;
	//审核范围 
	private int range_flag;
	//为空时是否审核标识
	private int null_flag;
	
	private int elem_Data_type;

	public long getElem_id() {
		return elem_id;
	}

	public void setElem_id(long elem_id) {
		this.elem_id = elem_id;
	}

	public long getK_id() {
		return k_id;
	}

	public void setK_id(long k_id) {
		this.k_id = k_id;
	}

	public int getElem_type_id() {
		return elem_type_id;
	}

	public void setElem_type_id(int elem_type_id) {
		this.elem_type_id = elem_type_id;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public String getElem_symb() {
		return elem_symb;
	}

	public void setElem_symb(String elem_symb) {
		this.elem_symb = elem_symb;
	}

	public int getScene_flag() {
		return scene_flag;
	}

	public void setScene_flag(int scene_flag) {
		this.scene_flag = scene_flag;
	}

	public int getCond_flag() {
		return cond_flag;
	}

	public void setCond_flag(int cond_flag) {
		this.cond_flag = cond_flag;
	}

//	public List<KlElemVal> getKlElemVals() {
//		return klElemVals;
//	}
//
//	public void setKlElemVals(List<KlElemVal> klElemVals) {
//		this.klElemVals = klElemVals;
//	}
	public String[] getElemVals() {
		return elemVals;
	}
	public void setElemVals(String[] elemVals) {
		this.elemVals = elemVals;
	}
//	public boolean isElem_state() {
//		return elem_state;
//	}
//	public void setElem_state(boolean elem_state) {
//		this.elem_state = elem_state;
//	}

	public String getIntv_range() {
		return intv_range;
	}
	public void setIntv_range(String intv_range) {
		this.intv_range = intv_range;
	}
	public String getIntv_unit() {
		return intv_unit;
	}
	public void setIntv_unit(String intv_unit) {
		this.intv_unit = intv_unit;
	}

	public int getEx_obj_flag() {
		return ex_obj_flag;
	}
	public void setEx_obj_flag(int ex_obj_flag) {
		this.ex_obj_flag = ex_obj_flag;
	}

	public int getRange_flag() {
		return range_flag;
	}

	public void setRange_flag(int range_flag) {
		this.range_flag = range_flag;
	}

	public int getNull_flag() {
		return null_flag;
	}

	public void setNull_flag(int null_flag) {
		this.null_flag = null_flag;
	}

	public int getElem_Data_type() {
		return elem_Data_type;
	}

	public void setElem_Data_type(int elem_Data_type) {
		this.elem_Data_type = elem_Data_type;
	}
	
}
