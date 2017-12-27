package com.aghit.task.common.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.aghit.base.BaseService;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.common.entity.klbase.KlElemVal;
import com.aghit.task.common.entity.klbase.KlObj;
import com.aghit.task.common.entity.klbase.KlStat;
import com.aghit.task.common.service.KlDataService;
import com.aghit.task.util.Constant;


/**
 * 知识服务实现
 */
public class KlDataServiceImpl extends BaseService implements KlDataService {

	
	public void test(){
//		List<Long> t = new ArrayList<Long>();
//		t.add((long) 21501);
		try {
			findKlDefByRuleType(5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
 	}
	
	@Override
	public List<KlDef> findKlDefByRuleType(int ruleType) throws Exception {
		
		//查询该规则类型下所有的知识内容
		List<KlDef> klDefs = new ArrayList<KlDef>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct rule.rule_id,rule.rule_name,rule.cycle_flg,rule.cycle_start,rule.cycle_end,");
		sql.append(" rule.medical_type,rule.run_model,def.* from cpa_rule_kl_relation rkr "); 
		sql.append(" inner join k_def def on rkr.kl_id = def.k_id ");
	    sql.append(" inner join cpa_rule_common rule on rkr.rule_id = rule.rule_id ");
		sql.append(" where rule.rule_type = ? and rule.enable_flg = ? and def.stop_sign = ? and def.std_flag = ?"); 
		klDefs= getJdbcService().findListByQuery(sql.toString(),new Object[]{ruleType + "",Constant.ENABLED,Constant.STOP_ENABLED,Constant.STD_FLAG_COMPLETE }, new KlDefMapper());
		if (null == klDefs){
			log.error("数据错误：没有找到知识。");
			return null;
		}
		//拼接知识ID条件sql语句
		StringBuffer conSql = new StringBuffer();
		conSql.append("select distinct def.k_id ");
		conSql.append(" from cpa_rule_kl_relation rkr "); 
		conSql.append(" inner join k_def def on rkr.kl_id = def.k_id ");
		conSql.append(" inner join cpa_rule_common rule on rkr.rule_id = rule.rule_id ");
		conSql.append(" where rule.rule_type ='");
		conSql.append(ruleType);
		conSql.append("' and rule.enable_flg = '1' and def.stop_sign = 0 and def.std_flag = 1 ");
		
		 //获取知识主体对象
		Map<Long,List<KlObj>> mklObjs = this.findklObjByKl(conSql.toString());
		if (null == mklObjs){
			log.error("数据错误：没有找到知识主体对象！");
			return null;
		}
		//获取表达式
		Map<Long,List<KlStat>> mklStats = this.findklStatByKl(conSql.toString());
		if (null == mklStats){
			log.error("数据错误：没有找到表达式对象！");
			return null;
		}
		//获取元素定义
		Map<Long,List<KlElemDef>> mklElemDefs = this.findklElemDefByKl(conSql.toString());
		if (null == mklElemDefs){
			log.error("数据错误：没有找到元素定义！");
			return null;
		}
		
		Iterator<KlDef> it = klDefs.iterator();  
		while(it.hasNext()){  
			KlDef kldef = it.next();  
			//加载主体对象
			List<KlObj> lst = mklObjs.get(kldef.getK_id());
			if (null != lst){
				long[] lng = new long[lst.size()];
				for(int i=0;i<lst.size();i++){
					lng[i]=lst.get(i).getObj_id();
				}
				kldef.setObjIds(lng);
			}else{
				//移除不符合规格知识
				it.remove();
				log.error("数据错误：知识"+ kldef.getK_id() + "不存在知识主体对象！被移除。");
				continue;
			}
			//加载表达式
			if (null != mklStats.get(kldef.getK_id())){
				kldef.setKlStats(mklStats.get(kldef.getK_id()));
			}else{
				//移除不符合规格知识
				it.remove();
				log.error("数据错误：知识"+ kldef.getK_id() + "不存在表达式对象！被移除。");
				continue;
			}
			//加载元素
			if (null != mklElemDefs.get(kldef.getK_id())){
				kldef.setKlElemDefs(mklElemDefs.get(kldef.getK_id()));
			}else{
				//移除不符合规格知识
				it.remove();
				log.error("数据错误：知识"+ kldef.getK_id() + "不存在元素定义！被移除。");
				continue;
			}
		} 
		
		return klDefs;
	}
		
	//获取知识主体对象
	private Map<Long,List<KlObj>> findklObjByKl(String klIdSql) throws Exception{

		Map<Long,List<KlObj>> mklObjs = new HashMap<Long,List<KlObj>>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		sql.append(" from k_obj obj");
		sql.append(" where obj.k_id in (");
		sql.append(klIdSql);
		sql.append(")");
		sql.append(" order by obj.k_id ");
		List<KlObj> klObjs = getJdbcService().findListByQuery(sql.toString(),null, new KlObjMapper());
		if (null == klObjs){
			return null;
		}
		for (KlObj klObj:klObjs){
			if (null == mklObjs.get(klObj.getK_id())){
				List<KlObj> obj = new ArrayList<KlObj>();
				obj.add(klObj);
				mklObjs.put(klObj.getK_id(), obj);
			}else{
				mklObjs.get(klObj.getK_id()).add(klObj);
			}
		}
		
		return mklObjs;
	}
	
	//获取表达式
	private  Map<Long,List<KlStat>> findklStatByKl(String klIdSql) throws Exception{
		
		Map<Long,List<KlStat>> mklStats = new HashMap<Long,List<KlStat>>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		sql.append(" from k_stat stat");
		sql.append(" where stat.k_id in (");
		sql.append(klIdSql);
		sql.append(")");
		sql.append(" order by stat.k_id,cond_stat_id ");
		List<KlStat> klStats = getJdbcService().findListByQuery(sql.toString(),null, new KlStatMapper());
		if (null == klStats){
			return null;
		}
		for (KlStat klStat:klStats){
			if (null == mklStats.get(klStat.getK_id())){
				List<KlStat> stat = new ArrayList<KlStat>();
				stat.add(klStat);
				mklStats.put(klStat.getK_id(), stat);
			}else{
				mklStats.get(klStat.getK_id()).add(klStat);
			}
		}
		
		return mklStats;
	}
	
	//获取元素定义
	private Map<Long,List<KlElemDef>> findklElemDefByKl(String klIdSql) throws Exception{
			
		Map<Long,List<KlElemDef>> mklElemDefs = new HashMap<Long,List<KlElemDef>>();
		//查询元素	
		StringBuffer sql = new StringBuffer();
		sql.append("select elem.*,type.data_type ");
		sql.append(" from k_elem_def elem");
		sql.append(" left join k_elem_type type on elem.elem_type_id = type.elem_type_id ");
		sql.append(" where elem.k_id in (");
		sql.append(klIdSql);
		sql.append(")");
		sql.append(" order by elem.k_id,elem_id ");
		List<KlElemDef> klElemDefs = getJdbcService().findListByQuery(sql.toString(),null, new KlElemDefMapper());
		if (null == klElemDefs){
			return null;
		}
		
		//元素id条件sql
		StringBuffer conSql = new StringBuffer();
		conSql.append("select distinct elem.elem_id ");
		conSql.append(" from k_elem_def elem");
		conSql.append(" where elem.k_id in (");
		conSql.append(klIdSql);
		conSql.append(")");
		
		//获取知识元素取值
		Map<Long,List<KlElemVal>> mklElemVals = this.findklElemValByElem(conSql.toString());
		if (null == mklElemVals){
			log.error("数据错误：没有找到元素取值列表！");
			return null;
		}
		
		for(KlElemDef klElemDef:klElemDefs){
			//加载条件值
			List<KlElemVal> lst =mklElemVals.get(klElemDef.getElem_id());
			if (null != mklElemVals.get(klElemDef.getElem_id())){	
				String[] lng = new String[lst.size()];
				for(int i=0;i<lst.size();i++){
					lng[i]=lst.get(i).getVal();
				}
			    Arrays.sort(lng);
				klElemDef.setElemVals(lng);
			}else{
				log.error("数据错误：元素"+ klElemDef.getElem_id() + "不存在条件取值列表！");
			}
			//组织返回元素集合
			if (null == mklElemDefs.get(klElemDef.getK_id())){
				List<KlElemDef> def = new ArrayList<KlElemDef>();
				def.add(klElemDef);
				mklElemDefs.put(klElemDef.getK_id(), def);
			}else{
				mklElemDefs.get(klElemDef.getK_id()).add(klElemDef);
			}
		}
		
		return mklElemDefs;
	} 
	
	//获取元素取值列表
	private  Map<Long,List<KlElemVal>> findklElemValByElem(String elemIdSql) throws Exception{
		Map<Long,List<KlElemVal>> mklElemVals = new HashMap<Long,List<KlElemVal>>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		sql.append(" from k_elem_val val");
		sql.append(" where val.elem_id in (");
		sql.append(elemIdSql);
		sql.append(") and val is not null ");
		sql.append(" order by val.elem_id ");
		List<KlElemVal> klElemVals = getJdbcService().findListByQuery(sql.toString(),null, new KlElemValMapper());
		if (null == klElemVals){
			return null;
		}
		for (KlElemVal klElemVal:klElemVals){
			if (null == mklElemVals.get(klElemVal.getElem_id())){
				List<KlElemVal> val = new ArrayList<KlElemVal>();
				val.add(klElemVal);
				mklElemVals.put(klElemVal.getElem_id(), val);
			}else{
				mklElemVals.get(klElemVal.getElem_id()).add(klElemVal);
			}
		}
		
		return mklElemVals;
	} 
	
	
	//知识定义mapper
	private static final class KlDefMapper implements RowMapper<KlDef> {

	    public KlDef mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KlDef klDef = new KlDef();
	    	
	        klDef.setRule_id(rs.getLong("rule_id"));
	    	klDef.setK_id(rs.getLong("k_id"));
	    	klDef.setK_subj_type_id(rs.getLong("k_subj_type_id"));
	    	klDef.setObj_elem_type_id(rs.getInt("obj_elem_type_id"));
	    	klDef.setK_src_id(rs.getLong("k_src_id"));
	    	klDef.setRegn_id(rs.getLong("regn_id"));
	    	klDef.setScene_flag(rs.getInt("scene_flag"));
	    	klDef.setScene_enable(rs.getInt("scene_enable"));
	    	klDef.setStop_sign(rs.getInt("stop_sign"));
	    	klDef.setStd_flag(rs.getInt("std_flag"));
	    	klDef.setLast_trans_date(rs.getDate("last_trans_date"));
	    	klDef.setAg_ver(rs.getString("ag_ver"));
	    	
	    	klDef.setRule_name(rs.getString("rule_name"));
	    	klDef.setCycle_flg(rs.getInt("cycle_flg"));
	    	klDef.setCycle_start(rs.getInt("cycle_start"));
	    	klDef.setCycle_end(rs.getInt("cycle_end"));
	    	klDef.setMedical_type(rs.getString("medical_type"));
   
	        return klDef;
	    }
	}
	
	//知识主题对象mapper
	private static final class KlObjMapper implements RowMapper<KlObj> {

	    public KlObj mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KlObj klObj = new KlObj();
	       
	    	klObj.setK_id(rs.getLong("k_id"));
	    	klObj.setObj_id(rs.getLong("obj_id"));
   
	        return klObj;
	    }
	}
	
	//知识表达式mapper
	private static final class KlStatMapper implements RowMapper<KlStat> {

	    public KlStat mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KlStat klStat = new KlStat();
	    	klStat.setCond_stat_id(rs.getLong("cond_stat_id"));
	    	klStat.setK_id(rs.getLong("k_id"));
	    	klStat.setScene_stat(rs.getString("scene_stat"));
	    	klStat.setCond_stat(rs.getString("cond_stat"));
	    	klStat.setCond_stat_rel(rs.getInt("cond_stat_rel"));
	    	klStat.setWarn_desc(rs.getString("warn_desc"));
	    	klStat.setK_warn_lvl_id(rs.getLong("k_warn_lvl_id"));
   
	        return klStat;
	    }
	}
	
	//元素定义mapper
	private static final class KlElemDefMapper implements RowMapper<KlElemDef> {

	    public KlElemDef mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KlElemDef klElemDef = new KlElemDef();
	    	klElemDef.setElem_id(rs.getLong("elem_id"));
	    	klElemDef.setK_id(rs.getLong("k_id"));
	    	klElemDef.setElem_type_id(rs.getInt("elem_type_id"));
	    	klElemDef.setCategory_id(rs.getLong("category_id"));
	    	klElemDef.setElem_symb(rs.getString("elem_symb"));
	    	klElemDef.setScene_flag(rs.getInt("scene_flag"));
	    	klElemDef.setCond_flag(rs.getInt("cond_flag"));
	    	klElemDef.setIntv_range(rs.getString("intv_range"));
	    	klElemDef.setIntv_unit(rs.getString("intv_unit"));
	    	klElemDef.setEx_obj_flag(rs.getInt("ex_obj_flag"));
	    	klElemDef.setRange_flag(rs.getInt("range_flag"));
	    	klElemDef.setNull_flag(rs.getInt("null_flag"));
	    	klElemDef.setElem_Data_type(rs.getInt("data_type"));
	        return klElemDef;
	    }
	}
	
	//元素定义mapper
	private static final class KlElemValMapper implements RowMapper<KlElemVal> {

	    public KlElemVal mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KlElemVal klElemVal = new KlElemVal();
	    	klElemVal.setElem_det_id(rs.getLong("elem_det_id"));
	    	klElemVal.setElem_id(rs.getLong("elem_id"));
	    	klElemVal.setVal(rs.getString("val"));
	    	
	        return klElemVal;
	    }
	}


}
