package com.ts.service.mts.matchrule;

import com.ts.entity.Page;





public interface DataRelationManger {
	
	/**
	 * 根据聚类id查询
	 * @return
	 * @throws Exception
	 */
	public Integer relationCount(String claid) throws Exception;
	
	
	/**
	 * 新增关系
	 * @return
	 * @throws Exception
	 */
	public void addDataRelation(Page pg) throws Exception;
	
	
	
	/**
	 * 删除关系
	 * @return
	 * @throws Exception
	 */
	public void deleteDataRelation(String datatype) throws Exception;
	
	/**
	 * 查询RELATION_ID最大值
	 * @return
	 * @throws Exception
	 */
	public String maxDataRelation() throws Exception;
	
	/**
	 * 根据标化id查询聚类id
	 * @return
	 * @throws Exception
	 */
	public String findRelationid(String datatype) throws Exception;

}
