package com.aghit.task.manager.rule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.rule.HospitalPatientRule;
import com.aghit.task.manager.ProjectMgr;

public class DateAuditRule extends RuleMgr{

	private Logger log = Logger.getLogger(DateAuditRule.class);

	private static DateAuditRule mgr = new DateAuditRule();

	private DateAuditRule() {
	}

	public static DateAuditRule getInstance() {
		return mgr;
	}

	/**
	 * 加载分解住院规则
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AbstractRule> initRule() throws Exception {

		List<AbstractRule> result = new ArrayList<AbstractRule>();

		try {
			// 拼接数据库查询语句
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT r.*,n.interval ");
			sql.append("  FROM CPA_Scenario 			  s, ");
			sql.append("	   CPA_Scenario_Rule_Relation sr, ");
			sql.append("	   CPA_Rule_Common			  r, ");
			sql.append("	   CPA_Interval_Check_Rule	  n ");
			sql.append(" WHERE s.scenario_id = sr.scenario_id ");
			sql.append("   AND r.rule_id = sr.rule_id ");
			sql.append("   AND n.rule_id = r.rule_id ");
			sql.append("   AND r.rule_type = ? ");

			// 查询参数
			Object[] params = new Object[] { 3 };
			final DateAuditRule mgr=this;
			result = getJdbcService().findListByQuery(sql.toString(), params,
					new RowMapper<AbstractRule>() {

						@Override
						public AbstractRule mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							HospitalPatientRule rule = new HospitalPatientRule(
									rs.getInt("INTERVAL"),
									rs.getLong("RULE_ID"), null);
							try {
								rule.category_name = mgr.getCategory_name(rs.getLong("RULE_ID"));
							} catch (Exception e) {
								log.error(e.getMessage());
							} //取得用户定义的规则类别
							return rule;
						}
					});

		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

		return result;
	}
}
