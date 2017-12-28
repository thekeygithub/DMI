package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_bill_item;
import com.ts.entity.Page;
import com.ts.service.pay.PbillitemManager;
import com.ts.util.PageData;

/** 
 * 说明： pay1
 * 创建人：
 * 创建时间：2017-03-29
 * @version
 */
@Service("pbillitemService")
public class PbillitemService implements PbillitemManager{

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(P_bill_item pd)throws Exception{
		dao.save("PbillitemMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PbillitemMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PbillitemMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PbillitemMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PbillitemMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PbillitemMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PbillitemMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

