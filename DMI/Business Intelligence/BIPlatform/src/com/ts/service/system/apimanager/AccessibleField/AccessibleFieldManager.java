package com.ts.service.system.apimanager.AccessibleField;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;


/** 接口业务列表接口类
 * @author 
 * 修改时间：2016.10.11
 */
public interface AccessibleFieldManager {
	

	
	/**接口业务列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageAccess(Page page)throws Exception;
	
	
	/**接口业务列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageAccess()throws Exception;
	
	
	/**保存
	 * @param pd
	 * @throws Exception
	 */
	public void saveA(PageData pd)throws Exception;
	
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
		
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllAccess(PageData pd)throws Exception;
	/**获取全部分组数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listYPAccess(PageData pd)throws Exception;
	/**获取ZTree数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public List<PageData> listZtreeAccess(PageData pd)throws Exception;
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllA(String[] IN_IDS)throws Exception;
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
	
	/**通过SYS_INTERFACE表中type字段获取COLUMN_NAME数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<String> findByType(PageData pd)throws Exception;
	
	/**通过SYS_INTERFACE表中type字段获取COLUMN_DISC数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<String> findByBusinessName(PageData pd)throws Exception;
	
	/**
	 * @描述：获取该用户角色的可访问业务权限
	 * @作者：SZ
	 * @时间：2016年11月8日 下午1:48:42
	 * @param value
	 * @return
	 */
	public List<String> getSysInterface(PageData pd)throws Exception;
}

