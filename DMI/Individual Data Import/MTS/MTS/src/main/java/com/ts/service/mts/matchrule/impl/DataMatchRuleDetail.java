package com.ts.service.mts.matchrule.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.service.mts.matchrule.MatchRuleDetailManger;
import com.ts.util.PageData;

@Service("DataMatchRuleDetail")
public class DataMatchRuleDetail implements MatchRuleDetailManger {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	/**
	 * 根据聚类代码，查询标准匹配的标化类型（即：获得药品的最后一步标化类型）
	 * @param 聚类代码
	 * @return
	 * @throws Exception
	 */
	public String findByClass(String dataclass,String bhlx) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("DATA_CLASS_CODE", dataclass);
		map.put("FLAG", "0");
		map.put("IFNEXT", bhlx);
		return  (String) dao.findForObject("MtsMatchRuleDetail.findByClass", map);
	}
	
	/**
	 * 通过Id查询单个匹配规则详细
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public MatchRuleDetailT findRuleDetailById(String pd) throws Exception {
				
		return  (MatchRuleDetailT) dao.findForObject("MtsMatchRuleDetail.findRuleDetailById", pd);
	}	
	
	
	/**添加匹配规则详细
	 * @param pd
	 * @throws Exception
	 */
	public void addRuleDetail(MatchRuleDetailT mrd) throws Exception {
		dao.save("MtsMatchRuleDetail.addRuleDetail", mrd);
	}
	
	/**修改匹配规则详细
	 * @param pd
	 * @throws Exception
	 */
	public void editRuleDetail(MatchRuleDetailT mrd) throws Exception {
		dao.update("MtsMatchRuleDetail.editRuleDetail", mrd);
	}
	
	/**删除匹配规则详细
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRuleDetail(String MEM_ID) throws Exception {
		dao.delete("MtsMatchRuleDetail.deleteRuleDetail", MEM_ID);
	}
	
	/**
	 * 根据聚类查询匹配规则
	 */
	@SuppressWarnings("unchecked")	
	public List<MatchRuleDetailT> findMatchRuleListByClassCode(MatchRuleDetailT matchRuleDetailT) throws Exception {
	
		return (List<MatchRuleDetailT>) dao.findForList("MtsMatchRuleDetail.findMatchRuleListByClassCode", matchRuleDetailT);
	}
	
		
	/**
	 * 分页查询匹配流程列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listRuleDetail(Page page) throws Exception {
		return (List<PageData>) dao.findForList("MtsMatchRuleDetail.DetailRulelistPage", page);
	}

	
	public String maxRule() throws Exception {
		
		return (String) dao.findForObject("MtsMatchRuleDetail.maxRule", null);
	}

	
	@SuppressWarnings("unchecked")
	public List<PageData> listSFDetail(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsMatchRuleDetail.DetailRuleSF", page);
	}

	

	/**
	 * 查询程序列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findSoft() throws Exception {
		
		return (List<PageData>) dao.findForList("MtsMatchRuleDetail.findSoft", null);
	}

	/**
	 * 判断流程入口是否存在
	 */
	public MatchRuleDetailT findRuleDetail(Page page) throws Exception {
		
		return (MatchRuleDetailT) dao.findForObject("MtsMatchRuleDetail.findRuleDetail", page);
	}

	/**
	 * 查询数据特殊处理流程按区域聚类分组后数量
	 */
	@SuppressWarnings("unchecked")	
	public List<PageData> findSpecCnt() throws Exception {
		
		return (List<PageData>) dao.findForList("MtsMatchRuleDetail.findSpecCnt", null);
	}

	/**
	 * 按区域聚类查询数据特殊处理流程列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> listSpecSoft(String areaid,String clacode) throws Exception {
		Page page=new Page();
		PageData pd = new PageData();
		pd.put("AREA_ID", areaid);
		pd.put("CLASSCODE", clacode);
		page.setPd(pd);
		return (List<String>) dao.findForList("MtsMatchRuleDetail.findSpecSoft", page);
	}

}
