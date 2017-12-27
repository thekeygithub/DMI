package com.aghit.task.algorithm.domain.exp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aghit.task.util.Constant;

/**
 * 字面值表达式
 * 医院等级、医院、年龄、性别、支付类别、医师职称
 * @author Administrator
 *
 */
public class LiteralExpression extends AbstractExpression{
	
	private List<String> validLst;	// 有效取值集合
	private List<String> labelLst;	// 条件规则中所选表示值，便于以后出日志
		
	public boolean evaluate1(Object[] refData) {
		
		// 如果无有效的判断集合，则表示无需判断，直接返回true
		if(validLst == null || validLst.isEmpty()) return true;
		
		// 如果没有无实际值传入，则根本无法匹配成功，直接返回false
		if(refData == null || refData.length < 1 || refData[0] == null) return false;
		
		String itemValue = refData[0].toString();
		
		// 如果是年龄时，需要特别处理
		if(getType() == Constant.CONDITION_PROJECT_AGE){
			for(String item : validLst){
				String[] period = item.split("-");
				// 如果实际年龄包含在范围内，则直接跳出
				if(Integer.valueOf(itemValue).intValue() >= Integer.valueOf(period[0]).intValue() &&
						Integer.valueOf(itemValue).intValue() <= Integer.valueOf(period[1]).intValue()){
					return true;
				}
			}
		}else{
			if(Collections.binarySearch(validLst, itemValue) >= 0){
				// 只要有一个条件满足便可，故直接跳出
				return true;
			}
		}
		
		return false;
	}

	public List<String> getValidLst() {
		return validLst;
	}

	public void setValidLst(List<String> validLst) {
		this.validLst = validLst;
	}

	@Override
	public Map<Boolean, Set<Long>> evaluate(Object[] refData) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
