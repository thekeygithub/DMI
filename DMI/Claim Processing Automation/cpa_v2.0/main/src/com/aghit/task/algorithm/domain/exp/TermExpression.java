package com.aghit.task.algorithm.domain.exp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 术语类的表达式
 * 如：药品、诊断、诊疗、耗材
 * @author Administrator
 *
 */
public class TermExpression extends AbstractExpression{

	private long[] validArr;	// 有效的取值集合
	
	
	public boolean evaluate1(Object[] refData) {
		
		// 如果无有效的判断集合，则表示无需判断，直接返回true
		if(validArr == null || validArr.length < 1) return true;
		
		// 如果没有无实际值传入，则根本无法匹配成功，直接返回false
		if(refData == null || refData.length < 1 || refData[0] == null) return false;
		
		long[] chkData = (long[]) refData[0];
		
		for(long item : chkData){
			if(Arrays.binarySearch(validArr, item) >= 0){
				// 只要有一个条件满足便可，故直接跳出
				return true;
			}
		}
		
		return false;
	}

	public long[] getValidArr() {
		return validArr;
	}

	public void setValidArr(long[] validArr) {
		this.validArr = validArr;
	}

	@Override
	public Map<Boolean, Set<Long>> evaluate(Object[] refData) {
		// TODO Auto-generated method stub
		return null;
	}

}
