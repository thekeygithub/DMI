package com.ts.service.system.apimanager.PayRule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;


/** 接口业务列表接口类
 * @author 
 * 修改时间：2016.10.11
 */
public interface PayRuleManager {
	

	
	/**接口业务列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPagePay(Page page)throws Exception;
	
	
	/**保存
	 * @param pd
	 * @throws Exception
	 */
	public void savePay(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deletePay(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editPay(PageData pd)throws Exception;
		
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllPay(PageData pd)throws Exception;
	/**删除时检查
	 * @param IN_IDS
	 * @throws Exception
	 */
	public boolean findByAll(String[] IN_IDS)throws Exception;
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] IN_IDS)throws Exception;
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
	public PageData getCount(String value)throws Exception;
	
	/**获取所有参数 及参数对应规则
	 * @throws Exception
	 */
	public List<PageData> getDataRuleAll()throws Exception;


	/**
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ParamCheckIdList(String drId)throws Exception;


	/**查询参数校验关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findRelationByDrCheckId(PageData pd)throws Exception;

	/**添加关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean saveRelation(PageData pd)throws Exception;
	
	/**删除关系
	 * @param pd
	 * @throws Exception
	 */
	public boolean deleteRelation(PageData pd) throws Exception;
	
}

