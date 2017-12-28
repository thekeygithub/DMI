package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_insu_info;
import com.ts.service.pay.PinsuinfoManager;
import com.ts.util.PageData;

@Service("pinsuinfoService")
public class PinsuinfoService implements PinsuinfoManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(P_insu_info pd) throws Exception {
		dao.save("PinsuinfoMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PinsuinfoMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PinsuinfoMapper.findById", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageData findByUser(PageData pd) throws Exception {
		PageData param = new PageData();
		List<PageData> list = (List<PageData>)dao.findForList("PinsuinfoMapper.findByUser", pd);
		if(list!=null && list.size()>0){
			param = (PageData)list.get(0);
		}
		return param;
	}

}