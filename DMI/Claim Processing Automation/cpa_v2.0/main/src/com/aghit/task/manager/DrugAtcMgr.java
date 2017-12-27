package com.aghit.task.manager;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aghit.task.runtime.KLDataService;

public class DrugAtcMgr extends Manager {

	private Logger log = Logger.getLogger(DrugAtcMgr.class);
	// key=child value=father
	public Map<Long, Long> father_tree = new HashMap<Long, Long>();

	// key=atc value=drug
	public Map<String, String> atc_drug = new HashMap<String, String>();

	// K_DRUG_CINDI_INDI 药品禁忌症、适应症 key=drug_id+_+type value=diagId
	private Map<String, Set<String>> drug_diag = new HashMap<String, Set<String>>();

	private DrugRelaMgr drugRealTree = null;

	private TreeMgr diagTree = null; //诊断树

	public void init(TreeMgr diagTree,DrugRelaMgr drugRealTree)
			throws Exception {
		
		father_tree.clear();
		
		this.diagTree = diagTree;
		this.drugRealTree = drugRealTree;

		String sql = KLDataService.getAtcWestTreeSql();
		SqlRowSet s = this.getJdbcService().findByQuery(sql, null);

		int count = 0;
		while (s.next()) {
			long atc_ic = s.getLong("atc_id");
			long father_id = s.getLong("father_id");
			father_tree.put(atc_ic, father_id);
			count++;
		}
		log.info("init s_drug_atc_west size = " + count);

		// K_DRUG_CINDI_INDI_REP 知识库 - 禁忌、适应症替代表
		sql = "select atc_id,drug_id from k_drug_ci_rep ";
		s = this.getJdbcService().findByQuery(sql, null);
		count = 0;
		while (s.next()) {
			String atc_ic = s.getLong("atc_id") + "";
			String drug_id = s.getLong("drug_id") + "";
			atc_drug.put(atc_ic, drug_id);
			count++;
		}
		log.info("init k_drug_cindi_indi_rep size = " + count);

		// K_DRUG_CINDI_INDI 药品禁忌症、适应症
		sql = "select drug_id,kdc_type,diag_id from k_drug_ci";
		s = this.getJdbcService().findByQuery(sql, null);
		count = 0;
		while (s.next()) {
			long drug_id = s.getLong("drug_id");
			String kdc_type = s.getString("kdc_type");
			String diag_id = s.getLong("diag_id")+"";
			Set<String> set = null;
			if ((set = this.drug_diag.get(drug_id + "_" + kdc_type)) == null) {
				set = new HashSet<String>();
			}
			//把一个诊断代码翻译成多个真实的诊断
			String[] diags=this.diagTree.getChild(diag_id);
			for (int i = 0; i < diags.length; i++) {
				set.add(diags[i]);
			}
			drug_diag.put(drug_id + "_" + kdc_type, set);
			count++;
		}
		log.info("init k_drug_cindi_indi size = " + count);
	}

	/**
	 * 
	 * @param drugAtc
	 *            药品atc
	 * @param type
	 *            适应症/禁忌症
	 * @return  Map<药品, List<诊断列表>>
	 */
	public Map<String, Set<String>> findDiag(String[] atcs, int type) {

		Map<String, Set<String>> maps = new HashMap<String, Set<String>>();

		for (int i = 0; i < atcs.length; i++) {
			String atc = atcs[i];
			String[] drug_codes = this.drugRealTree.getDrugList(atc);
			Set<String> diags = null;
			if (drug_codes == null) {
				continue;
			} else {
				Map<String, Set<String>> temp = null;
				if ((temp = this.getValuesFromMap(drug_codes, type)).size() > 0) {
					maps.putAll(temp);
				} else {
					// 第一次到父子关系表中查询
					String drug_code = this.atc_drug.get(atc);
					if (drug_code != null
							&& (diags=this.drug_diag.get(drug_code + "_" + type)) != null) {
						maps.put(drug_code + "_" + type, diags);

					} else {
						// 没有对应的禁忌或适应症时应找到atc的替代库
						while ((atc = this.father_tree.get(new Integer(atc))
								+ "") != null) {
							if ((drug_code = this.atc_drug.get(atc)) != null
									&& (diags = this.drug_diag.get(drug_code+ "_" + type)) != null) {
								maps.put(drug_code+ "_" + type, diags);
								break;
							}
						}
					}
				}
			}
		}

		return maps;
	}

	private Map<String, Set<String>> getValuesFromMap(String[] keys, int type) {

		Map<String, Set<String>> maps = new HashMap<String, Set<String>>();
		List<Integer> diags = null;
		for (int i = 0; i < keys.length; i++) {
			if (this.drug_diag.get(keys[i] + "_" + type) != null) {
				maps.put(keys[i] + "_" + type, this.drug_diag.get(keys[i] + "_"
						+ type));
			}
		}

		return maps;
	}

}
