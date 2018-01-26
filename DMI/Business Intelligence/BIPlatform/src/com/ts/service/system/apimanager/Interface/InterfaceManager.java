package com.ts.service.system.apimanager.Interface;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;


/** 接口业务列表接口类
 * @author 
 * 修改时间：2016.10.11
 */
public interface InterfaceManager {
	

	
	/**接口业务列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageInterface(Page page)throws Exception;
	
	
	/**保存
	 * @param pd
	 * @throws Exception
	 */
	public void saveI(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteI(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editI(PageData pd)throws Exception;
		
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllInterface(PageData pd)throws Exception;
	/**删除时检查
	 * @param IN_IDS
	 * @throws Exception
	 */
	public boolean findByAllInId(String[] IN_IDS)throws Exception;
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllI(String[] IN_IDS)throws Exception;
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
	public PageData getInterfaceCount(String value)throws Exception;
	
}

