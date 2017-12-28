package com.ts.service.app.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAPI;
import com.ts.entity.Page;
import com.ts.service.app.AppManager;
import com.ts.util.PageData;

/** 
 * 说明：App接口方法
 * 创建人：
 * 创建时间：2016-04-29
 * @version
 */
@Service("appService")
public class AppService implements AppManager{

	@Resource(name = "daoSupportAPI")
	private DaoSupportAPI dao;

	/**
	 * 查询(全部)app
	 * 
	 * **/
	@SuppressWarnings("unchecked")
	public List<PageData> json(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("AppMapper.json", pd);
	}
	
	/**
	 * 查询(分页)app
	 * 
	 * **/
	@SuppressWarnings("unchecked")
	public List<PageData> jsonPage(Page page) throws Exception {
		return (List<PageData>)dao.findForList("AppMapper.jsonlistPage", page);
	}
	
	/**获取药品总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDrugCount(String value) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("AppMapper.getAppUserCount", value);
	}

	/**获取导诊总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getGuideCount(String value) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("AppMapper.getAppUserCount", value);
	}

	/**获取医生总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDoctorCount(String value) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("AppMapper.getDoctorCount", value);
	}

	/**获取医院总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getHospitalCount(String value) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("AppMapper.getHospitalCount", value);
	}

	/**获取科室总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDepartmentCount(String value) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("AppMapper.getDepartmentCount", value);
	}

}

