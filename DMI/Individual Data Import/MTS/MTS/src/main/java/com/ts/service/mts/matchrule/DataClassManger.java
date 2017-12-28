package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataClass;
import com.ts.entity.mts.MtsDataType;
import com.ts.util.PageData;



public interface DataClassManger {
	
	/**
	 * 普通查询所有
	 * @return
	 * @throws Exception
	 */
	public List<MtsDataClass> listAllDataClass() throws Exception;
	/**
	 * 带分页查询所有
	 * @return
	 * @throws Exception
	 */
	public List<PageData> DataClasslistPage(Page page) throws Exception;
	
	/**
	 * 新增聚类
	 * @return
	 * @throws Exception
	 */
	public void addDataClass(MtsDataClass mdc) throws Exception;
	
	/**
	 * 修改聚类
	 * @return
	 * @throws Exception
	 */
	public void editDataClass(MtsDataClass mdc) throws Exception;
	
	/**
	 * 删除聚类
	 * @return
	 * @throws Exception
	 */
	public void deleteDataClass(String datatype) throws Exception;
	
	/**
	 * 查询DATA_CLASS_ID最大值
	 * @return
	 * @throws Exception
	 */
	public String maxDataClass() throws Exception;
	
	/**
	 * 根据id获取聚类
	 * @return
	 * @throws Exception
	 */
	public MtsDataClass dataClassById(String typeid) throws Exception;
	
	/**
	 * 查询MEM_DATA_CODE最大值
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public String maxDataCode() throws Exception;
	
	/**
	 * 判断标化代码是否存在
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public MtsDataClass codeByCode(String code) throws Exception;
	
	/**
	 * 判断标化名是否存在
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public MtsDataClass nameByName(String name) throws Exception;

}
