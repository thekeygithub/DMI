package com.aghit.task.manager.rule;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aghit.base.MessageService;
import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.common.service.impl.CacheServiceImpl;
import com.aghit.task.manager.Manager;
import com.aghit.task.manager.MsgServiceFactory;
import com.aghit.task.manager.ProjectMgr;
import com.aghit.task.util.Constant;

public abstract class RuleMgr extends Manager {

	private Logger log = Logger.getLogger(RuleMgr.class);

	final public MessageService msgService = MsgServiceFactory
			.getMessageService();

	/**
	 * 根据方案ID和规则类型查找规则的基本信息
	 * 表cpa_rule_common cpa_Scenario_Rule_Relation
	 * @param projectNo
	 * @param ruleType
	 * @param runModel:区别是否审核项目还是分类
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> loadRuleCommon(long projectNo, int ruleType)
			throws Exception {
		// 根据方案查找具体的规则
		String sql = "select t.rule_id,t.rule_name,t.cycle_flg,"
				+ "t.cycle_start,t.cycle_end,t.check_type,t.medical_type,use_standard,t.run_model "
				+ " from cpa_rule_common t,cpa_Scenario_Rule_Relation sr "
				+ " where sr.scenario_id= ? and sr.rule_id=t.rule_id "
				+ " and t.rule_type =? and t.enable_flg=? ";
		sql = sql + " order by t.rule_id";
		List<Map<String, Object>> list = this.getJdbcService().findForList(sql,
				new Object[] { projectNo, ruleType + "", Constant.ENABLED });
		return list;
	}
	
	/**
	 * 根据方案ID和规则类型查找规则的基本信息
	 * 表cpa_rule_common cpa_Scenario_Rule_Relation
	 * @param projectNo
	 * @param ruleType
	 * @param runModel:区别是否审核项目还是分类
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> loadRuleCommon(int ruleType)
			throws Exception {
		// 查询所有规则
		String sql = "select t.rule_id,t.rule_name,t.cycle_flg,"
				+ "t.cycle_start,t.cycle_end,t.check_type,t.medical_type,t.use_standard,t.run_model "
				+ " from cpa_rule_common t  "
				+ "  where t.rule_type =? and t.enable_flg=? ";
		sql = sql + " order by t.rule_id";
		List<Map<String, Object>> list = this.getJdbcService().findForList(sql,
				new Object[] {ruleType + "", Constant.ENABLED });
		return list;
	}

	/**
	 * 返回规则的 t.category_name(用户定义的规则类别)
	 * 
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	public String getCategory_name(long ruleId) throws Exception {

		String sql = "select t.category_name from cpa_rule_category t,"
				+ "cpa_rule_category_relation r where r.rule_id=" + ruleId
				+ " and r.category_id=t.category_id";
		List<Map<String, Object>> list = this.getJdbcService().findForList(sql,
				null);
		if (list != null && list.size() > 0) {
			return list.get(0).get("category_name").toString();
		} else {
			return null;
		}

	}

//	public abstract List<AbstractRule> initRule(int projectNo) throws Exception;
	
	public abstract List<AbstractRule> initRule() throws Exception;

}
