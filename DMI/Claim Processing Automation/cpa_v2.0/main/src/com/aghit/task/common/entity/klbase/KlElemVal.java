package com.aghit.task.common.entity.klbase;

/**
 * 元素取值列表
 */
public class KlElemVal {

	//条件细项ID
	private long elem_det_id;
	//条件元素ID
	private long elem_id;
	//取值
	private String val;
	
	
	public long getElem_det_id() {
		return elem_det_id;
	}
	public void setElem_det_id(long elem_det_id) {
		this.elem_det_id = elem_det_id;
	}
	public long getElem_id() {
		return elem_id;
	}
	public void setElem_id(long elem_id) {
		this.elem_id = elem_id;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
}
