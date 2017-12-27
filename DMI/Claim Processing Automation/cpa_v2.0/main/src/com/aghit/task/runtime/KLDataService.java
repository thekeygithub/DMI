package com.aghit.task.runtime;

import com.aghit.task.manager.TreeMgr;
import com.aghit.utils.SpringBeanUtil;

/**
 * 获取知识接口
 */
public class KLDataService {
	
	/**
	 * 耗材分类sql
	 * @return
	 */
	public static String getCsumCateSql(){
		String sql = getKLData().getCsumCateSql();
		return sql;
	}
	
	/**
	 * ATC西药分类sql
	 * @return
	 */
	public static String getAtcWestSql(){
		String sql = getKLData().getAtcWestSql();
	    return sql;
	}
	
	/**
	 * ATC中药分类sql
	 * @return
	 */
	public static String getAtcChinaSql(){
		String sql = getKLData().getAtcChinaSql();
		return sql;
	}
	
	/**
	 * ATC西药分类树sql
	 * @return
	 */
	public static String getAtcWestTreeSql(){
		String sql = getKLData().getAtcWestTreeSQL();
		return sql;
	}
	
	/**
	 * 诊断分类树
	 * @return
	 */
	public static TreeMgr getDiagTree(){
		return getKLData().getDiagTree();
	}
	
	/**
	 * ATC西药分类树
	 * @return
	 */
	public static TreeMgr getDrugWestTree(){
		return getKLData().getDrugWestTree();
	}
	
	/**
	 * ATC中药分类树
	 * @return
	 */
	public static TreeMgr getDrugChianTree(){
		return getKLData().getDrugChinaTree();
	}
	
	/**
	 * 诊疗树
	 * @return
	 */
	public static TreeMgr getTreatTree(){
		return getKLData().getTreatTree();
	}
	
	/**
	 * 耗材树
	 * @return
	 */
	public static TreeMgr getCsumTree(){
		return getKLData().getCsumTree();
	}
	
	/**
	 * 药品的通用名信息
	 * @return
	 */
	public static String getDrugGenNameSql(){
		return getKLData().getDrugGenNameSql();
	}
	
	/**
	 * 
	 * @return
	 */
	private static IKLData getKLData(){
		return SpringBeanUtil.getSpringBean("kldataService", IKLData.class);
	}
	

}
