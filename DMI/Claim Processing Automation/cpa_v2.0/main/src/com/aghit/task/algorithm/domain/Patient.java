package com.aghit.task.algorithm.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Patient {

	private long seqId;

	/** 身份证号 */
	private String p_id_no;

	/** 社保卡号 */
	private String p_si_no;

	/** 姓名 */
	private String p_name;

	// 按照住院时间排序的住院结算单数据
	private List<Payment> inpClmList = new ArrayList<Payment>();

	// 处方数据(门诊与门特)
	private Map<String, Payment> prescription = new LinkedHashMap<String, Payment>();

	// key为:数据来源ID_结算单ID
	private Map<String, Payment> clm_payments = new LinkedHashMap<String, Payment>();

	// 医嘱数据_耗材(门诊与门特)
	private Map<String, Payment> doctorAdvice_Consum = new LinkedHashMap<String, Payment>();

	// 医嘱数据_诊疗(门诊与门特)
	private Map<String, Payment> doctorAdvice_Treat = new LinkedHashMap<String, Payment>();

	// -----------------------------------------------------------------

	// 住院的处方数据
	private Map<String, Payment> inpPre = new LinkedHashMap<String, Payment>();

	// key为:数据来源ID_结算单ID
	private Map<String, Payment> clm_inpClm = new LinkedHashMap<String, Payment>();

	// 医嘱数据_耗材(住院)
	private Map<String, Payment> inpOrd_Consum = new LinkedHashMap<String, Payment>();

	// 医嘱数据_诊疗(住院)
	private Map<String, Payment> inpOrd_Treat = new LinkedHashMap<String, Payment>();

	// -----------------------------------------------------------------

	// 结算单-费用清单，key为:数据来源ID_结算单ID
	private Map<String, Diagnosis> pays = new HashMap<String, Diagnosis>();

	// key = 来源(1,门诊门特 3,住院) + 下划线 + 数据来源(1,费用清单 2,医嘱 3,处方)
	// value = 本类中的各种数据，例： payments inpClm inpPre...
	private Map<String, Map<String, Payment>> allData = new HashMap<String, Map<String, Payment>>();

	// 按时间排序的多个结算单,与allData中的数据一致,allData是以结算单为key
	// allData_day以结算时间作为key
	private Map<String, TreeMap<Date, Payment>> allData_day = new HashMap<String, TreeMap<Date, Payment>>();

	private Random r = new Random();

	public Patient() {

		this.allData.put("1_1", prescription);// 处方(门诊与门特)
		this.allData.put("1_2", clm_payments); // 费用(门诊与门特)
		this.allData.put("1_4", doctorAdvice_Treat);// 诊疗(门诊与门特)
		this.allData.put("1_5", doctorAdvice_Consum);// 耗材(门诊与门特)
		
		this.allData.put("2_1", prescription);// 处方(门诊与门特)
		this.allData.put("2_2", clm_payments); // 费用(门诊与门特)
		this.allData.put("2_4", doctorAdvice_Treat);// 诊疗(门诊与门特)
		this.allData.put("2_5", doctorAdvice_Consum);// 耗材(门诊与门特)
		
		this.allData.put("3_1", inpPre);// 处方 (住院)
		this.allData.put("3_2", clm_inpClm); // 清单 (住院)
		this.allData.put("3_4", inpOrd_Treat);// 诊疗 (住院)
		this.allData.put("3_5", inpOrd_Consum);// 耗材 (住院)
		

		TreeMap<Date, Payment> drug = new TreeMap<Date, Payment>();
		TreeMap<Date, Payment> diagnosis = new TreeMap<Date, Payment>();
		TreeMap<Date, Payment> consum = new TreeMap<Date, Payment>();
		TreeMap<Date, Payment> treat = new TreeMap<Date, Payment>();
		this.allData_day.put("1_1", drug);// 处方(门诊与门特)
		this.allData_day.put("1_2", diagnosis); // 费用(门诊与门特)
		this.allData_day.put("1_4", treat); // 诊疗(门诊与门特)
		this.allData_day.put("1_5", consum);// 耗材(门诊与门特)
		

		this.allData_day.put("2_1", drug);// 处方(门诊与门特)
		this.allData_day.put("2_2", diagnosis); // 费用(门诊与门特)
		this.allData_day.put("2_4", treat); // 诊疗(门诊与门特)
		this.allData_day.put("2_5", consum);// 耗材(门诊与门特)
		

		this.allData_day.put("3_1", new TreeMap<Date, Payment>());// 处方 (住院)
		this.allData_day.put("3_2", new TreeMap<Date, Payment>()); // 清单 (住院)
		this.allData_day.put("3_4", new TreeMap<Date, Payment>()); // 诊疗 (住院)
		this.allData_day.put("3_5", new TreeMap<Date, Payment>());// 耗材 (住院)
		
	}

	public Map<String, TreeMap<Date, Payment>> getAllData_day() {
		return allData_day;
	}

	public void setAllData_day(Map<String, TreeMap<Date, Payment>> allDataDay) {
		allData_day = allDataDay;
	}

	public void addPaymentToAllData_day(String key, Payment pay) {
		TreeMap<Date, Payment> temp = this.allData_day.get(key);
		Payment p = null;
		Date date = pay.getClm_date();
		while ((p = temp.get(date)) != null) {
			date = new Date(date.getTime() + r.nextInt(500));
		}

		temp.put(date, pay);
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public String getP_id_no() {
		return p_id_no;
	}

	public Map<String, Map<String, Payment>> getAllData() {
		return allData;
	}

	public void setAllData(Map<String, Map<String, Payment>> allData) {
		this.allData = allData;
	}

	public void setP_id_no(String pIdNo) {
		p_id_no = pIdNo;
	}

	public String getP_si_no() {
		return p_si_no;
	}

	public void setP_si_no(String pSiNo) {
		p_si_no = pSiNo;
	}

//	public String getP_name() {
//		return p_name;
//	}
//
//	public void setP_name(String pName) {
//		p_name = pName;
//	}

	public Map<String, Diagnosis> getPays() {
		return pays;
	}

	public void setPays(Map<String, Diagnosis> pays) {
		this.pays = pays;
	}

	public Map<String, Payment> getPrescription() {
		return prescription;
	}

	public void setPrescription(Map<String, Payment> prescription) {
		this.prescription = prescription;
	}

	public Map<String, Payment> getClm_payments() {
		return clm_payments;
	}

	public void setClm_payments(Map<String, Payment> clmPayments) {
		clm_payments = clmPayments;
	}

	public Map<String, Payment> getClm_inpClm() {
		return clm_inpClm;
	}

	public void setClm_inpClm(Map<String, Payment> clmInpClm) {
		clm_inpClm = clmInpClm;
	}

	public Map<String, Payment> getInpPre() {
		return inpPre;
	}

	public void setInpPre(Map<String, Payment> inpPre) {
		this.inpPre = inpPre;
	}

	public List<Payment> getInpClmList() {
		return inpClmList;
	}

	public void setInpClmList(List<Payment> inpClmList) {
		this.inpClmList = inpClmList;
	}

	public Map<String, Payment> getDoctorAdvice_Consum() {
		return doctorAdvice_Consum;
	}

	public void setDoctorAdvice_Consum(Map<String, Payment> doctorAdviceConsum) {
		doctorAdvice_Consum = doctorAdviceConsum;
	}

	public Map<String, Payment> getDoctorAdvice_Treat() {
		return doctorAdvice_Treat;
	}

	public void setDoctorAdvice_Treat(Map<String, Payment> doctorAdviceTreat) {
		doctorAdvice_Treat = doctorAdviceTreat;
	}

	public Map<String, Payment> getInpOrd_Consum() {
		return inpOrd_Consum;
	}

	public void setInpOrd_Consum(Map<String, Payment> inpOrdConsum) {
		inpOrd_Consum = inpOrdConsum;
	}

	public Map<String, Payment> getInpOrd_Treat() {
		return inpOrd_Treat;
	}

	public void setInpOrdTreat(Map<String, Payment> inpOrdTreat) {
		inpOrd_Treat = inpOrdTreat;
	}

}
