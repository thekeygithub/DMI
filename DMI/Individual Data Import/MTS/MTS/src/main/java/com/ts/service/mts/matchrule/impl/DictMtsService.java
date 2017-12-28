package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsDict;
import com.ts.service.mts.matchrule.MtsDictManger;
import com.ts.util.PageData;

@Service("DictMtsService")
public class DictMtsService implements MtsDictManger{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	@SuppressWarnings("unchecked")
	public List<MtsDict> listAllByClass(Page page) throws Exception {
		return (List<MtsDict>) dao.findForList("MtsDictMapper.listAllByClass", page);
	}

	@Override
	public List<MtsDict> listKeyRuleByClass(String cl) throws Exception {
		return (List<MtsDict>) dao.findForList("MtsDictMapper.listKeyRuleByClass", cl);
	}

	@Override
	public List<MtsDict> listValueRuleByClass(String cl) throws Exception {
		return (List<MtsDict>) dao.findForList("MtsDictMapper.listValueRuleByClass", cl);
	}

	@Override
	public List<PageData> findMtsDictByPd(PageData pd) throws Exception {
		Object object = dao.findForList("MtsDictMapper.findMtsDictByPd", pd);
		return (List<PageData>)object;
	}

	/**
	 * 
	 * @方法名称: findMtsDictByRuleID
	 * @功能描述: 根据规则ID查询KEY规则字典列表
	 * @作者:李巍
	 * @创建时间:2017年3月21日 下午3:48:39
	 * @param ruleid
	 * @throws Exception
	 * @see com.ts.service.mts.matchrule.MtsDictManger#findMtsDictByRuleID(java.lang.String)
	 */
	@Override
	public List<PageData> findMtsKeyDictByRuleID(String ruleid) throws Exception {
		Object object = dao.findForList("MtsRuleDictMapper.findMtsKeyDictByRuleID", ruleid);
		return (List<PageData>)object;
	}
	
	/**
	 * 
	 * @方法名称: findMtsValueDictByRuleID
	 * @功能描述: 根据规则ID查询VALUE规则字典列表
	 * @作者:李巍
	 * @创建时间:2017年3月21日 下午4:18:48
	 * @param ruleid
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.matchrule.MtsDictManger#findMtsValueDictByRuleID(java.lang.String)
	 */
	@Override
	public List<PageData> findMtsValueDictByRuleID(String ruleid) throws Exception {
		Object object = dao.findForList("MtsRuleDictMapper.findMtsValueDictByRuleID", ruleid);
		return (List<PageData>)object;
	}
	
	/**
	 * 
	 * @方法名称: maxRuleDict
	 * @功能描述: 查询MTS_RULE_DICT 最大主键
	 * @作者:李巍
	 * @创建时间:2017年3月21日 下午4:07:57
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.matchrule.MtsDictManger#maxRuleDict()
	 */
	@Override
	public String maxRuleDict() throws Exception {
		return (String) dao.findForObject("MtsRuleDictMapper.maxRuleDict", null);
	}
	
	/**
	 * 
	 * @方法名称: saveRuleDict
	 * @功能描述: 添加中间表数据
	 * @作者:李巍
	 * @创建时间:2017年3月21日 下午4:12:20
	 * @param pd
	 * @throws Exception void
	 */
	@Override
	public void saveRuleDict(PageData pd) throws Exception{
		dao.save("MtsRuleDictMapper.saveRuleDict", pd);
	}
	
	/**
	 * 
	 * @方法名称: deleteMtsRuleDict
	 * @功能描述: 删除中间表数据
	 * @作者:李巍
	 * @创建时间:2017年3月22日 下午1:11:07
	 * @param ruleid
	 * @throws Exception
	 * @see com.ts.service.mts.matchrule.MtsDictManger#deleteMtsRuleDict(java.lang.String)
	 */
	@Override
	public void deleteMtsRuleDict(String ruleid) throws Exception {
		dao.delete("MtsRuleDictMapper.delMtsRuleDict", ruleid);
	}
}
