package com.dhcc.ts.ipdData.model;
/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月21日 下午3:15:49 
* @类说明：就诊数据通用model
*/
public class CurrentModel {

	private String id;//数据主键（门诊号、住院号...）
	private String rdate;//日期
	private String mtype;//类型
	private String dtype;//详细类型
	private String corp;//机构名称
	private String corp_id;//机构id
	private String unit;//科室
	private String detail;//详细
	private String dcname;//医生
	private String mts_zd;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getCorp() {
		return corp;
	}
	public void setCorp(String corp) {
		this.corp = corp;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getCorp_id() {
		return corp_id;
	}
	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}
	public String getDcname() {
		return dcname;
	}
	public void setDcname(String dcname) {
		this.dcname = dcname;
	}
	public String getMts_zd() {
		return mts_zd;
	}
	public void setMts_zd(String mts_zd) {
		this.mts_zd = mts_zd;
	}
	
}
