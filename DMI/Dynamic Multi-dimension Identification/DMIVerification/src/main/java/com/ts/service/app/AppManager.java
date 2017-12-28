package com.ts.service.app;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 说明： App数据接口
 * 创建人：李世博
 * 创建时间：2016-09-01
 * @version
 */
public interface AppManager{

	
	/**
	 * 查询app
	 * 
	 * */
	public List<PageData> json(PageData pd)throws Exception;
	
	/**
	 * 分页查询app
	 * 
	 * */
	public List<PageData> jsonPage(Page page)throws Exception;
	
	/**获取药品总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDrugCount(String value)throws Exception;
	/**获取导诊总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getGuideCount(String value)throws Exception;
	/**获取医生总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDoctorCount(String value)throws Exception;
	/**获取医院总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getHospitalCount(String value)throws Exception;
	/**获取科室总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDepartmentCount(String value)throws Exception;
	
}

