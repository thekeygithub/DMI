package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MatchRule;
import com.ts.service.mts.matchrule.MatchRuleManger;
import com.ts.util.PageData;

@Service("DataMatchRule")
public class DataMatchRule implements MatchRuleManger {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	/**
	 * 根据标化id，查询匹配规则信息
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	public MatchRule findByType(String bhlx) throws Exception  {
		return (MatchRule) dao.findForObject("MtsMatchRuleMapper.findByType", bhlx);
	
	}
	
	/**
	 * 通过Id查询单个匹配规则
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public MatchRule findRuleById(String pd) throws Exception {
				
		return  (MatchRule) dao.findForObject("MtsMatchRuleMapper.findRuleById", pd);
	}
	
	/**
	 * 查询匹配规则列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> matchRules(Page page) throws Exception {
		return (List<PageData>) dao.findForList("MtsMatchRuleMapper.matchRulelistPage",page);
	}
	
	/**
	 * 查询匹配规则列表加载缓存用
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> matchRulelp(Page page) throws Exception {
		return (List<PageData>) dao.findForList("MtsMatchRuleMapper.matchRulelp",page);
	}
	
	/**添加匹配规则
	 * @param pd
	 * @throws Exception
	 */
	public void addRule(MatchRule mtr) throws Exception {
		dao.save("MtsMatchRuleMapper.addRule", mtr);
	}
	
	/**保存修改匹配规则
	 * @param pd
	 * @throws Exception
	 */
	public void editRule(MatchRule mtr) throws Exception {
		dao.update("MtsMatchRuleMapper.editRule", mtr);
	}
	
	/**删除匹配规则
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRule(String RULE_ID) throws Exception {
		dao.delete("MtsMatchRuleMapper.deleteRule", RULE_ID);
	}


	public String maxRule() throws Exception {		
		return (String) dao.findForObject("MtsMatchRuleMapper.maxRule", null);
	}

	
	public MatchRule findByAreaType(Page page) throws Exception {
		
		return (MatchRule) dao.findForObject("MtsMatchRuleMapper.findByAreaType", page);
	}


}
