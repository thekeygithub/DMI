package com.aghit.task.algorithm.domain;


public class Hospital extends Payment {
	
	//患者姓名
	private String p_name;
	// 结算单ID
	private long clm_id;
	//违规项目名称
	private String err_item_name;
	//患者id
	private String p_id;
	

	// 数据来源
	private long data_src_id;
	
	public long getClm_id() {
		return clm_id;
	}
	public void setClm_id(long clm_id) {
		this.clm_id = clm_id;
	}
	public long getData_src_id() {
		return data_src_id;
	}
	public void setData_src_id(long data_src_id) {
		this.data_src_id = data_src_id;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getErr_item_name() {
		return err_item_name;
	}
	public void setErr_item_name(String err_item_name) {
		this.err_item_name = err_item_name;
	}
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
}
