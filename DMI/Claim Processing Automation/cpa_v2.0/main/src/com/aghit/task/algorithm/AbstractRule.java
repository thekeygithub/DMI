package com.aghit.task.algorithm;

import java.util.*;

import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.ComparedEntity;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.util.Constant;

public abstract class AbstractRule {

	public String ruleName; // 规则名称
	public int checkType; // 审核项目
	public long ruleId; // 规则ID
	public int cycleStart; // 周期开始日
	public int cycleEnd; // 周期结束日
	public int cycleFlg; // 是否周期
	
	/**
	 * 就医类型
	 */
	public Set<Integer> medicalType; 
	
	/**
	 * 规则类别,用户定义的,用在break_detail字段中的输出
	 */
	public String category_name; 

	/**
	 * 检查结算单数据的起始时间，包括参与计算的数据和辅助运算的数据,由任务开始时间再减去周期结束日而来 cycleStartTime =
	 * task.startTime - (0-cycleEnd)
	 */
	public Date cycleStartTime;

	//运行模型：0:药品ID，1:分类ID
	private String runModel;
	
	public String getRunModel() {
		return runModel;
	}

	public void setRunModel(String runModel) {
		this.runModel = runModel;
	}

	/**
	 * 无参构造器
	 */
	public AbstractRule() {

	}

	/**
	 * 全参数构造器
	 * 
	 * @param ruleName
	 *            规则名称
	 * @param checkType
	 *            审核项目
	 * @param ruleId
	 *            规则ID
	 * @param cycleStart
	 *            周期开始日
	 * @param cycleEnd
	 *            周期结束日
	 * @param cycleFlg
	 *            是否周期
	 * @param startTime
	 *            结算单起始时间
	  * @param medicalType
	 *            来源(门诊,门特,住院)     
	 */
	public AbstractRule(String ruleName, int checkType, long ruleId,
			int cycleStart, int cycleEnd, int cycleFlg, Date startTime,
			Set<Integer> medicalType,String runModel) {
		super();
		this.ruleName = ruleName;
		this.checkType = checkType;
		this.ruleId = ruleId;
		this.cycleStart = cycleStart;
		this.cycleEnd = cycleEnd;
		this.cycleFlg = cycleFlg;
		this.medicalType = medicalType;
		this.runModel=runModel;
//		if (null == runModel[0]){
//			this.runModel=Constant.RUN_MODEL_ITEM;
//		}else{
//			this.runModel=runModel[0];
//		}

//		calculateStartTime(startTime);// 设置结算单数据的起始时间

	}

	/**
	 * 结算单数据的起始时间
	 * 
	 * @param startTime
	 *            审核方案时间
	 */
	public void calculateStartTime(Date startTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		c.add(Calendar.DAY_OF_MONTH, 0 - this.cycleEnd);
		this.cycleStartTime = c.getTime();
	}

	/**
	 * 得到本规则的名称
	 * 
	 * @return
	 */
	public String getCkeckName() {
		return this.ruleName;
	}

	/**
	 * 本规则的检查方法
	 * 
	 * @param user
	 * @return
	 */
	public List<BreakDetail> check(Patient p) throws Exception {

		List<BreakDetail> bds = null;
		Collection<Payment> list = null;

		if (this.medicalType.contains(Constant.MEDICAL_TYPE_OUTPATIENT)
				|| this.medicalType.contains(Constant.MEDICAL_TYPE_SPECIAL)) {

			// 检查诊断-来自结算
			if (this.checkType == Constant.CHECK_PROJECT_DIAGNOSIS) {
				list = p.getClm_payments().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 查检药材-来自处方
			else if (this.checkType == Constant.CHECK_PROJECT_DRUG) {
				list = p.getPrescription().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 诊疗-来自医嘱
			else if (this.checkType == Constant.CHECK_PROJECT_TREAT) {
				list = p.getDoctorAdvice_Treat().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 耗材-来自医嘱
			else if (this.checkType == Constant.CHECK_PROJECT_CONSUM) {
				list = p.getDoctorAdvice_Consum().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
		
		}

		if (this.medicalType.contains(Constant.MEDICAL_TYPE_HOSPITAL)) {

			// 当本规则的审核类型为诊断时，则检查费用清单里的数据
			if (this.checkType == Constant.CHECK_PROJECT_DIAGNOSIS) {
				list = p.getClm_inpClm().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 查检药材
			else if (this.checkType == Constant.CHECK_PROJECT_DRUG) {
				list = p.getInpPre().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 诊疗-来自医嘱
			else if (this.checkType == Constant.CHECK_PROJECT_TREAT) {
				list = p.getInpOrd_Treat().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
			// 耗材-来自医嘱
			else if (this.checkType == Constant.CHECK_PROJECT_CONSUM) {
				list = p.getInpOrd_Consum().values();
				bds = this.joinList(bds, this.checkPayment(list, p),list);
			}
		}
		
		return bds;
	}

	/**
	 * 把两个都可能为空的list组装成一个
	 * 
	 * @param src
	 * @param target
	 */
	public List<BreakDetail> joinList(List src, List<BreakDetail> target,Collection<Payment> list) {
		if (list!=null && target != null) {
			
			Payment[] ps=list.toArray(new Payment[0]);
			
			for (int i = 0; i < target.size(); i++) {
				BreakDetail bd=target.get(i);
//				int fromType=ps[0].getFromType();
//				bd.setMed_type_id(fromType+""); //就医类型	
				//为了补充数据库与代码的常量定义不一致的问题
				int type=bd.getErrData_type();
				if(type==1){
					bd.setErrData_type(2);
				}else if(type==2){
					bd.setErrData_type(1);
				}
				
			}
			if (src == null) {
				src = new ArrayList(1);
			}
			src.addAll(target);
		}
		return src;
	}

	/**
	 * 返回src与target相同代码的数组
	 */
	public ComparedEntity[] getEqualsCodes(ComparedEntity[] target,
			ComparedEntity[] src) {

		Set<ComparedEntity> list = null;
		for (int i = 0; i < target.length; i++) {
			ComparedEntity com = target[i];
			for (int j = 0; j < src.length; j++) {
				if (null != com.getCode() && com.getCode().equals(src[j].getCode())) {
					if (list == null) {
						list = new HashSet<ComparedEntity>();
					}
					list.add(com);
				}
			}
		}
		if (null != list) {
			return list.toArray(new ComparedEntity[list.size()]);
		} else {
			return null;
		}
	}

	/**
	 * 返回src与target相同代码的数组
	 */
	public ComparedEntity[] getEqualsCodes(String[] src, ComparedEntity[] target) {

		List<ComparedEntity> list = null;
		
		for (int i = 0; i < target.length; i++) {
			for (int j = 0; j < src.length; j++) {
				if (target[i].getCode().equals(src[j])) {
					if (list == null) {
						list = new ArrayList<ComparedEntity>();
					}
					list.add(target[i]);
				}
			}
		}
		if (null != list) {
			return list.toArray(new ComparedEntity[list.size()]);
		} else {
			return null;
		}
	}

//	public String getCodesString(ComparedEntity[] codes) {
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < codes.length; i++) {
//			sb.append(codes[i].getName() + "|" + codes[i].getId() + ",");
//		}
//		return sb.toString();
//	}

	public static String getCodesString(String[] codes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < codes.length; i++) {
			sb.append(codes[i] + ",");
		}
		return sb.toString();
	}
	
	/**
	 * 增加错误信息到一个list中，如果为空则创建它
	 * 
	 * @param list
	 * @param si_clearing_cost_no
	 * @param preId
	 *            处方、医嘱的ID
	 * @param detail
	 */
	public List<BreakDetail> addErrorToList(Patient  patient,List<BreakDetail> list,
			Payment payment, ComparedEntity entity) {

		if (list == null) {
			list = new ArrayList<BreakDetail>(1);
		}

		BreakDetail bd = new BreakDetail();
		String[] s = payment.getSi_clearing_cost_no().split("_");
		bd.setClm_id(Long.parseLong(s[1]));
		bd.setData_src_id(Long.parseLong(s[0]));
		//当类型为诊疗或耗材时，错误类型定义为3（医嘱）
		if (4==payment.getType() || 5==payment.getType()){
			bd.setErrData_type(3);
		}
		else{
			bd.setErrData_type(payment.getType());
		}
		bd.setMed_type_id(payment.getFromType()+"");
		bd.setPre_id(entity.getId());
		bd.setPay_tot(entity.getPay_tot());  //违法金额 
		bd.setErr_item_name(entity.getName()); //审核对像名称
		bd.setClm_Dt(payment.getClm_date()); //结算单日期
		bd.setLgcy_org_code(payment.getH_code()); //医院编号
		bd.setP_id(patient.getP_id_no()); //身份证
		bd.setP_name(payment.getP_name()); //名字
		
		list.add(bd);

		return list;
	}

	/**
	 * 本规则的检查方法
	 * 
	 * @param user
	 * @return
	 */
	public abstract List<BreakDetail> checkPayment(Collection<Payment> list,
			Patient p) throws Exception;

}
