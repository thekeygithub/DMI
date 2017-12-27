package com.ebmi.std.pbase.cond;

/**
 * 参保人社保情况条件类
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:10:22
 *
 */
public class PSiStatCond {
	
	private String p_mi_id;
	private Integer si_type_id;
	private Integer coll_stat_id;
	
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public Integer getSi_type_id() {
		return si_type_id;
	}
	public void setSi_type_id(Integer si_type_id) {
		this.si_type_id = si_type_id;
	}
	public Integer getColl_stat_id() {
		return coll_stat_id;
	}
	public void setColl_stat_id(Integer coll_stat_id) {
		this.coll_stat_id = coll_stat_id;
	}

}
