package com.ts.service.system.apimanager.RecycleLog.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.RecycleLog.AppRecycleLogManager;
import com.ts.util.PageData;


/** 
 * 说明： TokenManage
 * 创建人：
 * 创建时间：2016-09-13
 * @version
 */
@Service("appRecycleLogService")
public class AppRecycleLogService implements AppRecycleLogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AppRecycleLogMapper.save", pd);
	}	

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AppRecycleLogMapper.edit", pd);
	}
	
	/**日志列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> recyclelistPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppRecycleLogMapper.recyclelistPage", page);
	}
	/**全部日志
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllRecycle(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppRecycleLogMapper.listAllRecycle", pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppRecycleLogMapper.findById", pd);
	}
	/**通过Userid获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppRecycleLogMapper.findByUserId", pd);
	}
	/**通过刷新令牌获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppRecycleLogMapper.findByCode", pd);

	}
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getRecycleCount(String value)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getRecycleCount", value);
	}
}

