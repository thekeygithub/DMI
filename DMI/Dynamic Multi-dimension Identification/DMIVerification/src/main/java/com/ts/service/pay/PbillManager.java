package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_bill;
import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 说明： pay接口
 * 创建人：
 * 创建时间：2017-03-29
 * @version
 */
public interface PbillManager{
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(P_bill pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	public Integer findUserID() throws Exception;

	public List<PageData> searchBillListAll(PageData pd) throws Exception;
}