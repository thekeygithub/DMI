package com.ts.service.system.apimanager.ColRule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.ColRule.ColRuleManager;
import com.ts.util.PageData;


/**类名称：ColRuleService
 * @author Lee
 * 修改时间：2016年12月1日
 */
@Service("colRuleService")
public class ColRuleService implements ColRuleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	/**接口列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageColRule(Page page)throws Exception{
		return (List<PageData>) dao.findForList("ColRuleMapper.colRulelistPage", page);
	}
		
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ColRuleMapper.saveA", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteA(PageData pd)throws Exception{
		dao.delete("ColRuleMapper.deleteA", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editA(PageData pd)throws Exception{
		dao.update("ColRuleMapper.editA", pd);
	}
		
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllA(String[] IDS)throws Exception{
		dao.delete("ColRuleMapper.deleteAllA", IDS);
	}
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ColRuleMapper.findById", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getAccessCount(String value)throws Exception{
		return (PageData)dao.findForObject("ColRuleMapper.getAccessCount", value);
	}

}

