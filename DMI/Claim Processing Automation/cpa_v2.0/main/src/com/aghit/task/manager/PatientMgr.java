package com.aghit.task.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aghit.base.BaseService;
import com.aghit.task.algorithm.domain.ComparedEntity;
import com.aghit.task.algorithm.domain.Diagnosis;
import com.aghit.task.algorithm.domain.DoctorAdvice;
import com.aghit.task.algorithm.domain.DoctorAdviceRecord;
import com.aghit.task.algorithm.domain.Hospital;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.algorithm.domain.Payment;
import com.aghit.task.algorithm.domain.Prescription;
import com.aghit.task.algorithm.domain.PrescriptionRecord;
import com.aghit.task.common.entity.CpaTask;
import com.aghit.task.common.service.LoadMidDataService;
import com.aghit.task.common.service.impl.LoadInpDataServiceImpl;
import com.aghit.task.common.service.impl.LoadOupDataServiceImpl;
import com.aghit.task.common.service.impl.LoadOusDataServiceImpl;
import com.aghit.task.util.CPADateUtil;
import com.aghit.task.util.Constant;
import com.aghit.task.util.ConvertUtil;

public class PatientMgr extends BaseService {

	private static PatientMgr mgr = new PatientMgr();

//	private MessageService msgService = MsgServiceFactory.getMessageService();

	public static PatientMgr getInstance() {
		return mgr;
	}

	/**
	 * 转为数据库结果为诊断对象
	 * 
	 * @param m
	 * @return
	 */
	private Diagnosis getDiagnosis(Map<String, Object> m) {

		Diagnosis d = new Diagnosis();
		d.setData_src_id(Long.parseLong(m.get("DATA_SRC_ID").toString()));// 数据来源ID
		d.setClm_id(Long.parseLong(m.get("CLM_ID").toString()));// 结算单ID
		d.setClm_date((Date) m.get("CLM_DATE"));// 结算日期
		d.setEnterDate((Date) m.get("ENTR_DATE"));// 入院日期
		d.setLeaveDate((Date)m.get("DISCH_DATE"));
		d.setP_id_no(m.get("LGCY_P_CODE").toString());// 患者ID

		d.setSi_clearing_cost_no(m.get("DATA_SRC_ID") + "_" + m.get("CLM_ID"));// 来源id_结算单编码号

		d.setAge(CPADateUtil.calcAge((Date) m.get("P_DOB"), d.getEnterDate()));// 年龄
		
		
        d.setH_tier(ConvertUtil.getLong(m.get("MI_HOSP_TIER_ID"))); // 医院等级
        d.setDr_lvl(ConvertUtil.getLong(m.get("DR_LVL")));// 医师职称
        d.setMi_schm_id(ConvertUtil.getLong(m.get("MI_SCHM_ID")));// 支付类别
        d.setP_gend_id(ConvertUtil.getLong(m.get("P_GEND_ID"))); // 性别
		
		if (null !=m.get("SIGN") && 1==Integer.parseInt(m.get("SIGN").toString())){ //是否为当前审核结算单
			d.setSign(true);
		}

		d.setPay_tot(ConvertUtil.getDouble(m.get("PAY_TOT"))); // 金额
		d.setH_code(ConvertUtil.getString(m.get("LGCY_ORG_CODE"))); // 医院编号
		
		d.setP_name(ConvertUtil.getString(m.get("P_NAME"))); //患者姓名
		if (null != d.getLeaveDate()){      //住院天数
			 d.setInpatient_day(CPADateUtil.calDayDiff(d.getLeaveDate(), d.getEnterDate()));
		}else{
			 d.setInpatient_day(CPADateUtil.calDayDiff(d.getClm_date(), d.getEnterDate()));
			 log.error("clm_id:" + d.getClm_id() + "出院日期为空。");
		}
		d.setHosp_org_type_id(ConvertUtil.getLong(m.get("HOSP_ORG_TYPE_ID"))); //医院类型
		d.setCle_form_id(ConvertUtil.getLong(m.get("CLE_FORM_ID")));           //就诊结算方式
		d.setP_mi_cat_id(ConvertUtil.getLong(m.get("P_MI_CAT_ID")));           //参保人员类别
		List<ComparedEntity> codes = new ArrayList<ComparedEntity>();

		Object diagObj = m.get("DIAG_LIST");
		// 如果诊断对象不为空，并且为集合List类型
		if (null != diagObj && List.class.isInstance(diagObj)) {
			List<Map<String, Object>> diagLst = (List<Map<String, Object>>) diagObj;
			for (Map<String, Object> drow : diagLst) {
				codes.add(new ComparedEntity(drow.get("DIAG_ID").toString(),
						drow.get("DIAG_NAME").toString(), Long.valueOf(drow
								.get("ID").toString()), d.getPay_tot(),d.getClm_date(),0));
			}
		}

		d.setCompEntities(codes.toArray(new ComparedEntity[0]));
		return d;
	}

	/**
	 * 从mid_inp_clm（患者住院结算单）中查找供患者进行入院出院时间校验的基本信息
	 * 
	 * @param patients
	 * @param start
	 * @param end
	 * @param task
	 * @throws Exception
	 */
	public void loadHospitalData(final Map<Long, Patient> patients,
			long start, long end, final CpaTask task) throws Exception {
		// 查询的SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.data_src_id, ");// 数据来源ID
		sql.append("       t.clm_id, ");// 结算单ID
		sql.append("       t.clm_date, ");// 结算日期
		sql.append("       t.LGCY_ORG_CODE, ");// 医院编码
		sql.append("       t.p_gend_id, ");// 患者性别
		sql.append("       t.p_dob, ");// 患者生日
		sql.append("       t.p_age, ");// 患者年龄
		sql.append("       t.lgcy_dr_code, ");// 医师编码
		sql.append("       t.pay_tot, ");// 支付总额
		sql.append("       t.mi_schm_id, ");// 支付类别
		sql.append("       t.p_name, ");// 患者姓名
		sql.append("	   t.LGCY_P_CODE, ");// 患者ID
		sql.append("	   p.seqid,  ");// 序号ID
		sql.append(" 	   t.entr_date, ");// 入院日期
		sql.append("       t.disch_date ");// 出院日期
		sql.append("  FROM mid_inp_clm t, cpa_handled_patient p ");
		sql.append(" WHERE t.LGCY_P_CODE = p.patient_id_number ");
		sql.append("   AND p.seqid >= ? ");
		sql.append("   AND p.seqid <= ? ");
		sql.append("   AND t.clm_date >= ? ");
		sql.append("   AND t.clm_date <= ? ");
//		sql.append("   AND t.MTS_FLAG = '1' ");
//	    sql.append(" AND NOT EXISTS (SELECT 1 FROM mid_inp_pre WHERE mts_flag <> '1' AND clm_id = t.clm_id AND data_src_id = t.data_src_id) ");
//        sql.append(" AND NOT EXISTS (SELECT 1 FROM mid_inp_ord WHERE mts_flag <> '1' AND clm_id = t.clm_id AND data_src_id = t.data_src_id) ");
		sql.append("   ORDER BY t.entr_date");

		StringBuilder daySql = new StringBuilder(
				"select max(t1.INTERVAL) cou from CPA_Interval_Check_Rule t1, CPA_Scenario_Rule_Relation t2")
				.append(" where t1.rule_id = t2.rule_id and t2.SCENARIO_ID = ?");

		SqlRowSet srs = getJdbcService().findByQuery(daySql.toString(),
				new Object[] { task.getScenarioId() });
		int cou = 0;
		if (srs.next()) {
			cou = srs.getInt("COU");
		}

		if (cou > 0) {
			Object[] params = new Object[] { start, end,
					DateUtils.addDays(task.getTaskStart(), -cou),
					task.getTaskEnd() };

			List<Payment> d = getJdbcService().findListByQuery(sql.toString(),
					params, new RowMapper<Payment>() {
						@Override
						public Payment mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							Hospital hos = new Hospital();

							hos.setData_src_id(rs.getLong("data_src_id"));// 数据来源ID
							hos.setClm_id(rs.getLong("clm_id"));// 结算单ID
							hos.setClm_date(rs.getTimestamp("clm_date"));
							hos.setType(4);
							hos.setAge(rs.getInt("p_age"));
							hos.setEnterDate(rs.getTimestamp("entr_date"));
							hos.setLeaveDate(rs.getTimestamp("disch_date"));
							long seqId = rs.getLong("seqid");
							hos.setH_code(rs.getString("lgcy_org_code"));
							hos.setSi_clearing_cost_no(rs.getLong("data_src_id")
									+ "_" + rs.getLong("clm_id"));

							// 以下是2014-01-08新加
							hos.setPay_tot(rs.getDouble("pay_tot"));// 支付总额
							hos.setP_name(rs.getString("p_name"));
							hos.setErr_item_name("分解住院");
							hos.setP_id(rs.getString("LGCY_P_CODE"));

							// if(hos.getClm_date().getTime() >=
							// task.getStartTimeSearch().getTime()){
							hos.setSign(true);
							// }

							Patient p = patients.get(seqId);
							p.getInpClmList().add(hos);

							return hos;
						}
					});
		}

	}

	/**
	 * 加载患者的住院数据
	 * 
	 * @param patients
	 * @param start
	 * @param end
	 * @param task
	 * @throws Exception
	 */
	public void loadPatiData(Map<Long, Patient> patients, long start,
			long end, CpaTask task) throws Exception {

		// 1. 加载住院数据
		LoadMidDataService svc = getSpringServiceById("loadInpDataService",
				LoadInpDataServiceImpl.class);
		Map<String, Collection<Map<String, Object>>> search = svc
				.executeFetchMidData(task.getStartTimeSearch(), task
						.getTaskEnd(), start, end,task.getDataSrcId(),task.getExeType());

		// 1.1 处理住院诊断数据
		this.loadPatiClm(search, patients, task,
						Constant.MEDICAL_TYPE_HOSPITAL);
		// 1.2 处理住院医嘱（诊疗、耗材）数据
		this.loadPatiOrd(search, patients, task,
						Constant.MEDICAL_TYPE_HOSPITAL);
		// 1.3 处理住院处方（药品）数据
		this.loadPatiPre(search, patients, task,
						Constant.MEDICAL_TYPE_HOSPITAL);

		// 2. 加载门诊数据
		svc = getSpringServiceById("loadOupDataService",
				LoadOupDataServiceImpl.class);
		search = svc.executeFetchMidData(task.getStartTimeSearch(), task
				.getTaskEnd(), start, end, task.getDataSrcId(),task.getExeType());

		// 2.1 处理门诊诊断数据
		this.loadPatiClm(search, patients, task,
				Constant.MEDICAL_TYPE_OUTPATIENT);
		// 2.2 处理门诊医嘱（诊疗、耗材）数据
		this.loadPatiOrd(search, patients, task,
				Constant.MEDICAL_TYPE_OUTPATIENT);
		// 2.3 处理门诊处方（药品）数据
		this.loadPatiPre(search, patients, task,
				Constant.MEDICAL_TYPE_OUTPATIENT);

		// 3.加载门特数据
		svc = getSpringServiceById("loadOusDataService",
				LoadOusDataServiceImpl.class);
		search = svc.executeFetchMidData(task.getStartTimeSearch(), task
				.getTaskEnd(), start, end, task.getDataSrcId(),task.getExeType());

		// 3.1 处理门特诊断数据
		this.loadPatiClm(search, patients, task, Constant.MEDICAL_TYPE_SPECIAL);
		// 3.2 处理门特医嘱（诊疗、耗材）数据
		this.loadPatiOrd(search, patients, task, Constant.MEDICAL_TYPE_SPECIAL);
		// 3.3 处理门特处方（药品）数据
		this.loadPatiPre(search, patients, task, Constant.MEDICAL_TYPE_SPECIAL);
	}

	/**
	 * 处理诊断数据
	 * 
	 * @param search
	 * @param patients
	 * @param task
	 * @param fromType
	 */
	public void loadPatiClm(
			Map<String, Collection<Map<String, Object>>> search,
			Map<Long, Patient> patients, CpaTask task, int fromType) {

		String dataType = Constant.MID_DATA_TYPE_CLM;
		Iterator<Map<String, Object>> it = search.get(dataType).iterator();
		while (it.hasNext()) {
			Map<String, Object> rs_m = it.next();
			long seqId = ConvertUtil.getLong(rs_m.get("SEQID"));
			Diagnosis d = this.getDiagnosis(rs_m);
			// 如果本结算单的时间大于等于运算的周期时间，则设设本结算单为参与运算
			if (d.getClm_date().getTime() >= task.getTaskStart().getTime() && true == d.getSign()) {
				d.setSign(true);
			}else{
				d.setSign(false);
			}
				
			d.setType(ConvertUtil.getInt(dataType)); // 类型
			d.setFromType(fromType);
			Patient patient = patients.get(seqId);
			// 如果诊断是门特也按1处理,因为门诊与门特是放在一起的
			String key = (fromType == Constant.MEDICAL_TYPE_SPECIAL ? Constant.MEDICAL_TYPE_OUTPATIENT
					: fromType)
					+ "_" + d.getType();
			patient.getAllData().get(key).put(d.getSi_clearing_cost_no(), d);
			patient.addPaymentToAllData_day(key, d); // 增加到allData_day中
		}
	}

	/**
	 * 处理医嘱的耗材、诊疗数据
	 * 
	 * @param search
	 * @param patients
	 * @param task
	 * @param fromType
	 */
	public void loadPatiOrd(
			Map<String, Collection<Map<String, Object>>> search,
			Map<Long, Patient> patients, CpaTask task, int fromType) {

		Iterator<Map<String, Object>> it = search.get(
				Constant.MID_DATA_TYPE_ORD).iterator();
		Set<DoctorAdvice> doctorAdvice_Temp = new HashSet<DoctorAdvice>();
		while (it.hasNext()) {
			Map<String, Object> rs = it.next();
			DoctorAdviceRecord ord = new DoctorAdviceRecord();
			ord.setClm_date((Date) rs.get("CLM_DATE"));// 结算日期
			ord.setEnterDate((Date) rs.get("ENTR_DATE"));// 入院日期
			ord.setLeaveDate((Date) rs.get("DISCH_DATE")); //出院日期
			ord.setPatient_id_number(ConvertUtil.getString(rs
					.get("LGCY_P_CODE")));// 身份ID
			ord.setSi_clearing_cost_no(rs.get("DATA_SRC_ID") + "_"
					+ rs.get("CLM_ID"));// 来源id_结算单编码号

			ord.setPay_tot(ConvertUtil.getDouble(rs.get("PAY_TOT"))); // 金额

			ord.setOrder_amount(ConvertUtil.getDouble(rs.get("DRUG_AMT")));// 数量
			ord.setOrder_name(ConvertUtil.getString(rs.get("LGCY_CHRG_NAME")));// 标准化前名称
			ord.setOrder_type(ConvertUtil.getString(rs.get("ORD_TYPE_ID")));// 医嘱类型
			ord.setOrd_id(ConvertUtil.getLong(rs.get("ORD_ID")));// 医嘱ID
			ord.setAg_id(ConvertUtil.getLong(rs.get("AG_ID")));// 标准化ID
			ord.setOrd_exe_date(ConvertUtil.getDate(rs.get("ORD_EXE_DATE")));// 执行时间ID
			ord.setDr_lvl_detail(ConvertUtil.getLong(rs.get("DR_LVL_DETAIL"))); //医师级别
			ord.setA_mi_flag(ConvertUtil.getInt(rs.get("A_MI_FLAG"))); //医保标识
			ord.setOrd_unit_price(ConvertUtil.getDouble(rs.get("ORD_UNIT_PRICE"))); //单价
			ord.setRownum(ConvertUtil.getLong(rs.get("ROWNUM"))); //医嘱序号
			ord.setOrd_fee(ConvertUtil.getDouble(rs.get("ORD_FEE")));
			ord.setChk_flag(ConvertUtil.getInt(rs.get("CHK_FLAG")));//是否参与审核
			ord.setTotal(ConvertUtil.getMap(rs.get("TOTAL")));

			// 诊疗84104；耗材84105 
			if (Constant.ORD_TYPE_ID_TREAT.equals(ord.getOrder_type())) {
				ord.setType(Constant.CHECK_PROJECT_TREAT);// 诊疗
				ord.setCompEntities(new ComparedEntity[] { new ComparedEntity(
						ConvertUtil.getString(rs.get("AG_ID")), ConvertUtil
								.getString(rs.get("LGCY_CHRG_NAME")), ConvertUtil
								.getLong(rs.get("ORD_ID").toString()), ord
								.getPay_tot(),ord.getOrd_exe_date(),ord.getRownum())});
			} else if (Constant.ORD_TYPE_ID_CONSUM.equals(ord.getOrder_type())) {
				ord.setType(Constant.CHECK_PROJECT_CONSUM);// 耗材
				ord.setCompEntities(new ComparedEntity[] { new ComparedEntity(
						ConvertUtil.getString(rs.get("AG_ID")), ConvertUtil
								.getString(rs.get("LGCY_CHRG_NAME")), ConvertUtil
								.getLong(rs.get("ORD_ID").toString()), ord
								.getPay_tot(),ord.getOrd_exe_date(),ord.getRownum())});
//				ord.setCompEntities(ConvertUtil.crtCpdEntity(ConvertUtil.getString(rs.get("AG_ID")), ConvertUtil
//						.getString(rs.get("LGCY_CHRG_NAME")), ConvertUtil
//						.getLong(rs.get("ORD_ID").toString()), ord.getPay_tot(), 
//						Constant.CHECK_PROJECT_CONSUM, ConvertUtil.getString(rs.get("CSUM_TREE_NAME_ID"))));
			}else {
				continue;
			}

			ord.setFromType(fromType);

			ord.setAge(CPADateUtil.calcAge((Date) rs.get("P_DOB"), ord
					.getEnterDate()));// 年龄
			ord.setH_code(ConvertUtil.getString(rs.get("LGCY_ORG_CODE")));// 医院ID
			ord.setH_tier(ConvertUtil.getLong(rs.get("MI_HOSP_TIER_ID")));// 医院等级
			ord.setDr_lvl(ConvertUtil.getLong(rs.get("DR_LVL")));// 医师职称
			ord.setMi_schm_id(ConvertUtil.getLong(rs.get("MI_SCHM_ID")));// 支付类别
			ord.setP_gend_id(ConvertUtil.getLong(rs.get("P_GEND_ID"))); // 性别
			
			ord.setP_name(ConvertUtil.getString(rs.get("P_NAME"))); //患者姓名
			if (null != ord.getLeaveDate()){  //住院天数
				ord.setInpatient_day(CPADateUtil.calDayDiff(ord.getLeaveDate(), ord.getEnterDate()));
			}else{
				ord.setInpatient_day(CPADateUtil.calDayDiff(ord.getClm_date(), ord.getEnterDate()));
				 log.error("clm_id:" + rs.get("CLM_ID") + "出院日期为空。");
			}
			ord.setHosp_org_type_id(ConvertUtil.getLong(rs.get("HOSP_ORG_TYPE_ID"))); //医院类型
			ord.setCle_form_id(ConvertUtil.getLong(rs.get("CLE_FORM_ID")));           //就诊结算方式
			ord.setP_mi_cat_id(ConvertUtil.getLong(rs.get("P_MI_CAT_ID")));           //参保人员类别
			DoctorAdvice doctorAdvice = null;

			long seqId = ConvertUtil.getLong(rs.get("seqid"));
			String key = fromType + "_" + ord.getType();
			Patient patient = patients.get(seqId);
			Map<String, Payment> pays = patient.getAllData().get(key);
			Payment temp = pays.get(ord.getSi_clearing_cost_no());
			if (temp == null) {
				doctorAdvice = new DoctorAdvice();
				doctorAdvice.setClm_date(ord.getClm_date());
				doctorAdvice.setPatient_id_number(ord.getPatient_id_number());
				doctorAdvice.setSi_clearing_cost_no(ord
						.getSi_clearing_cost_no());

				doctorAdvice.setAge(ord.getAge());// 年龄
				doctorAdvice.setH_code(ord.getH_code());// 医院ID
				doctorAdvice.setH_tier(ord.getH_tier());// 医院等级
				doctorAdvice.setDr_lvl(ord.getDr_lvl());// 医师职称
				doctorAdvice.setMi_schm_id(ord.getMi_schm_id());// 支付类别
				doctorAdvice.setP_gend_id(ord.getP_gend_id()); // 性别
				doctorAdvice.setP_name(ord.getP_name()); //患者姓名
				doctorAdvice.setPay_tot(ord.getPay_tot()); //总费用
				doctorAdvice.setInpatient_day(ord.getInpatient_day()); //住院天数
				doctorAdvice.setHosp_org_type_id(ord.getHosp_org_type_id()); //医院类型
				doctorAdvice.setCle_form_id(ord.getCle_form_id()); //就诊结算方式
				doctorAdvice.setP_mi_cat_id(ord.getP_mi_cat_id()); //参保人员类别
				doctorAdvice.setEnterDate(ord.getEnterDate()); //入院日期
				doctorAdvice.setLeaveDate(ord.getLeaveDate()); //出院日期
				
				doctorAdvice.setType(ord.getType());
				doctorAdvice.setFromType(ord.getFromType());
				pays.put(ord.getSi_clearing_cost_no(), doctorAdvice); // 增加到allData中
				patient.addPaymentToAllData_day(key, doctorAdvice); // 增加到allData_day中
				doctorAdvice_Temp.add(doctorAdvice);
			} else {
				doctorAdvice = (DoctorAdvice) temp;
			}
			doctorAdvice.getDetail().add(ord);

			// 如果本结算单的时间大于等于运算的周期时间，则设置参与运算标志
			if (ord.getClm_date().getTime() >= task.getTaskStart().getTime() 
					&& null != rs.get("SIGN") && 1 == Integer.parseInt(rs.get("SIGN").toString())) {
				ord.setSign(true);
				doctorAdvice.setSign(true);
			}
		}
		Iterator<DoctorAdvice> itt = doctorAdvice_Temp.iterator();
		while (itt.hasNext()) {
			DoctorAdvice da = itt.next();
			ComparedEntity[] cs = new ComparedEntity[da.getDetail().size()];
			for (int i = 0; i < cs.length; i++) {
				cs[i] = da.getDetail().get(i).getCompEntities()[0];
			}
			da.setCompEntities(cs);
		}
	}

	/**
	 * 处理医嘱的药品信息
	 * 
	 * @param search
	 * @param patients
	 * @param task
	 * @param fromType
	 */
	public void loadPatiPre(
			Map<String, Collection<Map<String, Object>>> search,
			Map<Long, Patient> patients, CpaTask task, int fromType) {

		Set<Prescription> ps = new HashSet<Prescription>();

		String dataType = Constant.MID_DATA_TYPE_PRE;
		Iterator<Map<String, Object>> it = search.get(dataType).iterator();

		while (it.hasNext()) {
			Map<String, Object> rs = it.next();
			PrescriptionRecord pre = new PrescriptionRecord();
			pre.setClm_date((Date) rs.get("CLM_DATE"));// 结算日期
			pre.setEnterDate((Date) rs.get("ENTR_DATE"));// 入院日期
			pre.setLeaveDate((Date) rs.get("DISCH_DATE")); //出院日期
			pre.setSi_clearing_cost_no(ConvertUtil
					.getLong(rs.get("DATA_SRC_ID"))
					+ "_" + ConvertUtil.getLong(rs.get("CLM_ID")));// 来源id_结算单编码号
			pre.setPay_tot(ConvertUtil.getDouble(rs.get("PAY_TOT"))); // 金额
//			// 判断中西药类别，不同处理；没有类别时，按照西药对待处理
//			if(Constant.DRUG_CHINA.equals(ConvertUtil.getString(rs.get("ATC_TERM_TYPE_ID")))){
//				// 判断为中药时的分支
//				String key = "Z_" + ConvertUtil.getString(rs.get("ATC_NAME_ID"));
//				pre.setCompEntities(ConvertUtil.crtCpdEntity(ConvertUtil.getString(rs.get("DRUG_ID")), ConvertUtil
//						.getString(rs.get("LGCY_DRUG_DESC")), ConvertUtil.getLong(rs.get("PRE_ID")), 
//						pre.getPay_tot(),Constant.CHECK_PROJECT_DRUG, key));
//			}else{
//				// 判断为西药时的分支
//				String key = "X_" + ConvertUtil.getString(rs.get("ATC_NAME_ID")) + "_" + 
//						ConvertUtil.getString(rs.get("DRUG_FORM_ID"));
//				pre.setCompEntities(ConvertUtil.crtCpdEntity(ConvertUtil.getString(rs.get("DRUG_ID")), ConvertUtil
//						.getString(rs.get("LGCY_DRUG_DESC")), ConvertUtil.getLong(rs.get("PRE_ID")), 
//						pre.getPay_tot(),Constant.CHECK_PROJECT_DRUG, key));
//			}
			pre.setDrug_id(ConvertUtil.getLong(rs.get("DRUG_ID")));// 药品ID
			pre.setDrug_adm_appro(ConvertUtil.getLong(rs.get("DRUG_ADM_APPRO")));// 给药途径
			pre.setDrug_usg_val(ConvertUtil.getDouble(rs.get("DRUG_USG_VAL")));// 药品用法频次
			pre.setDrug_usg_val(ConvertUtil.getLong(rs.get("DRUG_USG_UNIT")));// 药品用法频次单位
			pre.setDrug_usg_amt(ConvertUtil.getDouble(rs.get("DRUG_USG_AMT")));// 次用量
			pre.setPre_Id(ConvertUtil.getLong(rs.get("PRE_ID")));// 处方ID
			pre.setOrd_exe_date(ConvertUtil.getDate(rs.get("ORD_EXE_DATE"))); // 处方执行时间
			pre.setDr_lvl_detail(ConvertUtil.getLong(rs.get("DR_LVL_DETAIL"))); //医师级别
			pre.setA_mi_flag(ConvertUtil.getInt(rs.get("A_MI_FLAG"))); //医保标识
			pre.setDrug_unit_price(ConvertUtil.getDouble(rs.get("DRUG_UNIT_PRICE"))); //单价
			pre.setLgcy_pre_code(ConvertUtil.getString(rs.get("LGCY_PRE_CODE"))); //处方单code
			pre.setPre_seq_no(ConvertUtil.getString(rs.get("PRE_SEQNO")));   //药品组Code
			pre.setRownum(ConvertUtil.getLong(rs.get("ROWNUM")));     //医嘱序号
			pre.setDrug_fee(ConvertUtil.getDouble(rs.get("DRUG_FEE"))); //医嘱费用
			pre.setChk_flag(ConvertUtil.getInt(rs.get("CHK_FLAG")));  //是否审核
			pre.setTotal(ConvertUtil.getMap(rs.get("TOTAL")));
			
			pre.setDrug_amount(ConvertUtil.getDouble(rs.get("DRUG_AMT")));// 数量
			pre.setLgcy_drug_desc(ConvertUtil.getString(rs
					.get("LGCY_DRUG_DESC")));// 标准化话前的药品名
			pre.setType(ConvertUtil.getInt(dataType));// 就医类别
			pre.setFromType(fromType);

			pre.setAge(CPADateUtil.calcAge((Date) rs.get("P_DOB"), pre
					.getEnterDate()));// 年龄
			pre.setH_code(ConvertUtil.getString(rs.get("LGCY_ORG_CODE")));// 医院ID
			pre.setH_tier(ConvertUtil.getLong(rs.get("MI_HOSP_TIER_ID")));// 医院等级
			pre.setDr_lvl(ConvertUtil.getLong(rs.get("DR_LVL")));// 医师职称
			pre.setMi_schm_id(ConvertUtil.getLong(rs.get("MI_SCHM_ID")));// 支付类别
			pre.setP_gend_id(ConvertUtil.getLong(rs.get("P_GEND_ID"))); // 性别
			pre.setP_name(ConvertUtil.getString(rs.get("P_NAME"))); //患者姓名
			if (null != pre.getLeaveDate()){  //住院天数
				pre.setInpatient_day(CPADateUtil.calDayDiff(pre.getLeaveDate(), pre.getEnterDate()));
			}else{
				pre.setInpatient_day(CPADateUtil.calDayDiff(pre.getClm_date(), pre.getEnterDate()));
				 log.error("clm_id:" + rs.get("CLM_ID") + "出院日期为空。");
			}
			pre.setHosp_org_type_id(ConvertUtil.getLong(rs.get("HOSP_ORG_TYPE_ID"))); //医院类型
			pre.setCle_form_id(ConvertUtil.getLong(rs.get("CLE_FORM_ID")));           //就诊结算方式
			pre.setP_mi_cat_id(ConvertUtil.getLong(rs.get("P_MI_CAT_ID")));           //参保人员类别
			pre.setCompEntities(new ComparedEntity[] { new ComparedEntity(
					ConvertUtil.getString(rs.get("DRUG_ID")), pre.getLgcy_drug_desc(), pre.getPre_Id(),
					pre.getPay_tot(),pre.getOrd_exe_date(),pre.getRownum(),pre.getLgcy_pre_code(),pre.getPre_seq_no())});

			String patient_id_number = ConvertUtil.getString(rs
					.get("LGCY_P_CODE"));// 身份ID

			long seqId = ConvertUtil.getLong(rs.get("seqid"));
			// 如果诊断是门特也按1处理,因为门诊与门特是放在一起的
			String key = (fromType == Constant.MEDICAL_TYPE_SPECIAL ? Constant.MEDICAL_TYPE_OUTPATIENT
					: fromType)
					+ "_" + pre.getType();
			Patient patient = patients.get(seqId);
			Map<String, Payment> map = patient.getAllData().get(key);
			Prescription p = null;
			Payment temp = map.get(pre.getSi_clearing_cost_no());
			if (temp == null) {
				p = new Prescription();
				p.setClm_date(pre.getClm_date());
				p.setPatient_id_number(patient_id_number);
				p.setSi_clearing_cost_no(pre.getSi_clearing_cost_no());
				p.setType(pre.getType());
				p.setFromType(pre.getFromType());

				p.setAge(pre.getAge());// 年龄
				p.setH_code(pre.getH_code());// 医院ID
				p.setH_tier(pre.getH_tier());// 医院等级
				p.setDr_lvl(pre.getDr_lvl());// 医师职称
				p.setMi_schm_id(pre.getMi_schm_id());// 支付类别
				p.setP_gend_id(pre.getP_gend_id()); // 性别
				p.setP_name(pre.getP_name());   //患者姓名
				
				p.setPay_tot(pre.getPay_tot()); //总费用
				p.setInpatient_day(pre.getInpatient_day()); //住院天数
				p.setHosp_org_type_id(pre.getHosp_org_type_id()); //医院类型
				p.setCle_form_id(pre.getCle_form_id()); //就诊结算方式
				p.setP_mi_cat_id(pre.getP_mi_cat_id()); //就诊结算方式
				p.setEnterDate(pre.getEnterDate()); //入院日期
				p.setLeaveDate(pre.getLeaveDate()); //出院日期
				

				map.put(pre.getSi_clearing_cost_no(), p);
				patient.addPaymentToAllData_day(key, p); // 增加到allData_day中
				ps.add(p);
			} else {
				p = (Prescription) temp;
			}

			p.getDetail().add(pre);

			// 如果本结算单的时间大于等于运算的周期时间，则设置参与运算标志
			if (pre.getClm_date().getTime() >= task.getTaskStart().getTime() 
					&& null != rs.get("SIGN") && 1 == Integer.parseInt(rs.get("SIGN").toString())) {
				pre.setSign(true);
				p.setSign(true);
			}
		}

		Iterator<Prescription> itt = ps.iterator();
		while (itt.hasNext()) {
			Prescription p = itt.next();
			ComparedEntity[] cs = new ComparedEntity[p.getDetail().size()];
			for (int i = 0; i < cs.length; i++) {
				cs[i] = p.getDetail().get(i).getCompEntities()[0];
			}
			p.setCompEntities(cs);
		}
	}

	public Patient getPatient(long seqid) throws Exception {

		String sql = "select PATIENT_NAME,PATIENT_ID_NUMBER"
				+ " from cpa_handled_patient t where t.seqid=?";
		Map<String, Object> map = this.getJdbcService().queryForMap(sql,
				new Long[] { seqid });

		if (map != null && map.size() > 0) {
			Patient p = new Patient();
			p.setSeqId(seqid);
			p.setP_id_no(ConvertUtil.getString(map.get("PATIENT_ID_NUMBER")));
//			p.setP_name(ConvertUtil.getString(map.get("PATIENT_NAME")));
			return p;
		}

		return null;
	}
}
