package com.aghit.task.common.entity.klbase;

/**
 * 表达式
 */
public class KlStat {

	//表达式ID
	private long cond_stat_id;
	//知识ID
	private long k_id;
	//场景表达式
	private String scene_stat;
	//条件表达式
	private String cond_stat;
	//条件表达式关系 1与、2或、9其他复杂表达式
	private int cond_stat_rel;
	//提示信息
	private String warn_desc;
	//警示级别
	private long k_warn_lvl_id;
	
	public long getCond_stat_id() {
		return cond_stat_id;
	}
	public void setCond_stat_id(long cond_stat_id) {
		this.cond_stat_id = cond_stat_id;
	}
	public long getK_id() {
		return k_id;
	}
	public void setK_id(long k_id) {
		this.k_id = k_id;
	}
	public String getScene_stat() {
		return scene_stat;
	}
	public void setScene_stat(String scene_stat) {
		this.scene_stat = scene_stat;
	}
	public String getCond_stat() {
		return cond_stat;
	}
	public void setCond_stat(String cond_stat) {
		this.cond_stat = cond_stat;
	}
	public int getCond_stat_rel() {
		return cond_stat_rel;
	}
	public void setCond_stat_rel(int cond_stat_rel) {
		this.cond_stat_rel = cond_stat_rel;
	}
	public String getWarn_desc() {
		return warn_desc;
	}
	public void setWarn_desc(String warn_desc) {
		this.warn_desc = warn_desc;
	}
	public long getK_warn_lvl_id() {
		return k_warn_lvl_id;
	}
	public void setK_warn_lvl_id(long k_warn_lvl_id) {
		this.k_warn_lvl_id = k_warn_lvl_id;
	}
	
	
	
}
