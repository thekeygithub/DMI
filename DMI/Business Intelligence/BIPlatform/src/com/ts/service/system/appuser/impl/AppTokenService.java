package com.ts.service.system.appuser.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.appuser.AppTokenManager;
import com.ts.util.PageData;


/** 
 * 说明： TokenManage
 * 创建人：李世博
 * 创建时间：2016-09-13
 * @version
 */
@Service("appTokenService")
public class AppTokenService implements AppTokenManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AppTokenMapper.save", pd);
	}	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AppTokenMapper.delete", pd);
	}
	/**通过Token_Info 删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteTokenInfo(PageData pd)throws Exception{
		dao.delete("AppTokenMapper.deleteTokenInfo", pd);
	}
	/**通过Validity 删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteValidity(PageData pd)throws Exception{
		dao.delete("AppTokenMapper.deleteValidity", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AppTokenMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppTokenMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AppTokenMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppTokenMapper.findById", pd);
	}
	/**通过Userid获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppTokenMapper.findByUserId", pd);
	}
	/**通过刷新令牌获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByToken(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppTokenMapper.findByToken", pd);

	}
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

