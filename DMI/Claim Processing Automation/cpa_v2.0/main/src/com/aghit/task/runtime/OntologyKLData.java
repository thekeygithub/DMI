package com.aghit.task.runtime;

import com.aghit.task.manager.TreeMgr;

/**
 * 知识本体知识
 */
public class OntologyKLData implements IKLData {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCsumCateSql() {
		// 构造SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("select t.csum_name_id, t.csum_cat_id ");
		sql.append("  from s_csum_tree_cfda t ");
	    sql.append(" where csum_name_id is not null ");
	    return sql.toString();
	}

	@Override
	public String getAtcWestSql() {
		// 构造SQL语句-西药分类
		StringBuilder sql = new StringBuilder();
		sql.append("select 'X_' || t.drug_name_id || '_' || t.form_id  as key, t.atc_id ");
		sql.append("  from s_drug_atc_west t ");
		sql.append(" where t.drug_name_id is not null ");
		sql.append("   and t.form_id is not null ");
		sql.append("   and t.stop_sign = '0' ");
		return sql.toString();
	}

	@Override
	public String getAtcChinaSql() {
		// 构造SQL语句-中药分类
		StringBuilder sql = new StringBuilder();
		sql.append("select 'Z_' || t.drug_name_id as key, t.atc_id ");
		sql.append("  from s_drug_atc_china t ");
		sql.append(" where t.drug_name_id is not null ");
		sql.append("   and t.stop_sign = '0' ");
		return sql.toString();
	}

	@Override
	public String getAtcWestTreeSQL() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select atc_id,father_id from s_drug_atc_west where stop_sign = '0' ");
		return sql.toString();

	}

	@Override
	public TreeMgr getDiagTree() {
		return new TreeMgr("o_tree_diag t, o_diag d", "ID", "FATHER_ID", 25201,
				" t.id = d.diag_id and d.stop_sign = '0' ");
	}

	@Override
	public TreeMgr getDrugWestTree() {
		return new TreeMgr("s_drug_atc_west", "atc_id", "father_id",
				-1," stop_sign = '0'");
	}

	@Override
	public TreeMgr getDrugChinaTree() {
		return new TreeMgr("s_drug_atc_china", "atc_id", "father_id",
				-1," stop_sign = '0'");
	}

	@Override
	public TreeMgr getTreatTree() {
		return  new TreeMgr("o_tree_exam", "id", "father_id", 25401,null);
	}

	@Override
	public TreeMgr getCsumTree() {
		return  new TreeMgr("s_csum_tree_cfda", "csum_cat_id", "csum_cat_pa_id", -1,null);
	}

	@Override
	public String getDrugGenNameSql() {
		String sql ="select t.gen_name_id,t.drug_id from o_drug t where t.stop_sign='0'";
		return sql;
	}

}
