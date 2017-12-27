package com.aghit.task.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.aghit.base.BaseService;
import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.ProperUsageKL;
import com.aghit.task.common.service.CacheService;
import com.aghit.task.manager.ProjectMgr;
import com.aghit.task.manager.TreeMgr;
import com.aghit.task.runtime.KLDataService;

/**
 * 缓存Impl
 */
public class CacheServiceImpl extends BaseService implements CacheService{
	
	@Resource(name = "projectMgr")
	private ProjectMgr projectMgr;	// 方案服务类
	
	// 合理用药知识库缓存
	// 主键为【逻辑ID_用法_DrugId】
	private static Map<String,List<ProperUsageKL>> proUsgKlCache;
	
	// 数据字典表缓存
	// 主键为类型
	private static Map<String,Map<Integer,String>> codeDict; 
	
	//所有规则集合
	private static List<AbstractRule> rules;
	
	//诊断树
	private static TreeMgr diagTree = null; // 诊断树
	
	//方案规则关系表
	private static Map<Long,List<Long>> SernarioRuleRel;
	
	@Override
	public void initCache() {

		log.info("********* CPA引擎，初始化缓存开始。***********");
		
		initDiagTree();         //初始化诊断树
		
		initCodeDict();			// 初始化数据字典
		
		intiRule();             //初始化规则集合
		
		initSenarioRuleRel();   //初始化方案规则集合
		
		log.info("********* CPA引擎，初始化缓存结束。***********");
		
	}
	
	//加载诊断树
	private void initDiagTree(){
		diagTree = KLDataService.getDiagTree();
		try{
		    diagTree.init();
		}catch (Exception e) {
			log.error("加载诊断树缓存失败，" + e.getMessage());
		}finally{
			log.info("已完成加载诊断树");
		}

	}

	//加载规则
    private void intiRule(){
		//加载规则
		try {
			rules = getProjectMgr().initRules();
		} catch (Exception e) {
			log.error("加载规则缓存失败，" + e.getMessage());
		}finally{
			log.info("已完成规则缓存，记录数：" + ((null == rules)?0:rules.size()));
		}
    }
    
    /**
     * 加载方案规则关系
     */
    private void initSenarioRuleRel(){
    	try {
			SernarioRuleRel=getProjectMgr().initScenarioRuleRel();
		} catch (Exception e) {
			log.error("加载方案规则关系缓存失败，" + e.getMessage());
		}finally{
			log.info("已完成方案规则关系缓存，记录数：" + SernarioRuleRel.size());
		}
    }

	/**
	 * 加载有用的代码表缓存
	 */
	private void initCodeDict() {
		
		log.info("加载代码表缓存");
		// 数据库查询结果暂存
		List<Map<String,Object>> ls = new ArrayList<Map<String,Object>>();//初始化
		codeDict = new HashMap<String, Map<Integer,String>>();
		try {
//			ls = getJdbcService().findForList(SQL2, null);
//			Iterator<Map<String,Object>> it = ls.iterator();
			
//			// 模拟年龄的代码表
//			Map<Integer,String> mp = new HashMap<Integer, String>();
//			mp.put(1, "1-12");// 婴儿1-12月
//			mp.put(2, "13-156");// 儿童1-12岁
//			mp.put(3, "157-732");//成人：13-60岁
//			mp.put(4, "733-1200");//老年人：61-100岁
//			
//			codeDict.put("nl", mp);
		}catch (Exception e) {
			log.error("加载代码表缓存失败，" + e.getMessage());
		}finally{
			log.info("已完成加载代码表缓存，记录数：" + ls.size());
		}
	}
	
	public static Map<String, List<ProperUsageKL>> getProUsgKlCache() {
		return proUsgKlCache;
	}
	public static Map<String, Map<Integer, String>> getCodeDict() {
		return codeDict;
	}
	public static TreeMgr getDiagTree() {
		return diagTree;
	}
	public static void setDiagTree(TreeMgr diagTree) {
		CacheServiceImpl.diagTree = diagTree;
	}
	public static List<AbstractRule> getRules() {
		return rules;
	}
	public static void setRules(List<AbstractRule> rules) {
		CacheServiceImpl.rules = rules;
	}
	public static Map<Long, List<Long>> getSernarioRuleRel() {
		return SernarioRuleRel;
	}
	public static void setSernarioRuleRel(
			Map<Long, List<Long>> sernarioRuleRel) {
		SernarioRuleRel = sernarioRuleRel;
	}

	public ProjectMgr getProjectMgr() {
		return projectMgr;
	}

	public void setProjectMgr(ProjectMgr projectMgr) {
		this.projectMgr = projectMgr;
	}	
	
}
