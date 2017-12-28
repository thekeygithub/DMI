package com.ts.service.mts.mtsdict.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.mtsdict.MtsDictManager;
import com.ts.util.PageData;


/** 系统用户
 * @author 
 * 修改时间：2015.11.2
 */
@Service("mtsDictService")
public class MtsDictService implements MtsDictManager{
	
	
	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listMtsDict(Page page)throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("MtsDictMapper.mtsDictlistPage", page);
		return list;
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception 
	 */
	public void saveDict(PageData pd) throws Exception{
		dao.save("MtsDictMapper.saveDict", pd);
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
		return (String) dao.findForObject("MtsDictMapper.maxRule", null);
	}

	@Override
	public PageData findDictById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MtsDictMapper.findDictById", pd);
	}

	@Override
	public void editDict(PageData pd) throws Exception {
		dao.update("MtsDictMapper.editDict", pd);
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
	public void deleteMtsDict(String did) throws Exception {
		dao.delete("MtsDictMapper.delMtsDict", did);
	}
	
	
	@Override
	public PageData selByName(String name) throws Exception {
		Object object = dao.findForObject("MtsDictMapper.selByName",  name);
		return (PageData)object;
	}
	

}
