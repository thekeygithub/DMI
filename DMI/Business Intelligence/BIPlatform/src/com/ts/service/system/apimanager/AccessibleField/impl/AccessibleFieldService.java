package com.ts.service.system.apimanager.AccessibleField.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.AccessibleField.AccessibleFieldManager;
import com.ts.util.PageData;


/**类名称：AccessibleFieldService
 * @author 
 * 修改时间：2016年10月18日
 */
@Service("accessibleFieldService")
public class AccessibleFieldService implements AccessibleFieldManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	/**接口列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageAccess(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AccessibleFieldMapper.accesslistPage", page);
	}
	
	/**
	 * 获取接口列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageAccess() throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("InterfaceMapper.listAllInterface");
	}
		
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveA(PageData pd)throws Exception{
		dao.save("AccessibleFieldMapper.saveA", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteA(PageData pd)throws Exception{
		dao.delete("AccessibleFieldMapper.deleteA", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editA(PageData pd)throws Exception{
		dao.update("AccessibleFieldMapper.editA", pd);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllAccess(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AccessibleFieldMapper.listAllAccess", pd);
	}
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllA(String[] DR_IDS)throws Exception{
		dao.delete("ColRuleMapper.deleteAllDRID", DR_IDS);
		dao.delete("RelationMapper.deleteAllDRID", DR_IDS);
		dao.delete("AccessibleFieldMapper.deleteAllA", DR_IDS);
		dao.delete("PayRuleMapper.deleteAllByDRID", DR_IDS);
	}
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AccessibleFieldMapper.findById", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getAccessCount(String value)throws Exception{
		return (PageData)dao.findForObject("AccessibleFieldMapper.getAccessCount", value);
	}
	/**字段列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listYPAccess(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AccessibleFieldMapper.accesslistYP", pd);
	}
	/**Ztree列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listZtreeAccess(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AccessibleFieldMapper.accesslistzTree", pd);
	}
	/**
	 * 通过SYS_INTERFACE表中type字段获取COLUMN_NAME数据
	 */
	@SuppressWarnings("unchecked")
	public List<String> findByType(PageData pd) throws Exception {
		return (List<String>) dao.findForList("AccessibleFieldMapper.accesslistByType", pd);
	}
	
	/**
	 * 通过SYS_INTERFACE表中type字段获取COLUMN_DISC数据
	 */
	@SuppressWarnings("unchecked")
	public List<String> findByBusinessName(PageData pd) throws Exception {
		return (List<String>) dao.findForList("AccessibleFieldMapper.accesslistByBusinessName", pd);
	}
	
	/**
	 * @描述：获取该用户角色的可访问业务权限
	 * @作者：SZ
	 * @时间：2016年11月8日 下午1:48:42
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSysInterface(PageData pd) throws Exception {
		return (List<String>) dao.findForList("AccessibleFieldMapper.getSysInterface",pd);
	}
}

