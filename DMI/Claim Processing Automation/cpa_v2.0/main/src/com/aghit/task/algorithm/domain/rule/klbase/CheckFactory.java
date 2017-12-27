package com.aghit.task.algorithm.domain.rule.klbase;

import java.util.List;

import org.apache.log4j.Logger;

import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.util.Constant;

/**
 * 审核项目工厂
 */
public class CheckFactory {

	private static Logger log = Logger.getLogger(CheckPayment.class);
	
	public static CheckPayment creatCheck(int checkType, long ruleId,
			int cycleStart, int cycleEnd, int cycleFlg,KlDef klDef,List<KLElemExpression> exps){
		
		CheckPayment check = null ;
		switch (checkType){
		case Constant.CHECK_PROJECT_DRUG :
			check = new DrugCheckPayment(checkType, ruleId,
					cycleStart, cycleEnd, cycleFlg,klDef,exps);
			break;
		case Constant.CHECK_PROJECT_DIAGNOSIS:
			check = new DiagCheckPayment(checkType, ruleId,
					cycleStart, cycleEnd, cycleFlg,klDef,exps);
			break;
		case Constant.CHECK_PROJECT_CONSUM:
		case Constant.CHECK_PROJECT_TREAT:	
			check = new TreatConsumCheckPayment(checkType, ruleId,
					cycleStart, cycleEnd, cycleFlg,klDef,exps);
			break;
		default:
			log.error("不支持知识id（"+ klDef.getK_id() +"）的审核类型为 " + checkType + "的规则。");
		}
		
		return check;
	}
}
