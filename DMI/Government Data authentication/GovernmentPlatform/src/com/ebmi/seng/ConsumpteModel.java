package com.ebmi.seng;

public class ConsumpteModel {

	private String subject_name;//收费项目名称-col0
	private String subject_grade;//收费项目等级-col2
	private String pay_rate;//自付比例-col3
	private String amount;//数量-col5
	private String sum_moey;//总金额-col7
	
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public String getSubject_grade() {
		return subject_grade;
	}
	public void setSubject_grade(String subject_grade) {
		this.subject_grade = subject_grade;
	}
	public String getPay_rate() {
		return pay_rate;
	}
	public void setPay_rate(String pay_rate) {
		this.pay_rate = pay_rate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSum_moey() {
		return sum_moey;
	}
	public void setSum_moey(String sum_moey) {
		this.sum_moey = sum_moey;
	}
	
}