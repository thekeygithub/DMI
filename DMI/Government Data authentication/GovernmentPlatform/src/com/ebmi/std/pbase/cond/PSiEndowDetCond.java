package com.ebmi.std.pbase.cond;

/**
 * 参保人领取养老金明细 条件类
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:20:08
 *
 */
public class PSiEndowDetCond {

	private String p_mi_id;
	private String data_date;
	private String si_cat_id;
	
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
	public String getSi_cat_id() {
		return si_cat_id;
	}
	public void setSi_cat_id(String si_cat_id) {
		this.si_cat_id = si_cat_id;
	}
	
}
