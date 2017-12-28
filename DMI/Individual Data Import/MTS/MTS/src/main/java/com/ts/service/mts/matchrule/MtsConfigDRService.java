package com.ts.service.mts.matchrule;


import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.entity.mts.MtsConfig;
import com.ts.entity.mts.MtsConfigDetail;
import com.ts.entity.mts.MtsConfigRecord;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.util.PageData;


public interface MtsConfigDRService {
	
	
	public void addConfigRecord(MtsConfigRecord mcr) throws Exception;
	public List<PageData> listConfig(Page page) throws Exception;
	public List<PageData> listTitle(Page page) throws Exception;
	public List<PageData> listRecord(int t_id) throws Exception;
	public String listDetail(int t_id) throws Exception;
	public List<PageData> listZDRecord(Page page) throws Exception;
	public List<PageData>  listWFBH() throws Exception;
	public List<PageData>  listTermType() throws Exception;
	public void editDetail(Page page) throws Exception;
	public void deleteRecord(int t_id) throws Exception;
	public void deleteRecordByPt(String pt_id) throws Exception;
	public void deleteDetailByPt(String pt_id) throws Exception;
	public void deleteConfigByPt(String pt_id) throws Exception;
	public List<PageData>  findDataSource() throws Exception;
	public void addConfig(MtsConfig mc) throws Exception;
	public void addConfigDetail(MtsConfigDetail mcd) throws Exception;
	public void editConfigDetail(MtsConfigDetail mcd) throws Exception;
	public List<MtsConfigDetail> findByPT(String PT_ID) throws Exception;
	
	
}
