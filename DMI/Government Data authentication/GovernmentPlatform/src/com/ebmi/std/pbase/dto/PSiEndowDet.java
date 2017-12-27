package com.ebmi.std.pbase.dto;

import java.util.Date;

/**
 * 参保人领取养老金明细
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:20:08
 *
 */
public class PSiEndowDet {

	private String p_mi_id;
	private String data_date;
	private Integer endow_money_type_id;
	private Double money_recv;
	private String begin_date;
	private Date upd_time;
	private Date crt_time;
	private String etl_batch;
	private Date etl_time;
	
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getData_date() {
		return data_date;
	}
	public void setData_date(String data_date) {
		this.data_date = data_date;
	}
	public Integer getEndow_money_type_id() {
		return endow_money_type_id;
	}
	public void setEndow_money_type_id(Integer endow_money_type_id) {
		this.endow_money_type_id = endow_money_type_id;
	}
	public Double getMoney_recv() {
		return money_recv;
	}
	public void setMoney_recv(Double money_recv) {
		this.money_recv = money_recv;
	}
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public Date getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(Date upd_time) {
		this.upd_time = upd_time;
	}
	public Date getCrt_time() {
		return crt_time;
	}
	public void setCrt_time(Date crt_time) {
		this.crt_time = crt_time;
	}
	public String getEtl_batch() {
		return etl_batch;
	}
	public void setEtl_batch(String etl_batch) {
		this.etl_batch = etl_batch;
	}
	public Date getEtl_time() {
		return etl_time;
	}
	public void setEtl_time(Date etl_time) {
		this.etl_time = etl_time;
	}
}
