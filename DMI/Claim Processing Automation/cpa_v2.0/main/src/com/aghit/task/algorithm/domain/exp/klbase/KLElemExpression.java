package com.aghit.task.algorithm.domain.exp.klbase;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.aghit.task.algorithm.domain.exp.AbstractExpression;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.util.Constant;

public class KLElemExpression extends AbstractExpression{
	
	private static Logger log = Logger.getLogger(KLElemExpression.class);
	
	private KlElemDef elemDef;
	
	@Override
	public Map<Boolean,Set<Long>> evaluate(Object[] comparedData) {
    	
		Map<Boolean,Set<Long>> rtn = new HashMap<Boolean,Set<Long>>();
    	//元素审核条件
    	String[] validLst= elemDef.getElemVals();
    	
    	//
    	Map<Boolean,Set<Long>> rtnTrue = new HashMap<Boolean,Set<Long>>();
    	Set<Long> sTrue = new TreeSet<Long>();
    	sTrue.add(-1l);
    	rtnTrue.put(true,sTrue);
    	//
    	Map<Boolean,Set<Long>> rtnFalse = new HashMap<Boolean,Set<Long>>();
    	Set<Long> sfalse = new TreeSet<Long>();
    	sfalse.add(-1l);
    	rtnFalse.put(false,sfalse);
    	
		
		// 如果无有效的判断集合，则表示无需判断，直接返回true
		if(null  == validLst || 0==validLst.length ) {
			return rtnTrue;
		}
		// 如果没有无实际值传入，则根本无法匹配成功，直接返回false
		if(comparedData == null || comparedData.length < 1 || null == comparedData[0]){
			log.info("知识" + elemDef.getK_id() + ";元素 " + elemDef.getElem_id() + "无实际值传入.");
			return rtnFalse;
		}
		//取值
		String itemValue = comparedData[0].toString();
		if (comparedData[0] instanceof HashMap){
			Map<Long,Long> m = (Map<Long,Long>)comparedData[0];
			if (m.size() > 0){
				Set<Long> keSet=m.keySet(); 
				Iterator<Long> iterator = keSet.iterator();
				itemValue = m.get(iterator.next()).toString();
			}else{
				itemValue = "-1";
			}
		}
		//元素为空不审核时返回true,目前比较对象为“” 或 -1 时 认为元素为空
		if(Constant.NULL_FLAG_NO == elemDef.getNull_flag() && (itemValue.equals("") || itemValue.contains("-1"))){
			return rtnTrue;
		}
		
		if(Constant.CON_CODE_IN.equals(elemDef.getElem_symb())){ //IN
			if (comparedData[0] instanceof Map){
				Map<Long,Long> m = (Map<Long,Long>)comparedData[0];
				for (Long key : m.keySet()) {
					if(Collections.binarySearch(Arrays.asList(validLst), m.get(key).toString()) >= 0){
						// 只要有一个条件满足便可，故直接跳出
						return rtnTrue;
					}
			    }
//				for (Object o:(Object[])comparedData[0]){
//					if(Collections.binarySearch(Arrays.asList(validLst), o.toString()) >= 0){
//						// 只要有一个条件满足便可，故直接跳出
//						return rtnTrue;
//					}
//				}
			}else{
				if(Collections.binarySearch(Arrays.asList(validLst), itemValue) >= 0){
					// 只要有一个条件满足便可，故直接跳出
					return rtnTrue;
			    }
			}
		}else if(Constant.CON_CODE_LE.equals(elemDef.getElem_symb())
				|| Constant.CON_CODE_GE.equals(elemDef.getElem_symb())
				|| Constant.CON_CODE_LT.equals(elemDef.getElem_symb())
				|| Constant.CON_CODE_GT.equals(elemDef.getElem_symb())
				|| Constant.CON_CODE_EQ.equals(elemDef.getElem_symb())){
			if (compareVal(itemValue,validLst[0],elemDef.getElem_Data_type(),elemDef.getElem_symb())){
				return rtnTrue;
			}
		}else if(Constant.CON_CODE_EX.equals(elemDef.getElem_symb())){
			if (comparedData[0] instanceof HashMap){
				Map<Long,Long> m = (Map<Long,Long>)comparedData[0];
				for (long key : m.keySet()) {
					if(Collections.binarySearch(Arrays.asList(validLst), m.get(key).toString()) >= 0){
						// 只要有一个条件满足便可，故直接跳出
						if(rtn.containsKey(false)){
							rtn.get(false).add(key);
						}else{
							Set<Long> s = new TreeSet<Long>();
					    	s.add(key);
					    	rtn.put(false,s);
						}
						return rtn;
					}
				}
			}else{
				if(Collections.binarySearch(Arrays.asList(validLst), itemValue) >= 0){
					// 只要有一个条件满足便可，故直接跳出
					return rtnFalse;
			    }
			}
			return rtnTrue;
		}else if(Constant.CON_CODE_RG.equals(elemDef.getElem_symb())){
			for(String item : validLst){
				String[] period = item.split("-");
				if(Long.parseLong(itemValue)>= Long.parseLong(period[0]) &&
						Long.parseLong(itemValue) <= Long.parseLong(period[1])){
					return rtnTrue;
				}
			}
		}else if(Constant.CON_CODE_NX.equals(elemDef.getElem_symb())){ //NX
			if (comparedData[0] instanceof Map){
				Map<Long,Long> m = (Map<Long,Long>)comparedData[0];
				for (Long key : m.keySet()) {
					if(Collections.binarySearch(Arrays.asList(validLst), m.get(key).toString()) < 0){
						// 只要有一个条件不满足便可，故直接跳出
						return rtnTrue;
					}
			    }
//				for (Object o:(Object[])comparedData[0]){
//					if(Collections.binarySearch(Arrays.asList(validLst), o.toString()) < 0){
//						// 只要有一个条件不满足便可，故直接跳出
//						return rtnTrue;
//					}
//				}
			}else{
				if(Collections.binarySearch(Arrays.asList(validLst), itemValue) < 0){
					// 只要有一个条件不满足便可，故直接跳出
					return rtnTrue;
			    }
			}
		}else if(Constant.CON_CODE_OI.equals(elemDef.getElem_symb())){ //OI
			if (comparedData[0] instanceof Map){
				Map<Long,Long> m = (Map<Long,Long>)comparedData[0];
				for (Long key : m.keySet()) {
					if(Collections.binarySearch(Arrays.asList(validLst), m.get(key).toString()) < 0){
						// 只要有一个条件不满足便可，故直接跳出
						return rtnFalse;
					}
			    }
//				for (Object o:(Object[])comparedData[0]){
//					if(Collections.binarySearch(Arrays.asList(validLst), o.toString()) < 0){
//						// 只要有一个条件不满足便可，故直接跳出
//						return rtnFalse;
//					}
//				}
			}else{
				if(Collections.binarySearch(Arrays.asList(validLst), itemValue) < 0){
					// 只要有一个条件不满足便可，故直接跳出
					return rtnFalse;
			    }
			}
			return rtnTrue;
		}else{
			log.info("知识" + elemDef.getK_id() + ";元素" + elemDef.getElem_id() + "无条件参数");
			return rtnTrue;
		}
		return rtnFalse;
	}
	
	//计算比较值
	private boolean compareVal(String val1,String val2,int dataType,String comType){
		switch (dataType) {
		case Constant.DATA_TYPE_FLOAT :
		case Constant.DATA_TYPE_INT:
			BigDecimal a = new BigDecimal(val1);
			BigDecimal b = new BigDecimal(val2);
			if (comType.equals(Constant.CON_CODE_LE)){
				if (a.compareTo(b)<=0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_GE)){
				if (a.compareTo(b)>=0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_LT)){
				if (a.compareTo(b)<0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_GT)){
				if (a.compareTo(b)>0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_EQ)){
				if (a.compareTo(b)==0){
					return true;
				}
			}
			break;
		case Constant.DATA_TYPE_STRING:
			if (comType.equals(Constant.CON_CODE_LE)){
				if (val1.compareTo(val2)<=0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_GE)){
				if (val1.compareTo(val2)>=0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_LT)){
				if (val1.compareTo(val2)<0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_GT)){
				if (val1.compareTo(val2)>0){
					return true;
				}
			}else if(comType.equals(Constant.CON_CODE_EQ)){
				if (val1.compareTo(val2)==0){
					return true;
				}
			}
			break;
		case Constant.DATA_TYPE_DATE:
			return true;
		default:
			return true;
		}
	
		return false;
	}
	
	public KLElemExpression(KlElemDef e){
		elemDef=e;
	}
    public KlElemDef getElemDef() {
		return elemDef;
	}
	public void setElemDef(KlElemDef elemDef) {
		this.elemDef = elemDef;
	}
	
	


}
