package com.aghit.task.manager.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.algorithm.domain.rule.klbase.KlRule;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.common.service.KlDataService;
import com.aghit.task.manager.ProjectMgr;
import com.aghit.task.util.Constant;
import com.aghit.utils.SpringBeanUtil;

/**
 * 知识库规则
 */
public class KlRuleMgr extends RuleMgr {
    //日志
	private Logger log = Logger.getLogger(KlRuleMgr.class);
	
	//单例模式
	private static KlRuleMgr mgr = new KlRuleMgr();

	private KlRuleMgr() {}

	public static KlRuleMgr getInstance() {
		return mgr;
	}
	
	@Override
	public List<AbstractRule> initRule() throws Exception {
		
		List<AbstractRule> rules = new ArrayList<AbstractRule>();
		
		//根据规则ID集合查找对应的知识集合
		KlDataService svc = SpringBeanUtil.getSpringBean("knowledgeDataService", KlDataService.class);
		List<KlDef> klDefs = svc.findKlDefByRuleType(Constant.RULE_CLASS_KL);
		if (null == klDefs || 0 == klDefs.size()){
			return rules;
		}
		for(KlDef def : klDefs){
			KlRule rule = new KlRule();
			rule.checkType=changeType(def.getObj_elem_type_id());
			rule.ruleId = def.getRule_id(); // 规则ID
			rule.setExps(new ArrayList<KLElemExpression>());
			rule.ruleName = def.getRule_name(); // 规则名称
			rule.cycleFlg = def.getCycle_flg();// 取得周期标志
			rule.cycleStart = def.getCycle_start();
			rule.cycleEnd = def.getCycle_end();
			String[] medicalType = def.getMedical_type().split(",");
			Set<Integer> medical_type = new HashSet<Integer>();
			for (int w = 0; w < medicalType.length; w++) {
				medical_type.add(Integer.parseInt(medicalType[w]));
			}
			rule.medicalType = medical_type; // 范围
			//加载附加审核项目和条件项目信息
			String[] codes = SpringBeanUtil.getSpringBean("projectMgr", ProjectMgr.class)
					.getChild(def.getObj_elem_type_id(), conversionToString(def.getObjIds()));
			long[] temp = this.conversionToLong(codes);
			Arrays.sort(temp); // 审核对象排序
			def.setObjIds(temp);
			//条件项目附加集合
			for(KlElemDef e:def.getKlElemDefs()){
				String[] conCodes = null;
				if (null != e.getElemVals()){
				    conCodes =SpringBeanUtil.getSpringBean("projectMgr", ProjectMgr.class)
				    		.getChild(e.getElem_type_id(), e.getElemVals());
				}
				Arrays.sort(conCodes);
				e.setElemVals(conCodes);
			    KLElemExpression ex =new KLElemExpression(e);
			    ex.setType(changeType(e.getElem_type_id()));
				rule.getExps().add(ex);
		    }
			
			rule.setKlDefs(def);
			rules.add(rule);
		}
	
		return rules;
	}
	
	
	/**
	 * 为适应之前cpa，转换类型为之前设计
	 * @param sourceType
	 * @return
	 */
	private int changeType(int sourceType){
		int targetType=sourceType;
		switch (sourceType) {
		// 药品、诊断、诊疗、耗材类型时 医保药品目录
		case Constant.ELEM_TYPE_DRUG:
		case Constant.ELEM_TYPE_DRUG_CATE:
		case Constant.ELEM_TYPE_MI_DRUG:
			targetType=Constant.CHECK_PROJECT_DRUG;
			break;
		case Constant.ELEM_TYPE_DIAG:
			targetType=Constant.CHECK_PROJECT_DIAGNOSIS;
			break;
		case Constant.ELEM_TYPE_TREAT:
		case Constant.ELEM_TYPE_MI_TREAT:
			targetType=Constant.CHECK_PROJECT_TREAT;
			break;
		case Constant.ELEM_TYPE_CONSUM:
		case Constant.ELEM_TYPE_CONSUM_CATE:
		case Constant.ELEM_TYPE_MI_CONSUM:
			targetType=Constant.CHECK_PROJECT_CONSUM;
			break;
		}
		
		return targetType;
		
	}
		
	
	//把字符串数组转换为数字数组
	private long[] conversionToLong(String[] cs) {
		long[] cs_long = new long[cs.length];
		for (int j = 0; j < cs.length; j++) {
			cs_long[j] = Long.parseLong(cs[j]);
		}
		return cs_long;
	}
	//把数字数组转换为字符串数组
	private String[] conversionToString(long[] cs) {
		String[] cs_String = new String[cs.length];
		for (int j = 0; j < cs.length; j++) {
			cs_String[j] = String.valueOf(cs[j]);
		}
		return cs_String;
	}
	
}
