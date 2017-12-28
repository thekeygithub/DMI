package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.util.PageData;

public interface MatchRuleDetailManger {
	
	public String findByClass(String pd,String bhlx) throws Exception;
	
	public MatchRuleDetailT findRuleDetailById(String pd) throws Exception;
	
	public List<PageData> listRuleDetail(Page page) throws Exception;
	
	public void addRuleDetail(MatchRuleDetailT mrd) throws Exception;
	
	public void editRuleDetail(MatchRuleDetailT mrd) throws Exception;
	
	public void deleteRuleDetail(String ROLE_ID) throws Exception;
	
	public List<MatchRuleDetailT> findMatchRuleListByClassCode(MatchRuleDetailT matchRuleDetailT) throws Exception ;
	
	public String maxRule() throws Exception;
	
	public List<PageData> listSFDetail(Page page) throws Exception;
	
	public List<PageData> findSoft() throws Exception;
	
	public MatchRuleDetailT findRuleDetail(Page page) throws Exception;
	
	public List<PageData> findSpecCnt() throws Exception;
	
	public List<String> listSpecSoft(String areaid,String clacode) throws Exception;
}
