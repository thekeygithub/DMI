package com.ts.service.system.apimanager.ColRule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;


/** 接口业务列表接口类
 * @author 
 * 修改时间：2016.10.11
 */
public interface ColRuleManager {
	

	
	/**接口业务列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageColRule(Page page)throws Exception;
	
	/**保存
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteA(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editA(PageData pd)throws Exception;

	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllA(String[] IDS)throws Exception;
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getAccessCount(String value)throws Exception;

}

