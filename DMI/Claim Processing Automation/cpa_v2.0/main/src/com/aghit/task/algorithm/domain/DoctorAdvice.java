package com.aghit.task.algorithm.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 医嘱清单
 * 
 */
public class DoctorAdvice extends Payment{

	/** 生日 */
	private Date patient_birthday;
	/** 患者身份证 */
	private String patient_id_number;
	
	private List<DoctorAdviceRecord> detail=new ArrayList<DoctorAdviceRecord>();

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

	public List<DoctorAdviceRecord> getDetail() {
		return detail;
	}

	public void setDetail(List<DoctorAdviceRecord> detail) {
		this.detail = detail;
	}
 
}