package com.aghit.task.algorithm.domain;

/**
 * 违规项目类
 */
public class BreakDetailItem {

	private long rec_id; // 违规记录id
	private long err_item_id; //违规元素ID
	private long pre_id;   //项目Id
	
	public long getRec_id() {
		return rec_id;
	}
	public void setRec_id(long rec_id) {
		this.rec_id = rec_id;
	}
	public long getErr_item_id() {
		return err_item_id;
	}
	public void setErr_item_id(long err_item_id) {
		this.err_item_id = err_item_id;
	}
	public long getPre_id() {
		return pre_id;
	}
	public void setPre_id(long pre_id) {
		this.pre_id = pre_id;
	}
	
}
