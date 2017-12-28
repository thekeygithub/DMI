package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MatchRule;
import com.ts.entity.mts.MtsConfig;
import com.ts.entity.mts.MtsConfigDetail;
import com.ts.entity.mts.MtsConfigRecord;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.service.mts.matchrule.MatchRuleManger;
import com.ts.service.mts.matchrule.MtsConfigDRService;
import com.ts.util.PageData;

@Service("DataConfigRecord")
public class DataConfigRecord implements MtsConfigDRService {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	
	
	/**添加匹配结果列表
	 * @param pd
	 * @throws Exception
	 */
	public void addConfigRecord(MtsConfigRecord mcr) throws Exception {
		dao.save("MtsConfigRecordMapper.addConfigRecord", mcr);
	}




	/**查询标化总表列表
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listConfig(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsConfigRecordMapper.findMtsConfig", page);
	}




	/**查询字段表
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listTitle(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsConfigRecordMapper.findConfigTitle", page);
	}




	/**查询NLP结果表列表
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listRecord(int T_ID) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsConfigRecordMapper.findConfigRecord", T_ID);
	}




	/**查询标化详细表列表
	 * @param pd
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public String listDetail(int T_ID) throws Exception {
		List<String> list=(List<String>) dao.findForList("MtsConfigRecordMapper.findConfigDetail", T_ID);
		return list.get(0) ;
	}





	/**自动标化界面分页查询
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listZDRecord(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsConfigRecordMapper.findZDRecordlistPage", page);
	}





	/**无法标化列表查询
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listWFBH() throws Exception {	
		
		return(List<PageData>) dao.findForList("MtsConfigRecordMapper.findWFBH", null);
	}




	/**术语类型列表查询
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listTermType() throws Exception {
		
		return(List<PageData>) dao.findForList("MtsConfigRecordMapper.findTermType", null);
	}




	/**
	 * 修改detail表中的nlp内容
	 */
	public void editDetail(Page page) throws Exception {
		dao.update("MtsConfigRecordMapper.editDetail", page);
		
	}


	/**
	 * 删除t_id对应在record中的记录
	 */
	public void deleteRecord(int T_ID) throws Exception {
		
		dao.delete("MtsConfigRecordMapper.deleteRecord", T_ID);
	}




	/**
	 * 删除Pt_id对应在record中的记录
	 */
	public void deleteRecordByPt(String PT_ID) throws Exception {
		dao.delete("MtsConfigRecordMapper.deleteRecordByPt", PT_ID);
		
	}




	/**
	 * 删除Pt_id对应在Detail中的记录
	 */
	public void deleteDetailByPt(String PT_ID) throws Exception {
		dao.delete("MtsConfigRecordMapper.deleteDetailByPt", PT_ID);
		
	}




	/**
	 * 删除Pt_id对应在Config中的记录
	 */
	public void deleteConfigByPt(String PT_ID) throws Exception {
		dao.delete("MtsConfigRecordMapper.deleteConfigByPt", PT_ID);
		
	}




	/**
	 * 查询数据源
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findDataSource() throws Exception {
		return(List<PageData>) dao.findForList("MtsConfigRecordMapper.findDataSource", null);
	}




	/**
	 * 添加mts_config表数据
	 */
	public void addConfig(MtsConfig mc) throws Exception {
		dao.save("MtsConfigRecordMapper.addConfig", mc);
		
	}




	/**
	 * 添加mts_config_Detail表数据
	 */
	public void addConfigDetail(MtsConfigDetail mcd) throws Exception {
		dao.save("MtsConfigRecordMapper.addConfigDetail", mcd);
		
	}




	/**
	 * 修改mts_config_Detail表数据
	 */
	public void editConfigDetail(MtsConfigDetail mcd) throws Exception {
		dao.update("MtsConfigRecordMapper.editConfigDetail", mcd);
		
	}




	/**
	 * 按批次查询mts_config_Detail表数据
	 */
	@SuppressWarnings("unchecked")
	public List<MtsConfigDetail> findByPT(String PT_ID) throws Exception {
		return (List<MtsConfigDetail>) dao.findForList("MtsConfigRecordMapper.findDetailByPT",PT_ID);
	}
	
	


}
