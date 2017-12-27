package com.aghit.task.algorithm.domain.rule.klbase;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.aghit.task.algorithm.domain.Diagnosis;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.exp.klbase.KLElemExpression;
import com.aghit.task.common.entity.klbase.KlDef;
import com.aghit.task.common.entity.klbase.KlElemDef;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ConvertUtil;


/**
 * 审核类型为诊断情况
 */
public class DiagCheckPayment extends CheckPayment {

	
	public DiagCheckPayment(int checkType, long ruleId, int cycleStart,
			int cycleEnd, int cycleFlg, KlDef klDef, List<KLElemExpression> exps) {
		super(checkType, ruleId, cycleStart, cycleEnd, cycleFlg, klDef, exps);
	}
	
	private static Logger log = Logger.getLogger(DiagCheckPayment.class);

	@Override
	public List<BreakDetail> checkPayment(Collection<Payment> list, Patient p,
			Payment pay) {
		List<BreakDetail>  msgLst = new ArrayList<BreakDetail>();
		Diagnosis clmDiag = (Diagnosis) pay;
		ComparedEntity[] diagLst = clmDiag.getCompEntities();
		for(ComparedEntity ce : diagLst){
			
			long diagId = Long.valueOf(ce.getCode()).longValue();// 诊断ID
			// 如果当前审核项目不属于本规则，直接判断下一条
			if(Arrays.binarySearch(this.getKlDef().getObjIds(),diagId ) < 0) continue;
			
			// 右侧多条件表达式结果计算
			ErrMessage right = calcRightExp(clmDiag,diagId,p);
			
			if (false == right.isLegal()) {
				// 写错误信息
				msgLst.add(writeError(pay.getSi_clearing_cost_no(),
						pay.getType(), pay.getFromType(), ce.getId(),
						right.getErrDes(), ce.getName(), pay.getPay_tot(),
						p.getP_id_no(), pay.getP_name(),
						pay.getClm_date(), pay.getH_code(), this.getKlDef().getK_id(), right.getErrElemID(), right.getErrCateid(),right.getErr_item()));
			}
		}
		return msgLst;
	}
	
	//获得比较对象
	private Map<Long,Long> findTerm(ComparedEntity[] ids,KlElemDef elemDef,int conType,
			long checkId,boolean flag,Calendar startDate,Calendar endDate){
		
		int checkType = Constant.CHECK_PROJECT_DIAGNOSIS;
		Calendar thisDate = Calendar.getInstance();
		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		for(ComparedEntity ce : ids){
			//审核项目类型与条件类型相同，审核项目id与条件类型项目id相同，排除条件为true，则该条件项目不参加审核 drug_id
			if (elemDef.getEx_obj_flag() == Constant.EX_OBJ_FLAG_YES && checkType == conType && true == Long.toString(checkId).equals(ce.getCode())){
				continue;
			}
			//条件项目为诊断时  或无日期区间,或条件日期为空时全部返回 
			if (false == flag || null == ce.getExe_date()){ 
				tmpList.put(ce.getId(), Long.valueOf(ce.getCode()));
			}else{
				thisDate.setTime(ce.getExe_date());
				if (thisDate.compareTo(startDate) >= 0 && thisDate.compareTo(endDate) <= 0) {
					tmpList.put(ce.getId(), Long.valueOf(ce.getCode()));
				}
			}	
		}
		
		return tmpList;	
	}
	
	//获取可参考的五类术语集合
	private Map<Long,Long> findComparedTerm(int conType,Diagnosis clmdiag, Patient p, KlElemDef elemDef, long diagId){

		Map<Long,Long> tmpList = new HashMap<Long,Long>();	// 结果暂存list
		//日期区间
		Map<String,Calendar> map;
		Calendar startDate = null;
		Calendar endDate = null;
        //是否加入日期区间
	    boolean flag =false;
		if (null != elemDef.getIntv_range() && !"".equals(elemDef.getIntv_range()) && null != clmdiag.getClm_date()){
			flag = true;
			map=this.calDateArea(clmdiag.getClm_date(), elemDef.getIntv_range(), elemDef.getIntv_unit());
			startDate = map.get("start");
			endDate = map.get("end");
		}
		
		String dataKey =  clmdiag.getFromType() + "_" + conType;
		// 无周期的时候，参考值只选结算单的数据
		if (cycleFlg == Constant.DISABLED) {
			Map<String, Payment> tmp = p.getAllData().get(dataKey);
			if(null != tmp.get( clmdiag.getSi_clearing_cost_no())){
//				return tmpList.toArray(new Long[tmpList.size()]);
//			}
			    ComparedEntity[] ids = tmp.get( clmdiag.getSi_clearing_cost_no()).getCompEntities();   
			    tmpList.putAll(findTerm(ids, elemDef, conType, diagId,
			    		 flag, startDate, endDate));			
			}
		}else{
			// 有周期的时候
			TreeMap<Date, Payment> tmp = p.getAllData_day().get(dataKey);
			Date checkStartDt = DateUtils.addDays(CPADateUtil.getDateFirst(clmdiag.getClm_date()), -cycleEnd);//设置周期开始日
			Date checkEndDt = DateUtils.addDays(CPADateUtil.getDateLast(clmdiag.getClm_date()), -cycleStart);//设置周期结束日
			
			// 获取比较周期之内的数据，全部周期数据
			Map<Date,Payment> compareMap = tmp.subMap(checkStartDt, checkEndDt);
			Iterator<Entry<Date,Payment>> it = compareMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Date,Payment> en = it.next();
				ComparedEntity[] ids =  en.getValue().getCompEntities();   
				tmpList.putAll(findTerm(ids, elemDef, conType, diagId,
				     flag, startDate, endDate));
			}
		}
		
//		Set<Long> rtn = new TreeSet<Long>();
//		for(int i = 0; i< tmpList.size();i++){
//			rtn.add(tmpList.get(i).longValue());
//		}	
//		return rtn.toArray(new Long[rtn.size()]);
		
//		Map<Long,Long[]> rtn = new HashMap<Long,Long[]>();
//		for (Long key : tmpList.keySet()) {
//			rtn.put(key, (tmpList.get(key).toArray(new Long[rtn.size()])));
//		}	
//		return rtn;
		return tmpList;
	}
	/**
	 * 诊断时，处理右侧多表达式
	 * @param clmdiag 诊断结算单
	 * @param diagId 诊断ID
	 * @param p 患者实体
	 * @return
	 */
	private ErrMessage calcRightExp(Diagnosis clmdiag, long diagId, Patient p){
		
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
				elParams.add(findComparedTerm(exp.getType(), clmdiag, p,exp.getElemDef(),diagId));
				break;
			// 医院
//			case Constant.CONDITION_PROJECT_HOSPITAL:
//				elParams.add(clmdiag.getH_code());
//				break;
			// 医院等级
			case Constant.ELEM_TYPE_HOSP_RANK:
				elParams.add(clmdiag.getH_tier());
				break;
//			// 医师职称
//			case Constant.CONDITION_PROJECT_DOCTOR_TITLE:
//				elParams.add(clmdiag.getDoc_lvl());
//				break;
			// 性别
			case Constant.ELEM_TYPE_GENDER:
				elParams.add(clmdiag.getP_gend_id());
				break;
			// 年龄
			case Constant.ELEM_TYPE_AGE:
				elParams.add(clmdiag.getAge());
				break;
			// 支付类别
			case Constant.ELEM_TYPE_PAY_TYPE:
				elParams.add(clmdiag.getMi_schm_id());
				break;
			//就诊类型
			case Constant.ELEM_TYPE_FROMTYPE:
				elParams.add(clmdiag.getFromType());
				break;
		    //总费用
			case Constant.ELEM_TYPE_TOTAL_FEE:
				elParams.add(clmdiag.getPay_tot());
				break;
			//住院天数
			case Constant.ELEM_TYPE_HLOS:
				elParams.add(clmdiag.getInpatient_day());
				break;
			//医院类型 中医 专科等
			case Constant.ELEM_TYPE_HOSP_TYPE:
				elParams.add(clmdiag.getHosp_org_type_id());
				break;
			//结算方式
			case Constant.ELEM_TYPE_SETTLEMENT_TYPE:
				elParams.add(clmdiag.getCle_form_id());
				break;
				//参保人员类别
			case Constant.ELEM_TYPE_P_MI_CAT:
				elParams.add(clmdiag.getP_mi_cat_id());
				break;
			default:
				// 对于不支持的类型，直接删除，并记日志
				// 不支持日剂量、时间重复、超量、禁忌症、适应症
				log.error("知识库规则，诊断审核时，不支持的条件类型：" + exp.getElemDef().getElem_type_id());
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
//			exp.setElem_state(exp.evaluate(elParams.toArray()));
		}
		err=getErrMessage();
		return err;
	}

}
