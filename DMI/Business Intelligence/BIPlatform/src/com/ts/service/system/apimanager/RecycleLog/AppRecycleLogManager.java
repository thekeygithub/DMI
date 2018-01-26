package com.ts.service.system.apimanager.RecycleLog;



import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 说明： AppRecycleLogMapper接口
 * 创建人：
 * 创建时间：2016-09-13
 * @version
 */
public interface AppRecycleLogManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**日志列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> recyclelistPage(Page page)throws Exception;
	/**全部日志
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllRecycle(PageData pd)throws Exception;
		
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	/**通过用户id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserId(PageData pd)throws Exception;
	/**通过Code获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception;
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getRecycleCount(String value)throws Exception;
}

