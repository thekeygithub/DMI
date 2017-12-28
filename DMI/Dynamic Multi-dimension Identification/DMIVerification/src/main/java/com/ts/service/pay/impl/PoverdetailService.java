package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_over_detail;
import com.ts.entity.Page;
import com.ts.util.PageData;
import com.ts.service.pay.PoverdetailManager;

/** 
 * 说明： 超限明细表
 * 创建人：
 * 创建时间：2017-03-28
 * @version
 */
@Service("poverdetailService")
public class PoverdetailService implements PoverdetailManager{

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(P_over_detail pd)throws Exception{
		dao.save("PoverdetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PoverdetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PoverdetailMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PoverdetailMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PoverdetailMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PoverdetailMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PoverdetailMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

