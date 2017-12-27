package com.aghit.task.runtime;

import com.aghit.task.manager.TreeMgr;
import com.aghit.task.util.Constant;

/**
 * 运行时知识
 */
public class RunTimeKLData implements IKLData {

	@Override
	public String getCsumCateSql() {
		// 构造SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("select t.csum_name_id, t.csum_cat_id ");
		sql.append("  from rt_cpa_csum_tree_cfda t ");
	    sql.append(" where csum_name_id is not null ");
	    return sql.toString();
	}

	@Override
	public String getAtcWestSql() {
		// 构造SQL语句-西药分类
		StringBuilder sql = new StringBuilder();
		sql.append("select 'X_' || t.drug_name_id || '_' || t.form_id  as key, t.atc_id ");
		sql.append("  from rt_cpa_drug_atc t ");
		sql.append(" where t.drug_name_id is not null ");
		sql.append("   and t.form_id is not null ");
		sql.append("   and t.term_tree_id = ");
		sql.append(Constant.DRUG_TYPE_ATC_WEST);
		return sql.toString();
	}

	@Override
	public String getAtcChinaSql() {
		// 构造SQL语句-中药分类
		StringBuilder sql = new StringBuilder();
		sql.append("select 'Z_' || t.drug_name_id as key, t.atc_id ");
		sql.append("  from rt_cpa_drug_atc t ");
		sql.append(" where t.drug_name_id is not null ");
		sql.append("   and t.term_tree_id = ");
		sql.append(Constant.DRUG_TYPE_ATC_CHINA);
		return sql.toString();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAtcWestTreeSQL() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select atc_id,father_id ");
		sql.append("  from rt_cpa_drug_atc t ");
		sql.append(" where t.term_tree_id = ");
		sql.append(Constant.DRUG_TYPE_ATC_WEST);
		return sql.toString();
	}

	@Override
	public TreeMgr getDiagTree() {
		return new TreeMgr("rt_cpa_tree_diag", "id", "father_id", 25201,
				null);
	}

	@Override
	public TreeMgr getDrugWestTree() {
		return new TreeMgr("rt_cpa_drug_atc", "atc_id", "father_id",
				-1," term_tree_id = " + Constant.DRUG_TYPE_ATC_WEST );
	}

	@Override
	public TreeMgr getDrugChinaTree() {
		return new TreeMgr("rt_cpa_drug_atc", "atc_id", "father_id",
				-1," term_tree_id = " + Constant.DRUG_TYPE_ATC_CHINA );
	}

	@Override
	public TreeMgr getTreatTree() {
		return  new TreeMgr("rt_cpa_tree_exam", "id", "father_id", 25401,null);
	}

	@Override
	public TreeMgr getCsumTree() {
		return  new TreeMgr("rt_cpa_csum_tree_cfda", "csum_cat_id", "csum_cat_pa_id", -1,null);
	}

	@Override
	public String getDrugGenNameSql() {
		String sql ="select t.gen_name_id,t.drug_id from RT_CPA_DRUG t ";
		return sql;
	}

}
