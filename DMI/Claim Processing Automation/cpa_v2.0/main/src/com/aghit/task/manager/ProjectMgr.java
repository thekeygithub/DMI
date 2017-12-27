package com.aghit.task.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aghit.base.BaseService;
import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.common.service.impl.CacheServiceImpl;
import com.aghit.task.manager.rule.KlRuleMgr;
import com.aghit.task.util.Constant;

@Service("projectMgr")
public class ProjectMgr extends BaseService {
	
	// 日志
	private Logger log = Logger.getLogger(ProjectMgr.class);

	// 单例模式
	private static ProjectMgr mgr = new ProjectMgr();

	/**
	 * 取得审核方案的规则中最大的一个结束周期时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getMaxRuleDate(long scenarioId) throws Exception {

		String sql = "SELECT max(cycle_end) maxend "
				+ "FROM cpa_rule_common t, cpa_Scenario_Rule_Relation re "
				+ "WHERE t.rule_id = re.rule_id " + "AND t.enable_flg = ? "
				+ "AND re.scenario_id = ? ";

		Map<String, Object> m = this.getJdbcService().queryForMap(sql,
				new Object[] { Constant.ENABLED, scenarioId });

		if (m.get("maxend") == null) {
			return 0;
		}
		return Integer.parseInt(m.get("maxend").toString());
	}
	
	
	public List<AbstractRule> initRules() throws Exception {
		
		List<AbstractRule> rules = new ArrayList<AbstractRule>();
		List<AbstractRule> temp = null;	
//		// 加载单项目同类互斥
//		temp =AloneRuleMgr.getInstance().initRule(this);
//		this.rules.addAll(temp);
//		log.info("load alone rule size:" + temp.size());
//		
//		// 加载知识库规则
//		temp = LoreRuleMgr.getInstance().initRule(this);
//		this.rules.addAll(temp);
//		log.info("load lore rule size:" + temp.size());
//
//		// 加载分解住院审核的规则
//		temp = DateAuditRule.getInstance().initRule(this);
//		this.rules.addAll(temp);
//		log.info("load initInpDateAuditRule rule size:" + temp.size());
//		
//		//加载多项目规则
//		temp = MultiConditionRuleMgr.getInstance().initRule(this);
//		this.rules.addAll(temp);
//		log.info("load MultiConditionRule rule size:" + temp.size());
		//加载知识规则
		temp = KlRuleMgr.getInstance().initRule();
		rules.addAll(temp);
		log.info("load knowledge rule size:" + temp.size());
		 
		return rules;
	}
	
	/**
	 * 获得所有有效方案与规则的对应关系
	 * @return
	 * @throws Exception
	 */
	public Map<Long,List<Long>> initScenarioRuleRel() throws Exception{
		String sql ="select b.scenario_id,b.rule_id " +
				" from cpa_scenario a " + 
                " inner join cpa_scenario_rule_relation b on a.scenario_id = b.scenario_id " +
                " where a.enable_flg = ? " ;
		List<Map<String, Object>> list = this.getJdbcService().findForList(sql,
				new Object[] {Constant.ENABLED});
				
		Map<Long,List<Long>> rel = new HashMap<Long,List<Long>>();
		
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			long scenarioId = Long.parseLong(m.get("scenario_id").toString());
			long ruleId = Long.parseLong(m.get("rule_id").toString());
			List<Long> rules = rel.get(scenarioId);
			if (rules == null) {
				rules = new ArrayList<Long>();
			}
			rules.add(ruleId);
			rel.put(scenarioId, rules);
		}
		
		return rel;
	}
	
	/**
	 * 根据方案号获得缓存中规则
	 * @param rules
	 * @param rel
	 * @param scenarioId
	 * @return
	 * @throws Exception
	 */
	public List<AbstractRule> getRules(long scenarioId) throws Exception {
		
		List<AbstractRule> rules = CacheServiceImpl.getRules();
		Map<Long,List<Long>> rel = CacheServiceImpl.getSernarioRuleRel();
		
		List<AbstractRule> rs = new ArrayList<AbstractRule>();
		
		List<Long> ruleIds = rel.get(scenarioId);
		 
		 if (null == ruleIds){
			 return null;
		 }
		 
		 for (AbstractRule r: rules){
			 for (long id : ruleIds){
				 if (r.ruleId == id){
					 rs.add(r);
					 break;
				 }
			 }
		 }
		 
		 return rs;
	}
	
	
	/**
	 * 根据类型获得子项
	 * @param checkType
	 * @param codes
	 * @return
	 */
	public String[] getChild(int checkType, String[] codes) {

		String[] cs = null;
		switch (checkType){
		case Constant.ELEM_TYPE_DIAG:
			cs = CacheServiceImpl.getDiagTree().getChild(codes);
			break;
		default:
			cs=codes;
			break;
		}
		
		return cs;
	}
	
}
