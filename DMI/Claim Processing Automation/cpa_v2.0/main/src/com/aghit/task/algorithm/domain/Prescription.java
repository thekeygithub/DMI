package com.aghit.task.algorithm.domain;

import java.util.*;

/**
 * 处方清单
 */

public class Prescription extends Payment{

	private long cml_id; //结算单
	
	private List<PrescriptionRecord> detail=new ArrayList<PrescriptionRecord>();;  //药品名细

	
	/** 生日 */
	private Date patient_birthday;
	/** 患者身份证 */
	private String patient_id_number;
	
	
	public Date getPatient_birthday() {
		return patient_birthday;
	}

	public void setPatient_birthday(Date patientBirthday) {
		patient_birthday = patientBirthday;
	}

	public String getPatient_id_number() {
		return patient_id_number;
	}

	public void setPatient_id_number(String patientIdNumber) {
		patient_id_number = patientIdNumber;
	}

	public long getCml_id() {
		return cml_id;
	}

	public void setCml_id(long cmlId) {
		cml_id = cmlId;
	}

	public List<PrescriptionRecord> getDetail() {
		return detail;
	}

	public void setDetail(List<PrescriptionRecord> detail) {
		this.detail = detail;
	}
}