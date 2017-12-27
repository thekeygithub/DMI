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
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.Prescription;
import com.aghit.task.algorithm.domain.PrescriptionRecord;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ConvertUtil;

/**
 * 审核项目为药品情况
 */
public class DrugCheckPayment extends CheckPayment {

	/**
	 * 构造函数
	 */
	public DrugCheckPayment(int checkType, long ruleId, int cycleStart,
			int cycleEnd, int cycleFlg, KlDef klDef, List<KLElemExpression> exps) {
		super(checkType, ruleId, cycleStart, cycleEnd, cycleFlg, klDef, exps);
	}

	private static Logger log = Logger.getLogger(DrugCheckPayment.class);
	
	@Override
	public List<BreakDetail> checkPayment(Collection<Payment> list, Patient p ,Payment pay) {
		
		List<BreakDetail>  msgLst = new ArrayList<BreakDetail>();
		Prescription clmPre = (Prescription) pay;
		List<PrescriptionRecord> preLst = clmPre.getDetail();
		Iterator<PrescriptionRecord> localIt = preLst.iterator();
		while(localIt.hasNext()){
			PrescriptionRecord preRcd = localIt.next();
			
			// 如果当前审核项目不属于本规则，或者 当前审核项目不参与审核 直接判断下一条 
			if(!isExeDrugRule(preRcd) || Constant.CHK_FLAG_YES != preRcd.getChk_flag()) continue;
			
			// 右侧多条件表达式结果计算
			ErrMessage right = calcRightExp(clmPre,preRcd,p,list);
			if (false == right.isLegal()){
				msgLst.add(writeError(preRcd.getSi_clearing_cost_no(), preRcd.getType(), preRcd.getFromType(),preRcd.getPre_Id(),
						right.getErrDes(), preRcd.getLgcy_drug_desc(), preRcd.getDrug_fee(), p.getP_id_no(), preRcd.getP_name(),
						preRcd.getClm_date(), preRcd.getH_code(),this.getKlDef().getK_id(),right.getErrElemID(),right.getErrCateid(),right.getErr_item()));
			}
		}
		
		return msgLst;
	}
	
	//获取比较对象
	private Map<Long,Long> findTerm(ComparedEntity[] ids,KlElemDef elemDef,int conType,
			PrescriptionRecord pre,boolean flag,Calendar startDate,Calendar endDate){
		
		int checkType = Constant.CHECK_PROJECT_DRUG;
		long drugId = pre.getDrug_id();
		long pre_id = pre.getPre_Id();
		long rownum = pre.getRownum();
		String pre_code = pre.getLgcy_pre_code();
		String pre_seqno = pre.getPre_seq_no();
		
		Calendar conDate = Calendar.getInstance();
		Calendar checkDate = Calendar.getInstance();
		if (null != pre.getOrd_exe_date()){
			checkDate.setTime(pre.getOrd_exe_date());
		}
		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		
		for(ComparedEntity ce : ids){
			//审核项目类型与条件类型相同，审核项目id与条件类型项目id相同，排除条件为true，则该条件项目不参加审核 drug_id
			if (elemDef.getEx_obj_flag() == Constant.EX_OBJ_FLAG_YES && checkType == conType && true == Long.toString(drugId).equals(ce.getCode())){
				continue;
			}
				
			//当非诊断，审核项目类型与条件类型相同，审核项目pre_id与条件类型项目Pre_id相同 排除审核项目
			if (checkType == conType && pre_id == ce.getId()){
				continue;
			}
			
			//当审核项目和条件项目都为药品时 同一处方判断时，若处方单编码不同，则不参加审核
			if(elemDef.getRange_flag() == Constant.RANGE_FLAG_PRE && checkType == conType && !pre_code.equals(ce.getPre_code())){
				continue;
			}
			
			//当审核项目和条件项目都为药品时 同一药品组判断时，若处方单编码不同，则不参加审核
			if(elemDef.getRange_flag() == Constant.RANGE_FLAG_SEQ && checkType == conType && (!pre_code.equals(ce.getPre_code()) || !pre_seqno.equals(ce.getSeq_no()))){
				continue;
			}
			
			//条件项目为诊断时  或无日期区间,或条件日期为空时全部返回 
			if (false == flag || null == ce.getExe_date() || null == pre.getOrd_exe_date()){ 
//				tmpList.addAll(ConvertUtil.longArr2List(decideIdFrom(ce)));
//				tmpList.put(ce.getId(), ConvertUtil.longArr2List(decideIdFrom(ce)));
				tmpList.put(ce.getId(), Long.valueOf(ce.getCode()));
			}else{
				conDate.setTime(ce.getExe_date());
	    		if (conDate.compareTo(startDate)>=0 && conDate.compareTo(endDate) <=0){
	    			 //非诊断 审核项目类型与条件项目类型相同，日期相同，核项目id与条件类型项目id相同 则取rownum小的，大的过滤掉，防止日期相同项目都被报违规。
	    			if (conDate.compareTo(checkDate)==0 && checkType == conType && true == Long.toString(drugId).equals(ce.getCode()) && rownum < ce.getRownum()){
	    				continue;
	    			}
//	    			tmpList.addAll(ConvertUtil.longArr2List(decideIdFrom(ce)));
//	    			tmpList.put(ce.getId(), ConvertUtil.longArr2List(decideIdFrom(ce)));
	    			tmpList.put(ce.getId(),  Long.valueOf(ce.getCode()));
	    		}
			}	
		}
		
		return tmpList;	
	}
	
	//获取可参考的五类术语集合
	private Map<Long,Long> findComparedTerm(int conType,Prescription clmPre,PrescriptionRecord pre, Patient p, KlElemDef elemDef){

//		List<Long> tmpList = new ArrayList<Long>();	// 结果暂存list
		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		//日期区间
		Map<String,Calendar> map;
		Calendar startDate = null;
		Calendar endDate = null;
//		是否加入日期区间
	    boolean flag =false;
		if (null != elemDef.getIntv_range() && !"".equals(elemDef.getIntv_range()) && null != pre.getOrd_exe_date()){
			flag = true;
			map=calDateArea(pre.getOrd_exe_date(), elemDef.getIntv_range(), elemDef.getIntv_unit());
			startDate = map.get("start");
			endDate = map.get("end");
		}

		String dataKey = clmPre.getFromType() + "_" + conType;
		// 无周期的时候，参考值只选结算单的数据
		if (cycleFlg == Constant.DISABLED) {
			Map<String, Payment> tmp = p.getAllData().get(dataKey);
			if(null != tmp.get(clmPre.getSi_clearing_cost_no())){
//				return tmpList.toArray(new Long[tmpList.size()]);
//				return tmpList;
//			}else{
				ComparedEntity[] ids = tmp.get(clmPre.getSi_clearing_cost_no()).getCompEntities();   
			    tmpList.putAll(findTerm(ids, elemDef, conType, pre, flag, startDate, endDate));		
			}
		    	
		}else{
			// 有周期的时候
			TreeMap<Date, Payment> tmp = p.getAllData_day().get(dataKey);
			Date checkStartDt = DateUtils.addDays(CPADateUtil.getDateFirst(clmPre.getClm_date()), -cycleEnd);//设置周期开始日
			Date checkEndDt = DateUtils.addDays(CPADateUtil.getDateLast(clmPre.getClm_date()), -cycleStart);//设置周期结束日
			
			// 获取比较周期之内的数据，全部周期数据
			Map<Date,Payment> compareMap = tmp.subMap(checkStartDt, checkEndDt);
			Iterator<Entry<Date,Payment>> it = compareMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Date,Payment> en = it.next();
				ComparedEntity[] ids =  en.getValue().getCompEntities();   
				tmpList.putAll(findTerm(ids, elemDef, conType, pre, flag, startDate, endDate));
			}
		}
		
//		Map<Long,Long> rtn = new HashMap<Long,Long>();
//		for (Long key : tmpList.keySet()) {
//			rtn.put(key, (tmpList.get(key).toArray(new Long[rtn.size()])));
//		}	
		return tmpList;
	}
	
	//药品处理右侧多表达式
	private ErrMessage calcRightExp(Prescription clmPre, PrescriptionRecord pre, Patient p, Collection<Payment> list){

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
				elParams.add(findComparedTerm(exp.getType(), clmPre, pre, p, exp.getElemDef()));
				break;
//				// 医院
//				case Constant.CONDITION_PROJECT_HOSPITAL:
//					elParams.add(clmPre.getH_code());
//					break;
			// 医院等级
			case Constant.ELEM_TYPE_HOSP_RANK:
				elParams.add(clmPre.getH_tier());
				break;
//				// 医师职称
//				case Constant.CONDITION_PROJECT_DOCTOR_TITLE:
//					elParams.add(String.valueOf(pre.getDr_lvl()));
//					break;
			// 性别
			case Constant.ELEM_TYPE_GENDER:
				elParams.add(clmPre.getP_gend_id());
				break;
			// 年龄
			case Constant.ELEM_TYPE_AGE:
				elParams.add(clmPre.getAge());
				break;
			// 支付类别
			case Constant.ELEM_TYPE_PAY_TYPE:
				elParams.add(clmPre.getMi_schm_id());
				break;
		    //就诊类型
			case Constant.ELEM_TYPE_FROMTYPE:
				elParams.add(clmPre.getFromType());
				break;
		    //总费用
			case Constant.ELEM_TYPE_TOTAL_FEE:
				elParams.add(clmPre.getPay_tot());
				break;
			//住院天数
			case Constant.ELEM_TYPE_HLOS:
				elParams.add(clmPre.getInpatient_day());
				break;
			//医院类型 中医 专科等
			case Constant.ELEM_TYPE_HOSP_TYPE:
				elParams.add(clmPre.getHosp_org_type_id());
				break;
			//明细医师级别
			case Constant.ELEM_TYPE_DR_LVL_DETAIL:
				elParams.add(pre.getDr_lvl_detail());
				break;
			//医保标识
			case Constant.ELEM_TYPE_MI_FLAG:
				elParams.add(pre.getA_mi_flag());
				break;
		    //单价
			case Constant.ELEM_TYPE_ITEM_PRICE:
				elParams.add(pre.getDrug_unit_price());
				break;
			//结算方式
			case Constant.ELEM_TYPE_SETTLEMENT_TYPE:
				elParams.add(pre.getCle_form_id());
				break;	
			//参保人员类别
			case Constant.ELEM_TYPE_P_MI_CAT:
				elParams.add(clmPre.getP_mi_cat_id());
				break;
			//同类项目总金额	
			case Constant.ELME_TYPE_ITEM_FEE:
				String valFee;
				if (null == exp.getElemDef().getIntv_range()){ //整个结算单
					 valFee = "_PRECLMFEE";
				}else{
					 valFee = "_PREDAYFEE";
				}	
				if (null != pre.getTotal()){
					String key = "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + valFee;
					elParams.add(ConvertUtil.getDouble(pre.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			//同类项目总数量	
			case Constant.ELEM_TYPE_ITEM_COUNT:
				String valCount;
				if (null == exp.getElemDef().getIntv_range()){ //整个结算单
					 valCount = "_PRECLMCNT";
				}else{
					 valCount = "_PREDAYCNT";
				}	
				if (null != pre.getTotal()){
					String key =  "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + valCount;
					elParams.add(ConvertUtil.getDouble(pre.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			//同类项目总数量减住院天数	
			case Constant.ELEM_TYPE_ITEM_INDAYS:
				if (null != pre.getTotal()){
					String key =  "_" + exp.getElemDef().getK_id()+ "_" + exp.getElemDef().getElem_id() + "_PRECNTDAYS";
					elParams.add(ConvertUtil.getDouble(pre.getTotal().get(key)));	
				}else{
					elParams.add(-1);
				}
				break;
			default:
				log.error("知识库规则，药品审核时，不支持的条件类型：" + exp.getElemDef().getElem_type_id());
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
		
	private boolean isExeDrugRule(PrescriptionRecord preRcd){
		return isExeRule(decideIdFrom(preRcd.getCompEntities()[0]),this.getKlDef().getObjIds());
	}
		
	

}
