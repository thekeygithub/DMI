package com.aghit.task.manager;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aghit.task.util.Constant;

public class DrugDataMgr extends Manager {

	private Logger log = Logger.getLogger(DrugDataMgr.class);

	private String sql="select t.drug_id,t.lgcy_drug_code from r_drug_rela t where t.data_src_id = ? OR t.data_src_id= ? ";
	private Map<Integer,List<Integer>> data=new HashMap<Integer,List<Integer>>();
	
	public void init() throws Exception{
		
		try {
			SqlRowSet rs=this.getJdbcService().findByQuery(sql, new Object[] {Constant.DRUG_CATE_ATC_WEST, Constant.DRUG_CATE_ATC_CHINA });
			int code;
			int drug;
			List<Integer> temp = null;
			while(rs.next()){
				code=rs.getInt("lgcy_drug_code");
				drug=rs.getInt("drug_id");
				temp = this.data.get(code);
				if(temp==null){
					temp=new ArrayList<Integer>();
				}
				temp.add(drug);
			}
		} catch (Exception e) {
			throw new Exception("查询表R_DRUG_RELA出错,"+e.getMessage());
		}
	}
	
	/**
	 * 取得这些code下关联的所有药品
	 * @param codes
	 * @return
	 */
	public Set<Integer> getCodes(int codes[]){
		Set s=new HashSet();
		for (int i = 0; i < codes.length; i++) {
			int c=codes[i];
			List<Integer> temp = this.data.get(c);
			if(temp!=null){
				s.addAll(temp);
			}
		}
		return s;
	}
}
