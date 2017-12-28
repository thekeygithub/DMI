package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataType;
import com.ts.util.PageData;



public interface DataTypeManger {
	
	public String findByCode(String code) throws Exception;
	
	public List<MtsDataType> listAllDataType() throws Exception;
	
	public List<MtsDataType> listClassDataType(String classcode) throws Exception;
	
	public List<MtsDataType> listClassDataTypeById(String classid) throws Exception;
	
	
	/**
	 * 获取相应聚类下的所有标准化类型带分页
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public List<PageData> DataTypelistPage(Page page) throws Exception;
	
	/**
	 * 添加标化类型
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public void addDataType(MtsDataType mdt) throws Exception;
	
	/**
	 * 修改标化类型
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public void editDataType(MtsDataType mdt) throws Exception;
	
	/**
	 * 删除标化类型
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public void deleteDataType(String typeid) throws Exception;
	
	/**
	 * 查询DATA_TYPE_ID最大值
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public String maxDataType() throws Exception;
	/**
	 * 通过ID获取数据
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public MtsDataType findById(String tyid) throws Exception;
	
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
	public MtsDataType codeByCode(String code) throws Exception;
	
	/**
	 * 判断标化名是否存在
	 * @param classcode
	 * @return
	 * @throws Exception
	 */
	public MtsDataType nameByName(String name) throws Exception;
	
}
