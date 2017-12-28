package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MatchRule;
import com.ts.util.PageData;

public interface MatchRuleManger {
	
	public MatchRule findByType(String bhlx) throws Exception;
	
	public MatchRule findRuleById(String pd) throws Exception;
	
	public List<PageData> matchRules(Page page) throws Exception;
	
	public void addRule(MatchRule mtr) throws Exception;
	
	public void editRule(MatchRule mtr) throws Exception;
	
	public void deleteRule(String RULE_ID) throws Exception;
	
	public String maxRule() throws Exception;
	
	public MatchRule findByAreaType(Page page) throws Exception;
	
	public List<PageData> matchRulelp(Page page) throws Exception;
}
