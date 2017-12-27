//package com.aghit.task.algorithm.domain.rule;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Random;
//import java.util.TreeMap;
//
//import org.apache.commons.lang3.time.DateUtils;
//
//import com.aghit.task.algorithm.AbstractRule;
//import com.aghit.task.algorithm.domain.BreakDetail;
//import com.aghit.task.algorithm.domain.DoctorAdvice;
//import com.aghit.task.algorithm.domain.Patient;
//import com.aghit.task.algorithm.domain.Payment;
//import com.aghit.task.algorithm.domain.ProperUsageKL;
//import com.aghit.task.util.CPADateUtil;
//import com.aghit.task.util.Constant;
//
//public class OverDosageRule extends AbstractRule{
//	private String checkCode;			// 审核项目编码
//	private int dayDoseCalcWay; 		// 日剂量计算方式
//	private ProperUsageKL klItem; 		// 审核对象知识属性
//	
//	@Override
//	public List<BreakDetail> check(Patient patient) throws Exception {
//		
//		// 如果规则有周期设置，直接跳出，不进行计算
//		if (cycleFlg == Constant.ENABLED) {
//			return null;
//		}
//		
//		// 如果规则没有周期设置，直接跳出，不进行计算
//		if (cycleFlg == Constant.DISABLED) {
//			return null;
//		}
//		
//		// 违规数据保存
//		List<BreakDetail> errDetail = new ArrayList<BreakDetail>();
//		// 需要参与算法的参考、运算的处理数据
//		Map<Date,Payment> handleData = new TreeMap<Date, Payment>();
//		
//		// 如果审核项目为药品时
//		if (checkType == Constant.CHECK_PROJECT_DRUG) {
//			
//			// 处理门诊数据
//			if (medicalType != null
//					&& medicalType.contains(Constant.MEDICAL_TYPE_OUTPATIENT)) {
//				
//				handleData = findUsefullData(patient
//						.getPrescription());
//				// 进行审核判断，并保存门诊违规数据
//				//errDetail.addAll(handlePrescription(handleData));
//			}
//			// 处理门特数据
//			if(medicalType != null 
//					&& medicalType.contains(Constant.MEDICAL_TYPE_SPECIAL)){
//				
//				handleData = findUsefullData(patient.getPrescription());
//				// 进行审核判断，并保存门特违规数据
//				//errDetail.addAll(handlePrescription(handleData));
//			}
//			// 处理住院数据
//			if(medicalType != null 
//					&& medicalType.contains(Constant.MEDICAL_TYPE_HOSPITAL)){
//				
//				handleData = findUsefullData(patient.getPrescription());
//				// 进行审核判断，并保存住院违规数据
//				//errDetail.addAll(handlePrescription(handleData));
//			}
//		}
//
//		// 如果审核项目为诊疗或者耗材时
//		if (checkType == Constant.CHECK_PROJECT_DOCTOR
//				|| checkType == Constant.CHECK_PROJECT_CONSUM) {
//			
//			// 处理门诊数据
//			if(medicalType != null 
//					&& medicalType.contains(Constant.MEDICAL_TYPE_OUTPATIENT)){
//				
//				handleData = findUsefullData(patient.getDoctorAdvice());
//				// 进行审核判断，并保存门诊违规数据
//				errDetail.addAll(handleDocAdvice(handleData));
//			}
//			// 处理门特数据
//			if(medicalType != null 
//					&& medicalType.contains(Constant.MEDICAL_TYPE_SPECIAL)){
//				
//				handleData = findUsefullData(patient.getDoctorAdvice());
//				// 进行审核判断，并保存门诊违规数据
//				errDetail.addAll(handleDocAdvice(handleData));
//			}
//			// 处理住院数据
//			if(medicalType != null 
//					&& medicalType.contains(Constant.MEDICAL_TYPE_HOSPITAL)){
//				
//				handleData = findUsefullData(patient.getDoctorAdvice());
//				// 进行审核判断，并保存门诊违规数据
//				errDetail.addAll(handleDocAdvice(handleData));
//			}
//		}
//
//		return errDetail;
//	}
//	
//	/**
//	 * 检查诊疗、耗材的超量审核
//	 * 
//	 * @param handleData 处理数据
//	 * @return
//	 */
//	private List<BreakDetail> handleDocAdvice(Map<Date, Payment> handleData) {
//		
//		// 返回的违规信息
//		List<BreakDetail> retInfo = new ArrayList<BreakDetail>();
//		// 如果无处理数据直接返回
//		if(handleData == null || handleData.size() < 1){
//			return retInfo;
//		}
//		Iterator<Entry<Date, Payment>> it = handleData.entrySet().iterator();
//		Date checkStartDt = null;	//周期检查开始日
//		Date checkEndDt = null;		//周期检查结束日
//		
//		// 循环处理相关数据
//		while(it.hasNext()){
//			Entry<Date, Payment> ele = it.next();
//			Date recordDate = ele.getKey();		// 药品结算时间
//			DoctorAdvice advice = (DoctorAdvice) ele.getValue();
//			// 如果非处理数据，跳出当前循环进行下一条处理
//			if(!advice.getSign()){
//				continue;
//			}
//			
//			// 如果数据不完整，直接跳出当前循环进行下一条处理
//			if (advice.getOrder_amount() == 0) {
//				continue;
//			}
//			
//			checkStartDt = DateUtils.addDays(CPADateUtil.getDateFirst(recordDate), -cycleEnd);//设置周期开始日
//			checkEndDt = DateUtils.addDays(CPADateUtil.getDateLast(recordDate), -cycleStart);//设置周期结束日
//			
//			
//			// 获取比较周期之内的数据
//			Map<Date,Payment> compareMap = ((TreeMap)handleData).subMap(checkStartDt, checkEndDt);
//			
//			// 如果时间有重复，记录违规数据
//			int days = isOverDosageAdvice(advice);
//			if(days != -1){
//				BreakDetail error = new BreakDetail();
//				error.setRuleId(this.ruleId);	// 规则Id
//				error.setRawDataId(advice.getSi_clearing_cost_no());		// 原始记录Id
//				error.setBreakDetial("诊疗时间重复,上次诊疗天数为:" + days);		// 错误信息
//				retInfo.add(error);			//添加到违规总记录中
//			}
//		}
//		
//		return retInfo;
//	}
//	
//	/**
//	 * 计算是否有诊疗、耗材超量
//	 * 
//	 * @param advice 审核记录
//	 * @return 无违规数据返回-1，否则返回知识库规定最大数量
//	 */
//	private int isOverDosageAdvice(DoctorAdvice advice){
//		
//		// 计算知识库规定最大数量
//		int dayOfUsage = countMaxAdviceDosage();
//		
//		// 如果诊疗、耗材所开实际数量大于知识库中规定的数量，报错
//		if(advice.getOrder_amount() > dayOfUsage){
//			return dayOfUsage;
//		}
//		
//		return -1;
//	}
//	
//	/**
//	 * 计算知识库中诊疗、耗材的最大数量
//	 * 
//	 * 计算公式：最大日用量*限制天数
//	 * 
//	 * @return
//	 */
//	private int countMaxAdviceDosage(){
//			
//		double klDayDose = 0;	// 知识库用药日剂量
//		
//		// 判断日剂量所选用字段(可选有3个)，并统一计算到日
//		switch (dayDoseCalcWay) {
//		case 1:
//			// 使用 -- 频次单位最大剂量计算
//			klDayDose = (klItem.getMaxDoseFreqUnit() / getFreqUnitDays(klItem
//					.getFreqUnit())) * klItem.getLimitDays();
//			break;
//		case 2:
//			// 使用 -- 协定日剂量计算
//			klDayDose = (klItem.getFixDayDose() / getFreqUnitDays(klItem
//					.getFreqUnit())) * klItem.getLimitDays();
//			break;
//		default:
//			// 使用 -- 医保日剂量计算
//			klDayDose = (klItem.getSiFixDayDose() / getFreqUnitDays(klItem
//					.getFreqUnit())) * klItem.getLimitDays();
//		}
//		
//		return (int) Math.ceil(klDayDose);
//	}
//	
//	/**
//	 * 找出与本规则相关的有用数据
//	 * 
//	 * @param allData 全部的清单数据
//	 * @return
//	 */
//	private <T> Map<Date, T> findUsefullData(List<T> allData) {
//
//		Map<Date, T> result = new TreeMap<Date, T>();
//		Iterator<T> it = allData.iterator();
//
//		Date clearingDt = null;
//		Random random = new Random();//随机种子，放置中间表的结算日期没有时分秒
//		while (it.hasNext()) {
//			Payment entity = (Payment) it.next();
//
//			// 有异常数据出现
//			if (entity == null || entity.getCodes() == null
//					|| entity.getClm_date() == null 
//					|| entity.getCodes().length != 1) {
//				continue;
//			}
//
//			// 当前规则的code相匹配
//			if (this.cycleStartTime.compareTo(entity.getClm_date()) <= 0 
//					&& entity.getCodes()[0].startsWith(checkCode)) {
//				
//				clearingDt = entity.getClm_date();
//				// 以时间作为key时，会以有重复存在可能，可以为其随机增加一定的时间来确保惟一性
//				while ((result.get(clearingDt)) != null) {
//					clearingDt = new Date(clearingDt.getTime()
//							+ random.nextInt(500));
//				}
//				result.put(clearingDt, (T) entity);
//			}
//		}
//
//		return result;
//	}
//	
//	/**
//	 * 判断频次单位内的天数
//	 * 
//	 * @param unit 频次单位
//	 * @return 
//	 */
//	private int getFreqUnitDays(String unit){
//		
//		int days = 1;//默认周期为天
//		// 如果频次为周
//		if("2".equals(unit)){
//			days = 7;
//		}else if("3".equals(unit)){
//			// 如果频次为月
//			days = 30;
//		}
//		
//		return days;
//	}
//	
//	public String getCheckCode() {
//		return checkCode;
//	}
//
//	public void setCheckCode(String checkCode) {
//		this.checkCode = checkCode;
//	}
//
//	public int getDayDoseCalcWay() {
//		return dayDoseCalcWay;
//	}
//
//	public void setDayDoseCalcWay(int dayDoseCalcWay) {
//		this.dayDoseCalcWay = dayDoseCalcWay;
//	}
//
//	public ProperUsageKL getKlItem() {
//		return klItem;
//	}
//
//	public void setKlItem(ProperUsageKL klItem) {
//		this.klItem = klItem;
//	}
//
//}
