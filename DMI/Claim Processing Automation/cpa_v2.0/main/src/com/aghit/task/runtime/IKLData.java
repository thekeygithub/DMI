package com.aghit.task.runtime;

import com.aghit.task.manager.TreeMgr;

/**
 * 获取知识接口
 */
public interface IKLData {
	
	/**
	 * 初始化
	 */
	public void init();
		
	/**
	 * 耗材分类
	 * @return
	 */
	public String getCsumCateSql();
	
	/**
	 * ATC西药分类
	 * @return
	 */
	public String getAtcWestSql();
	
	/**
	 * ATC中药分类
	 * @return
	 */
	public String getAtcChinaSql();
	
	/**
	 * ATC西药分类树结构
	 * @return
	 */
	public String getAtcWestTreeSQL();
	
	/**
	 * 诊断树
	 * @return
	 */
	public TreeMgr getDiagTree();
	
	/**
	 * ATC西药树
	 * @return
	 */
	public TreeMgr getDrugWestTree();
	
	/**
	 * Atc中药树
	 * @return
	 */
	public TreeMgr getDrugChinaTree();
	
	/**
	 * 诊疗树
	 * @return
	 */
	public TreeMgr getTreatTree();
	
	/**
	 * 耗材树
	 * @return
	 */
	public TreeMgr getCsumTree();
	
	/**
	 * 药品的通用名信息
	 * @return
	 */
	public String getDrugGenNameSql();
	

}
