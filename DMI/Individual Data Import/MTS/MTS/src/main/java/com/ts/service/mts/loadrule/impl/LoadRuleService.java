package com.ts.service.mts.loadrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.loadrule.LoadRuleManager;
import com.ts.util.PageData;


/** 系统用户
 * @author 
 * 修改时间：2015.11.2
 */
@Service("loadRuleService")
public class LoadRuleService implements LoadRuleManager{
	
	
	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listLoadRules(Page page)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("MtsLoadRuleDetailMapper.loadrulelistPage", page);
		return list;
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception 
	 */
	public void saveRule(LoadRule lr) throws Exception{
		dao.save("MtsLoadRuleDetailMapper.saveRule", lr);
	}
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;



	@Override
	public List<MtsDataType> listAllMtsDataType(PageData pd) throws Exception {
		List<MtsDataType> list = (List<MtsDataType>)dao.findForList("MtsDataTypeMapper.listAllMtsDataType", pd);
		return list;
	}

	@Override
	public String maxRule() throws Exception {
		return (String) dao.findForObject("MtsLoadRuleDetailMapper.maxRule", null);
	}

	@Override
	public LoadRule findRuleById(String ruleid) throws Exception {
		return (LoadRule) dao.findForObject("MtsLoadRuleDetailMapper.findRuleById", ruleid);
	}

	@Override
	public void editRule(LoadRule lr) throws Exception {
		dao.update("MtsLoadRuleDetailMapper.editRule", lr);
	}

	/**
	 * 
	 * @方法名称: deleteRule
	 * @功能描述: 删除加载规则
	 * @作者:李巍
	 * @创建时间:2016年11月10日 上午10:51:05
	 * @param ruleid
	 * @throws Exception
	 * @see com.ts.service.mts.loadrule.LoadRuleManager#deleteRule(java.lang.String)
	 */
	@Override
	public void deleteRule(String ruleid) throws Exception {
		dao.delete("MtsLoadRuleDetailMapper.deleteRule", ruleid);
	}
	
}
