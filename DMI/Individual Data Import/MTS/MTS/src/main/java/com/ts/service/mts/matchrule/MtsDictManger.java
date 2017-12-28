package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDict;
import com.ts.util.PageData;

public interface MtsDictManger {

	public List<MtsDict> listAllByClass(Page page) throws Exception;

	public List<MtsDict> listKeyRuleByClass(String dATACLASS) throws Exception;

	public List<MtsDict> listValueRuleByClass(String dATACLASS) throws Exception;

	public List<PageData> findMtsDictByPd(PageData pd) throws Exception;

	public List<PageData> findMtsKeyDictByRuleID(String ruleid) throws Exception;
	
	public List<PageData> findMtsValueDictByRuleID(String ruleid) throws Exception;

	public String maxRuleDict() throws Exception;

	public void saveRuleDict(PageData pd) throws Exception;
	
	public void deleteMtsRuleDict(String ruleid) throws Exception;

}
