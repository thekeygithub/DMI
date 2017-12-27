package com.ebmi.std.pbase.cond;

/**
 * 参保人缴费明细 条件类
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:13:36
 *
 */
public class PCollDetCond extends BaseCond{
	
	private String coll_det_id;
	private String p_mi_id;
	private String coll_mon;
	private String si_cat_id;
	private String si_type_id;
	
	public String getColl_det_id() {
		return coll_det_id;
	}
	public void setColl_det_id(String coll_det_id) {
		this.coll_det_id = coll_det_id;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getColl_mon() {
		return coll_mon;
	}
	public void setColl_mon(String coll_mon) {
		this.coll_mon = coll_mon;
	}
	public String getSi_cat_id() {
		return si_cat_id;
	}
	public void setSi_cat_id(String si_cat_id) {
		this.si_cat_id = si_cat_id;
	}
	public String getSi_type_id() {
		return si_type_id;
	}
	public void setSi_type_id(String si_type_id) {
		this.si_type_id = si_type_id;
	}
}
