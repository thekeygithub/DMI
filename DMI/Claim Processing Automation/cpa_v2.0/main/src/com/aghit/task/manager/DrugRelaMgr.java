package com.aghit.task.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aghit.task.util.Constant;

public class DrugRelaMgr extends Manager {

	private Logger log = Logger.getLogger(DrugRelaMgr.class);
	Map<String, List<String>> tree = new HashMap<String, List<String>>();

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

		tree.clear();

		String sql = "select drug_id,lgcy_drug_code from r_drug_rela where data_src_id = ? OR data_src_id= ?";
		SqlRowSet s = this.getJdbcService().findByQuery(sql, new Object[] {Constant.DRUG_CATE_ATC_WEST, Constant.DRUG_CATE_ATC_CHINA });

		while (s.next()) {
			String drugId = s.getLong("drug_id")+"";
			String code = s.getString("lgcy_drug_code");
			List<String> temp = tree.get(code);
			if (temp == null) {
				temp = new ArrayList<String>();
			}
			temp.add(drugId);
			this.tree.put(code, temp);
		}
		log.info("init r_drug_rela size = " + this.tree.size());
	}

	/**
	 * 有可能返回null
	 * @param lgcy_drug_code
	 * @return
	 */
	public String[] getDrugList(String lgcy_drug_code) {
		List<String> temp =this.tree.get(lgcy_drug_code);
		if(temp==null){
			return null;
		}else{
			return temp.toArray(new String[temp.size()]);
		}
	}
	
	/**
	 * 批量查询
	 * @param lgcy_drug_codes
	 * @return
	 */
	public String[] getDrugList(String[] lgcy_drug_codes) {
		
		Set<String> list=new HashSet<String>();
		for (int i = 0; i < lgcy_drug_codes.length; i++) {
			String lgcy_drug_code=lgcy_drug_codes[i];
			List<String> temp =this.tree.get(lgcy_drug_code);
			if(temp!=null){
				list.addAll(temp);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
}
