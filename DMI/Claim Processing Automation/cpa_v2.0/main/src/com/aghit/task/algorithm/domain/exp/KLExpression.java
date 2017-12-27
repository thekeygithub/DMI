package com.aghit.task.algorithm.domain.exp;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aghit.task.algorithm.domain.ProperUsageKL;
import com.aghit.task.common.service.impl.CacheServiceImpl;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;

/**
 * 知识库规则表达式
 * 时间重复性、超量审核、适用症、禁忌症、日剂量
 * @author Administrator
 *
 */
public class KLExpression  extends AbstractExpression{
	
	private String val;		// 可重复天数
	private Map<String,Set<String>> ids;		// 适应症ID、禁忌症ID
	private String logicId;	// 逻辑计算ID（003用法（默认）、005年龄+用法、006诊断+用法、007年龄+诊断+用法）
	private static long DAY_MS = 24 * 3600 * 1000;	// 一天的毫秒数

	public boolean evaluate1(Object[] refData) {
		
		boolean rtn = false;
		
		// 如果为日剂量的计算
		if(getType() == Constant.CONDITION_PROJECT_DDD){
			rtn = dayDoseJudge(refData);
		}
		
		// 如果为时间重复
		if(getType() == Constant.CONDITION_PROJECT_TIME_REPEAL){
			rtn = timeRepeat(refData);
		}
		// 如果为超量审核
		if(getType() == Constant.CONDITION_PROJECT_OVERDOSAGE){
			rtn = overDosage(refData);
		}
		// 如果为适用症
		if(getType() == Constant.CONDITION_PROJECT_INDICATION){
			rtn = isExist(refData, Constant.INDICATION);
		}
		// 如果为禁忌症
		if(getType() == Constant.CONDITION_PROJECT_CONTRAINDICATION){
			rtn = !isExist(refData, Constant.CONTRAINDICATION);
		}
		
		return rtn;
	}
	
	/**
	 * 判断药品是否时间重复
	 * 当期开药日期   - 上一个同类药品开药日期  >= 给药天数  - 可重叠天数 
	 * @param refData
	 * @return
	 */
	private boolean timeRepeat(Object[] refData) {
		
		Date curDrugDate = (Date) refData[0]; // 当前药品的日期
		Object[] lastDrugInfo = (Object[]) refData[1]; // 比较对象信息
		// 如果之前没有同类药品，表示肯定没有问题，直接返回
		if(lastDrugInfo == null) return true;
		
		Date lastDrugDate = (Date)lastDrugInfo[0];	// 前一个同类药品的开药日期
		int drugDays = (Integer) lastDrugInfo[1];	// 前一个同类药品的给药天数
		
		return CPADateUtil.getDateFirst(curDrugDate).getTime() - CPADateUtil.getDateFirst(lastDrugDate).getTime() 
				>= (drugDays - Integer.valueOf(val)) * DAY_MS;
	}

	/**
	 * 超量审核判断
	 * @param refData
	 * @return
	 */
	private boolean overDosage(Object[] refData) {
		
		long drugId = (Long) refData[0];// 药品ID
		int drugDays = (Integer) refData[1];// 给药天数
		long[] diag = (long[]) refData[2];//诊断ID
		int age = (Integer) refData[3];// 年龄
		long admAppro = (Long) refData[4];//给药途径
		
		ProperUsageKL kl = matchKlItem(drugId, diag, age, admAppro);// 知识库得出的相应知识
		
		// 如果无法相关知识条目，则不进行判断，直接返回true
		if(kl == null){
			return true;
		}
		
		return kl.getLimitDays() >= drugDays;
	}

	/**
	 * 判断禁忌症或者适应症是否存在
	 * @param refData
	 * @param flg 禁忌症、适应症标识
	 * @return
	 */
	private boolean isExist(Object[] refData, int flg) {
		
		long[] chkData = (long[]) refData[0];	// 结算单诊断
		long drugId = (Long) refData[1];// 药品ID
		Set<String> diagSet = ids.get(drugId + "_" + flg);
	
		// 如果没有获取到药品的禁忌症、适应症，直接返回正确
		if(diagSet == null){
			if(flg == Constant.INDICATION){
				return true;// 适应症时返回true
			}else{
				return false;// 适应症时返回false
			}
		}
		
		// 遍历实际的诊断信息
		for(long item : chkData){
			// 实际诊断是否在禁忌或者适应症中出现
			for(String id :diagSet){
				if(item == Long.valueOf(id)){
					// 只要有一个条件满足便可，故直接跳出
					return true;
				}
			}
		}
		
		return false;
	}


	/**
	 * 日剂量判断
	 * @param refData
	 * @return
	 */
	private boolean dayDoseJudge(Object[] refData) {
		
		long drugId = (Long) refData[0];// 药品ID
		double midDose = (Double) refData[1];// 中间表计算得出的日剂量
		long[] diag = (long[]) refData[2];//诊断ID
		int age = (Integer) refData[3];// 年龄
		long admAppro = (Long) refData[4];//给药途径
		
		ProperUsageKL kl = matchKlItem(drugId, diag, age, admAppro);// 知识库得出的相应知识
		
		// 如果无法相关知识条目，则不进行判断，直接返回true
		if(kl == null){
			return true;
		}
		
		return kl.getDayDose() >= midDose;
	}

	/**
	 * 匹配知识库应该的记录
	 * @param drugId 药品ID
	 * @param diag 结算单诊断
	 * @param age 年龄
	 * @param admAppro 给药途径
	 * @return
	 */
	private ProperUsageKL matchKlItem(long drugId, long[] diag, int age, long admAppro){
		
		Map<String,List<ProperUsageKL>> klMap = CacheServiceImpl.getProUsgKlCache();
		
		//003用法（默认） 
		String factLogicId = logicId;
		
		List<ProperUsageKL> kls = klMap.get(factLogicId + "_" + admAppro + "_" + drugId);
		if(kls == null || kls.isEmpty()){
			// 如果无法精确匹配，则寻找默认003
			factLogicId = "003";
			kls = klMap.get(factLogicId + "_" + admAppro + "_" + drugId);
			// 如果默认用法还没有找到，直接返回-1，表示无法找到
			if(kls == null || kls.isEmpty()){
				return null;
			}
		}
		
		ProperUsageKL rtn = null;
		Iterator<ProperUsageKL> it = kls.iterator();		// 获取迭代器
		
		// 只通过用法判断
		if(factLogicId.equals("003")){
			rtn = kls.get(0);	// 此处必然有数据，只要选择第一条便可 
		}else if(factLogicId.equals("005")){
			// 年龄 + 用法
			while(it.hasNext()){
				ProperUsageKL kl = it.next();
				String[] agePerid = kl.getProperPersion().split("-");
				// 如果年龄符合，即为本条记录
				if(age >= Integer.valueOf(agePerid[0]) && age <= Integer.valueOf(agePerid[1])){
					rtn = kl;
					break;
				}
			}
			
		}else if(factLogicId.equals("006")){
			// 诊断 + 用法
			while(it.hasNext()){
				ProperUsageKL kl = it.next();
				// 如果诊断符合，即为本条记录
				if(Arrays.binarySearch(diag, kl.getIndication()) >= 0){
					rtn = kl;
					break;
				}
			}
		}else if(factLogicId.equals("007")){
			// 年龄 + 诊断 + 用法
			while(it.hasNext()){
				ProperUsageKL kl = it.next();
				String[] agePerid = kl.getProperPersion().split("-");
				// 如果年龄和诊断同时符合，即为本条记录
				if((age >= Integer.valueOf(agePerid[0]) && age <= Integer.valueOf(agePerid[1])) 
						&& Arrays.binarySearch(diag, kl.getIndication()) >= 0){
					rtn = kl;
					break;
				}
			}
		}
		
		return rtn;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getLogicId() {
		return logicId;
	}

	public void setLogicId(String logicId) {
		this.logicId = logicId;
	}

	public Map<String, Set<String>> getIds() {
		return ids;
	}

	public void setIds(Map<String, Set<String>> ids) {
		this.ids = ids;
	}

	@Override
	public Map<Boolean, Set<Long>> evaluate(Object[] refData) {
		// TODO Auto-generated method stub
		return null;
	}

}
