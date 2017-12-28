package com.ts.service.mts.loadrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsDataType;
import com.ts.util.PageData;

/**
 * 用户接口类
 * 
 * @author 修改时间：2015.11.2
 */
public interface LoadRuleManager {

	/**
	 * 加载规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listLoadRules(Page page) throws Exception;

	/**
	 * 
	 * @方法名称: listAllMtsDataType
	 * @功能描述: 获取全部数据标准化类型
	 * @作者:李巍
	 * @创建时间:2016年10月13日 下午12:20:33
	 * @param pd
	 * @return
	 * @throws Exception
	 *             List<MtsDataType>
	 */
	public List<MtsDataType> listAllMtsDataType(PageData pd) throws Exception;

	public void saveRule(LoadRule lr) throws Exception;

	public String maxRule() throws Exception;

	public LoadRule findRuleById(String ruleid) throws Exception;

	public void editRule(LoadRule lr) throws Exception;

	public void deleteRule(String ruleid) throws Exception;

}
