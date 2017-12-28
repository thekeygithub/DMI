package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_bill;
import com.ts.entity.Page;
import com.ts.service.pay.PbillManager;
import com.ts.util.PageData;

/** 
 * 说明： pay
 * 创建人：
 * 创建时间：2017-03-29
 * @version
 */
@Service("pbillService")
public class PbillService implements PbillManager{

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(P_bill pd)throws Exception{
		dao.save("PbillMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PbillMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PbillMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PbillMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PbillMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PbillMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PbillMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public Integer findUserID() throws Exception {
		return (Integer)dao.findForObject("PbillMapper.findUserID", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchBillListAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PbillMapper.billListAll", pd);
	}
	
}