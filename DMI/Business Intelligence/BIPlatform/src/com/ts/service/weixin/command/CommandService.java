package com.ts.service.weixin.command;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 类名称：CommandService
 * 创建人：
 * 创建时间：2015-05-09
 */
@Service("commandService")
public class CommandService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CommandMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CommandMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CommandMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CommandMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CommandMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CommandMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CommandMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**匹配关键词
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByKw(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CommandMapper.findByKw", pd);
	}
}

