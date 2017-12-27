package com.aghit.task.common.entity.klbase;

import java.util.Date;
import java.util.List;

/**
 * 知识定义表
 */
public class KlDef {
   
	//规则ID
	private long rule_id;
	//运行状态
	private String run_model;
	//知识ID
	private long k_id;
    //知识主题类型
	private long k_subj_type_id;
	//知识主体类型
	private int obj_elem_type_id;
	//知识来源ID
	private long k_src_id;
    //所属地域
	private long regn_id;
	//场景元素是否完整 1场景完整 0场景不完整
	private int scene_flag = 0;
	//是否启用场景1启用 0不启用
	private int scene_enable = 0;
	// 是否停用:1停用 0正常
	private int stop_sign = 0;
	// 标准化标识：-1自动拆分未确认 0未标准化 1完全标准化 2部分标准化
	private int std_flag;
    //最后一次从知识拆分转换到规则的时间
	private Date last_trans_date;
    //版本
	private String ag_ver;
	//创建时间
	private Date crt_date;
	//创建人
	private String crt_user;
	//更新时间
	private Date upd_date;
	//更新人
	private String upd_user;
	//审核项目ID
	private long[] objIds;
//	//知识主体对象
//	private List<KlObj> klObjs;
	//元素定义集合
	private List<KlElemDef> klElemDefs;
	//条件表达式集合
	private List<KlStat> klStats;
	
	//补充rule属性
	private String rule_name;
	//是否周期
	private int cycle_flg;
	//周期开始时间
	private int cycle_start;
	//周期结束时间
	private int cycle_end;
	//就诊类型
	private String medical_type;

	
	public long getRule_id() {
		return rule_id;
	}
	public void setRule_id(long rule_id) {
		this.rule_id = rule_id;
	}
	public String getRun_model() {
		return run_model;
	}
	public void setRun_model(String run_model) {
		this.run_model = run_model;
	}
	public int getObj_elem_type_id() {
		return obj_elem_type_id;
	}
	public void setObj_elem_type_id(int obj_elem_type_id) {
		this.obj_elem_type_id = obj_elem_type_id;
	}
	public long[] getObjIds() {
		return objIds;
	}
	public void setObjIds(long[] objIds) {
		this.objIds = objIds;
	}
	public long getK_id() {
		return k_id;
	}
	public void setK_id(long k_id) {
		this.k_id = k_id;
	}
	public long getK_subj_type_id() {
		return k_subj_type_id;
	}
	public void setK_subj_type_id(long k_subj_type_id) {
		this.k_subj_type_id = k_subj_type_id;
	}
	public long getK_src_id() {
		return k_src_id;
	}
	public void setK_src_id(long k_src_id) {
		this.k_src_id = k_src_id;
	}
	public long getRegn_id() {
		return regn_id;
	}
	public void setRegn_id(long regn_id) {
		this.regn_id = regn_id;
	}
	public int getScene_flag() {
		return scene_flag;
	}
	public void setScene_flag(int scene_flag) {
		this.scene_flag = scene_flag;
	}
	public int getScene_enable() {
		return scene_enable;
	}
	public void setScene_enable(int scene_enable) {
		this.scene_enable = scene_enable;
	}
	public int getStop_sign() {
		return stop_sign;
	}
	public void setStop_sign(int stop_sign) {
		this.stop_sign = stop_sign;
	}
	public int getStd_flag() {
		return std_flag;
	}
	public void setStd_flag(int std_flag) {
		this.std_flag = std_flag;
	}
	public Date getLast_trans_date() {
		return last_trans_date;
	}
	public void setLast_trans_date(Date last_trans_date) {
		this.last_trans_date = last_trans_date;
	}
	public String getAg_ver() {
		return ag_ver;
	}
	public void setAg_ver(String ag_ver) {
		this.ag_ver = ag_ver;
	}
	public Date getCrt_date() {
		return crt_date;
	}
	public void setCrt_date(Date crt_date) {
		this.crt_date = crt_date;
	}
	public String getCrt_user() {
		return crt_user;
	}
	public void setCrt_user(String crt_user) {
		this.crt_user = crt_user;
	}
	public Date getUpd_date() {
		return upd_date;
	}
	public void setUpd_date(Date upd_date) {
		this.upd_date = upd_date;
	}
	public String getUpd_user() {
		return upd_user;
	}
	public void setUpd_user(String upd_user) {
		this.upd_user = upd_user;
	}
//	public List<KlObj> getKlObjs() {
//		return klObjs;
//	}
//	public void setKlObjs(List<KlObj> klObjs) {
//		this.klObjs = klObjs;
//	}
	public List<KlElemDef> getKlElemDefs() {
		return klElemDefs;
	}
	public void setKlElemDefs(List<KlElemDef> klElemDefs) {
		this.klElemDefs = klElemDefs;
	}
	public List<KlStat> getKlStats() {
		return klStats;
	}
	public void setKlStats(List<KlStat> klStats) {
		this.klStats = klStats;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public int getCycle_flg() {
		return cycle_flg;
	}
	public void setCycle_flg(int cycle_flg) {
		this.cycle_flg = cycle_flg;
	}
	public int getCycle_start() {
		return cycle_start;
	}
	public void setCycle_start(int cycle_start) {
		this.cycle_start = cycle_start;
	}
	public int getCycle_end() {
		return cycle_end;
	}
	public void setCycle_end(int cycle_end) {
		this.cycle_end = cycle_end;
	}
	public String getMedical_type() {
		return medical_type;
	}
	public void setMedical_type(String medical_type) {
		this.medical_type = medical_type;
	}

}
