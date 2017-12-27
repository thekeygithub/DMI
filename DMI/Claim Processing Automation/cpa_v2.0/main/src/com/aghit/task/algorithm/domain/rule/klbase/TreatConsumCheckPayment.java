package com.aghit.task.algorithm.domain.rule.klbase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.ComparedEntity;
import com.aghit.task.algorithm.domain.DoctorAdvice;
import com.aghit.task.algorithm.domain.DoctorAdviceRecord;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ConvertUtil;

public class TreatConsumCheckPayment extends CheckPayment {

	public TreatConsumCheckPayment(int checkType, long ruleId, int cycleStart,
			int cycleEnd, int cycleFlg, KlDef klDef, List<KLElemExpression> exps) {
		super(checkType, ruleId, cycleStart, cycleEnd, cycleFlg, klDef, exps);
	}
	
	private static Logger log = Logger.getLogger(TreatConsumCheckPayment.class);

	@Override
	public List<BreakDetail> checkPayment(Collection<Payment> list, Patient p,
			Payment pay) {
		List<BreakDetail>  msgLst = new ArrayList<BreakDetail>();
		DoctorAdvice clmOrd = (DoctorAdvice) pay;
		List<DoctorAdviceRecord> ordLst = clmOrd.getDetail();
		Iterator<DoctorAdviceRecord> localIt = ordLst.iterator();
		while(localIt.hasNext()){
			DoctorAdviceRecord ordRcd = localIt.next();
			// 如果当前审核项目不属于本规则，或者 当前审核项目不参与审核 直接判断下一条 
			if (!isExeOrdRule(ordRcd) || Constant.CHK_FLAG_YES != ordRcd.getChk_flag())
				continue;

			// 右侧多条件表达式结果计算
			ErrMessage right = calcRightExp(clmOrd, ordRcd, p,this.getCheckType());
			// 如果为相符关系时
			if (false == right.isLegal()) {

				msgLst.add(writeError(ordRcd.getSi_clearing_cost_no(),
						3, ordRcd.getFromType(), ordRcd.getOrd_id(),
						right.getErrDes(), ordRcd.getOrder_name(),
						ordRcd.getOrd_fee(), p.getP_id_no(),
						ordRcd.getP_name(), ordRcd.getClm_date(),
						ordRcd.getH_code(), this.getKlDef().getK_id(), right.getErrElemID(), right.getErrCateid(),right.getErr_item()));
			}
		}
	
		return msgLst;
	}

	//获取比较对象
	private Map<Long,Long> findTerm(ComparedEntity[] ids,KlElemDef elemDef,int checkType,int conType,
			DoctorAdviceRecord ord,boolean flag,Calendar startDate,Calendar endDate){
		
		long checkId = ord.getAg_id();
		long ord_id = ord.getOrd_id();
		long rownum = ord.getRownum();
		
		Calendar conDate = Calendar.getInstance();
		Calendar checkDate = Calendar.getInstance();
		if (null != ord.getOrd_exe_date()){
			checkDate.setTime(ord.getOrd_exe_date());
		}
		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		for(ComparedEntity ce : ids){
			//审核项目类型与条件类型相同，审核项目id与条件类型项目id相同，排除条件为true，则该条件项目不参加审核 drug_id
			if (elemDef.getEx_obj_flag() == Constant.EX_OBJ_FLAG_YES && checkType == conType && true == Long.toString(checkId).equals(ce.getCode())){
				continue;
			}
				
			//当非诊断，审核项目类型与条件类型相同，审核项目pre_id与条件类型项目Pre_id相同 排除审核项目
			if (checkType == conType && ord_id == ce.getId()){
				continue;
			}
			
			//条件项目为诊断时  或无日期区间,或条件日期为空时全部返回 
			if (false == flag || null == ce.getExe_date() || null == ord.getOrd_exe_date()){ 
//				tmpList.addAll(ConvertUtil.longArr2List(decideIdFrom(ce)));
				tmpList.put(ce.getId(), Long.valueOf(ce.getCode()));
			}else{
				conDate.setTime(ce.getExe_date());
	    		if (conDate.compareTo(startDate)>=0 && conDate.compareTo(endDate) <=0){
	    			//非诊断 审核项目类型与条件项目类型相同，日期相同，核项目id与条件类型项目id相同 则取rownum小的，大的过滤掉，防止日期相同项目都被报违规。
	    			if (conDate.compareTo(checkDate)==0 && checkType == conType && true == Long.toString(checkId).equals(ce.getCode()) && rownum < ce.getRownum()){
	    				continue;
	    			}
//	    			tmpList.addAll(ConvertUtil.longArr2List(decideIdFrom(ce)));
	    			tmpList.put(ce.getId(), Long.valueOf(ce.getCode()));
	    		}
			}	
		}
		
		return tmpList;	
	}

	//获取可参考的五类术语集合
	private Map<Long,Long> findComparedTerm(int checkType, int conType, DoctorAdvice clmOrd, DoctorAdviceRecord ord, Patient p,
			KlElemDef elemDef) {
			
		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		//日期区间
		Map<String,Calendar> map;
		Calendar startDate = null;
		Calendar endDate = null;
        //是否加入日期区间
	    boolean flag =false;
		if (null != elemDef.getIntv_range() && !"".equals(elemDef.getIntv_range()) && null != ord.getOrd_exe_date()){
			flag = true;
			map=this.calDateArea(ord.getOrd_exe_date(), elemDef.getIntv_range(), elemDef.getIntv_unit());
			startDate = map.get("start");
			endDate = map.get("end");
		}
		
		String dataKey = clmOrd.getFromType() + "_" + conType;
		// 无周期的时候，参考值只选结算单的数据
		if (cycleFlg == Constant.DISABLED) {
			Map<String, Payment> tmp = p.getAllData().get(dataKey);
			if(null != tmp.get(clmOrd.getSi_clearing_cost_no())){
			    ComparedEntity[] ids = tmp.get(clmOrd.getSi_clearing_cost_no()).getCompEntities();   
			    tmpList.putAll(findTerm(ids, elemDef, checkType, conType,ord, flag, startDate, endDate));
			}
		}else{
			// 有周期的时候
			TreeMap<Date, Payment> tmp = p.getAllData_day().get(dataKey);
			Date checkStartDt = DateUtils.addDays(CPADateUtil.getDateFirst(clmOrd.getClm_date()), -cycleEnd);//设置周期开始日
			Date checkEndDt = DateUtils.addDays(CPADateUtil.getDateLast(clmOrd.getClm_date()), -cycleStart);//设置周期结束日
			
			// 获取比较周期之内的数据，全部周期数据
			Map<Date,Payment> compareMap = tmp.subMap(checkStartDt, checkEndDt);
			Iterator<Entry<Date,Payment>> it = compareMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Date,Payment> en = it.next();
				ComparedEntity[] ids =  en.getValue().getCompEntities();   
				tmpList.putAll(findTerm(ids, elemDef, checkType, conType, ord, flag, startDate, endDate));
			}
		}
		
		return tmpList;
//		Map<Long,Long[]> rtn = new HashMap<Long,Long[]>();
//		for (Long key : tmpList.keySet()) {
//			rtn.put(key, (tmpList.get(key).toArray(new Long[rtn.size()])));
//		}	
//		return rtn;
//		for(int i = 0; i< tmpList.size();i++){
//			rtn.add(tmpList.get(i).longValue());
//		}	
//		return rtn.toArray(new Long[rtn.size()]);
	}
	
	/**
	 * 耗材、诊疗时，处理右侧多表达式
	 * @param clmOrd 医嘱结算单
	 * @param ord 医嘱记录
	 * @param p 患者信息
	 * @return
	 */
	private ErrMessage calcRightExp(DoctorAdvice clmOrd, DoctorAdviceRecord ord, Patient p,int checkType){
		
		ErrMessage err;
		for (int i = 0; i < this.getExps().size(); i++) {
			KLElemExpression exp = this.getExps().get(i);
			// 动态的参数
			List<Object> elParams = new ArrayList<Object>();
			// 判断表达式的类型，并设置相关的参数
			switch (exp.getElemDef().getElem_type_id()) {
			// 药品、诊断、诊疗、耗材类型时
			case Constant.ELEM_TYPE_DRUG:
			case Constant.ELEM_TYPE_DRUG_CATE:
			case Constant.ELEM_TYPE_DIAG:
			case Constant.ELEM_TYPE_TREAT:
			case Constant.ELEM_TYPE_CONSUM:
			case Constant.ELEM_TYPE_CONSUM_CATE:
			case Constant.ELEM_TYPE_MI_DRUG:
			case Constant.ELEM_TYPE_MI_TREAT:
			case Constant.ELEM_TYPE_MI_CONSUM:
				elParams.add(findComparedTerm(checkType,exp.getType(),clmOrd,ord,p,exp.getElemDef()));
				break;
//			// 医院
//			case Constant.CONDITION_PROJECT_HOSPITAL:
//				elParams.add(clmOrd.getH_code());
//				break;
			// 医院等级
			case Constant.ELEM_TYPE_HOSP_RANK:
				elParams.add(clmOrd.getH_tier());
				break;
//			// 医师职称
//			case Constant.CONDITION_PROJECT_DOCTOR_TITLE:
//				elParams.add(String.valueOf(ord.getDr_lvl()));
//				break;
			// 性别
			case Constant.ELEM_TYPE_GENDER:
				elParams.add(clmOrd.getP_gend_id());
				break;
			// 年龄
			case Constant.ELEM_TYPE_AGE:
				elParams.add(clmOrd.getAge());
				break;
			// 支付类别
			case Constant.ELEM_TYPE_PAY_TYPE:
				elParams.add(clmOrd.getMi_schm_id());
				break;
			//就诊类型
			case Constant.ELEM_TYPE_FROMTYPE:
				elParams.add(clmOrd.getFromType());
				break;
			//总费用
			case Constant.ELEM_TYPE_TOTAL_FEE:
				elParams.add(clmOrd.getPay_tot());
				break;
			//住院天数
			case Constant.ELEM_TYPE_HLOS:
				elParams.add(clmOrd.getInpatient_day());
				break;
			//医院类型 中医 专科等
			case Constant.ELEM_TYPE_HOSP_TYPE:
				elParams.add(clmOrd.getHosp_org_type_id());
				break;
			//明细医师级别
			case Constant.ELEM_TYPE_DR_LVL_DETAIL:
				elParams.add(ord.getDr_lvl_detail());
				break;
			//医保标识
			case Constant.ELEM_TYPE_MI_FLAG:
				elParams.add(ord.getA_mi_flag());
				break;
		    //单价
			case Constant.ELEM_TYPE_ITEM_PRICE:
				elParams.add(ord.getOrd_unit_price());
				break;
			//结算方式
			case Constant.ELEM_TYPE_SETTLEMENT_TYPE:
				elParams.add(ord.getCle_form_id());
				break;
			//参保人员类别
			case Constant.ELEM_TYPE_P_MI_CAT:
				elParams.add(clmOrd.getP_mi_cat_id());
				break;
			//同类项目总金额	
			case Constant.ELME_TYPE_ITEM_FEE:
				String valFee;
				if (null == exp.getElemDef().getIntv_range()){ //整个结算单
					 valFee = "_ORDCLMFEE";
				}else{
					 valFee = "_ORDDAYFEE";
				}	
				if (null != ord.getTotal()){
					String key = "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + valFee;
					elParams.add(ConvertUtil.getDouble(ord.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			//同类项目总数量	
			case Constant.ELEM_TYPE_ITEM_COUNT:
				String valCount;
				if (null == exp.getElemDef().getIntv_range()){ //整个结算单
					 valCount = "_ORDCLMCNT";
				}else{
					 valCount = "_ORDDAYCNT";
				}	
				if (null != ord.getTotal()){
					String key =  "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + valCount;
					elParams.add(ConvertUtil.getDouble(ord.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			//同类项目总数量减住院天数	
			case Constant.ELEM_TYPE_ITEM_INDAYS:
				if (null != ord.getTotal()){
					String key =  "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + "_ORDCNTDAYS";
					elParams.add(ConvertUtil.getDouble(ord.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			//默认
			default:
				log.error("知识库规则，诊疗或耗材审核时，不支持的条件类型：" + exp.getElemDef().getElem_type_id());
				//无对应条件类型 直接返回true 不做判断。
				exp.setElem_state(true);
				continue;
			}
			
			Map<Boolean,Set<Long>> ret = exp.evaluate(elParams.toArray());
			Iterator<Entry<Boolean, Set<Long>>> iter = ret.entrySet().iterator();
			Entry<Boolean, Set<Long>> entry;
			entry = iter.next();
			exp.setElem_state(entry.getKey());
			exp.setPreId(entry.getValue());
		}
		err=getErrMessage();
		return err;
	}
	
	/**
	 * 获取医嘱的ID，判断是否应该经过规则判断
	 * @param ordRcd 医嘱记录
	 * @return
	 */
	private boolean isExeOrdRule(DoctorAdviceRecord ordRcd){
		return isExeRule(decideIdFrom(ordRcd.getCompEntities()[0]),this.getKlDef().getObjIds());
	}

}
