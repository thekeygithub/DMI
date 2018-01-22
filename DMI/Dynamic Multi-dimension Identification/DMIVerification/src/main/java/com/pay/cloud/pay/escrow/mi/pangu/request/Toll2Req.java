package com.pay.cloud.pay.escrow.mi.pangu.request;

import java.util.List;
/**
 * 药店收费明细增加预结算或结算
 * @author yanjie.ji
 * @date 2016年11月18日 
 * @time 下午6:04:10
 */
public class Toll2Req extends BaseReq {
	
	private YdToll ydToll;
	
	private List<YdSfmx> sfmxs;
	

	public YdToll getYdToll() {
		return ydToll;
	}

	public void setYdToll(YdToll ydToll) {
		this.ydToll = ydToll;
	}

	public List<YdSfmx> getSfmxs() {
		return sfmxs;
	}

	public void setSfmxs(List<YdSfmx> sfmxs) {
		this.sfmxs = sfmxs;
	}

	public static class YdToll{
		private String personID; // 个人ID
		private String perCardID;//身份证号
		private String perName;//姓名
		private String serialID; // 门诊流水号
		private String visDate; // 就诊日期 yyyymmdd
		private String jbr; // 经办人
		private String operDate; // 经办日期yyyymmddhh24miss
		private String telephone;   //联系电话
		public String getPersonID() {
			return personID;
		}
		public void setPersonID(String personID) {
			this.personID = personID;
		}
		public String getPerCardID() {
			return perCardID;
		}
		public void setPerCardID(String perCardID) {
			this.perCardID = perCardID;
		}
		public String getPerName() {
			return perName;
		}
		public void setPerName(String perName) {
			this.perName = perName;
		}
		public String getSerialID() {
			return serialID;
		}
		public void setSerialID(String serialID) {
			this.serialID = serialID;
		}
		public String getVisDate() {
			return visDate;
		}
		public void setVisDate(String visDate) {
			this.visDate = visDate;
		}
		public String getJbr() {
			return jbr;
		}
		public void setJbr(String jbr) {
			this.jbr = jbr;
		}
		public String getOperDate() {
			return operDate;
		}
		public void setOperDate(String operDate) {
			this.operDate = operDate;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
	}
	
	public static class YdSfmx{
		private String cfID;
		private String sfType;
		private String centerID;
		private String yyCode;
		private String projectName;
		private String price;
		private String num;
		private String sum;
		private String unit;
		private String norms;
		private String dosage;
		private String dose;
		private String frequency;
		private String days;
		private String from;
		public String getCfID() {
			return cfID;
		}
		public void setCfID(String cfID) {
			this.cfID = cfID;
		}
		public String getSfType() {
			return sfType;
		}
		public void setSfType(String sfType) {
			this.sfType = sfType;
		}
		public String getCenterID() {
			return centerID;
		}
		public void setCenterID(String centerID) {
			this.centerID = centerID;
		}
		public String getYyCode() {
			return yyCode;
		}
		public void setYyCode(String yyCode) {
			this.yyCode = yyCode;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getSum() {
			return sum;
		}
		public void setSum(String sum) {
			this.sum = sum;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getNorms() {
			return norms;
		}
		public void setNorms(String norms) {
			this.norms = norms;
		}
		public String getDosage() {
			return dosage;
		}
		public void setDosage(String dosage) {
			this.dosage = dosage;
		}
		public String getDose() {
			return dose;
		}
		public void setDose(String dose) {
			this.dose = dose;
		}
		public String getFrequency() {
			return frequency;
		}
		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}
		public String getDays() {
			return days;
		}
		public void setDays(String days) {
			this.days = days;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
	}

}
