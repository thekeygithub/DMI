package com.aghit.task.algorithm.domain.rule.klbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;

public class KlRule extends AbstractRule {
	//日志
	private static Logger log = Logger.getLogger(KlRule.class);
	
	//规则对应知识集
	private KlDef klDef;
	private List<KLElemExpression> exps;

	@Override
	public synchronized  List<BreakDetail> checkPayment(Collection<Payment> list, Patient p)
			throws Exception {
		// 如果异常导致无条件项目，直接跳出返回null
		if(exps == null || exps.isEmpty()) return null;
		
		List<BreakDetail> msgLst = new ArrayList<BreakDetail>();
		
		Iterator<Payment> it = list.iterator();
		// 循环判断所有的审核项目
		while(it.hasNext()){
			Payment pay = it.next();// 获取结算单数据
			
			// 如果结算单日期在规则审核日期之前，即为参考数据，不需要规则审核
			if (this.cycleStartTime.compareTo(pay.getClm_date()) > 0 || false == pay.getSign()){
				continue;
			}
			
			CheckPayment check = CheckFactory.creatCheck(checkType, ruleId, cycleStart, cycleEnd, cycleFlg, klDef, exps);
			msgLst.addAll(check.checkPayment(list, p, pay));	
		}
		
		return msgLst;
	}
	
	public KlDef getKlDefs() {
		return klDef;
	}

	public void setKlDefs(KlDef klDef) {
		this.klDef = klDef;
	}
	
	public List<KLElemExpression> getExps() {
		return exps;
	}
	public void setExps(List<KLElemExpression> exps) {
		this.exps = exps;
	}

}



