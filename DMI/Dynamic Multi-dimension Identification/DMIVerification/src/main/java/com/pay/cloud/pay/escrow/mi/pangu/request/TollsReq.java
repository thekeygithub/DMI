package com.pay.cloud.pay.escrow.mi.pangu.request;

import java.math.BigDecimal;
import java.util.List;

/**
 * 门诊明细增加结算、预结算参数
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 上午9:45:55
 */
public class TollsReq extends BaseReq {

	private MzToll mzToll; // 结算
	private List<MzSfmx> sfmxs; // 收费明细
	public MzToll getMzToll() {
		return mzToll;
	}
	public void setMzToll(MzToll mzToll) {
		this.mzToll = mzToll;
	}
	public List<MzSfmx> getSfmxs() {
		return sfmxs;
	}
	public void setSfmxs(List<MzSfmx> sfmxs) {
		this.sfmxs = sfmxs;
	}
	
	public static class MzToll{
		private String ylType; // 医疗类别	
		private String personID; // 个人ID
		private String perCardID;
		private String perName;
		private String mxbID; // 慢性病ID
		private String serialID; // 门诊流水号
		private String dealID;//交易流水号
		private String visDate; // 就诊日期 yyyymmdd
		private String jdjbID; // 诊断疾病编号
		private String jdjbName; // 诊断疾病名称
		private String officeName; // 科室名称
		private String doctorID; // 医生编号
		private String doctorName; // 医生姓名
		private String jbr; // 经办人
		private String operDate; // 经办日期yyyymmddhh24miss
		private String perCardInfo; // 个人卡信息（针对内存卡)
		private String jhsyssType;  //计划生育手术类别
		private String zhsybz;		//账户使用标志
		private String telephone;   //联系电话
		public String getYlType() {
			return ylType;
		}
		public void setYlType(String ylType) {
			this.ylType = ylType;
		}
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
		public String getMxbID() {
			return mxbID;
		}
		public void setMxbID(String mxbID) {
			this.mxbID = mxbID;
		}
		public String getSerialID() {
			return serialID;
		}
		public void setSerialID(String serialID) {
			this.serialID = serialID;
		}
		public String getDealID() {
			return dealID;
		}
		public void setDealID(String dealID) {
			this.dealID = dealID;
		}
		public String getVisDate() {
			return visDate;
		}
		public void setVisDate(String visDate) {
			this.visDate = visDate;
		}
		public String getJdjbID() {
			return jdjbID;
		}
		public void setJdjbID(String jdjbID) {
			this.jdjbID = jdjbID;
		}
		public String getJdjbName() {
			return jdjbName;
		}
		public void setJdjbName(String jdjbName) {
			this.jdjbName = jdjbName;
		}
		public String getOfficeName() {
			return officeName;
		}
		public void setOfficeName(String officeName) {
			this.officeName = officeName;
		}
		public String getDoctorID() {
			return doctorID;
		}
		public void setDoctorID(String doctorID) {
			this.doctorID = doctorID;
		}
		public String getDoctorName() {
			return doctorName;
		}
		public void setDoctorName(String doctorName) {
			this.doctorName = doctorName;
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
		public String getPerCardInfo() {
			return perCardInfo;
		}
		public void setPerCardInfo(String perCardInfo) {
			this.perCardInfo = perCardInfo;
		}
		public String getJhsyssType() {
			return jhsyssType;
		}
		public void setJhsyssType(String jhsyssType) {
			this.jhsyssType = jhsyssType;
		}
		public String getZhsybz() {
			return zhsybz;
		}
		public void setZhsybz(String zhsybz) {
			this.zhsybz = zhsybz;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		
	}
	
	public static class MzSfmx{
		private String cfID; // 处方ID
		private String sfType; // 收费类别01西药,02中成药,03中草药,09非药品
		private String centerID; // 中心ID
		private String yyCode; // 医院编码
		private String projectName; // 项目名称
		private BigDecimal price; // 单价
		private BigDecimal num; // 数量
		private BigDecimal sum; // 金额
		private String unit; // 单位
		private String norms; // 规格
		private String dosage; // 剂型
		private String dose; // 每次用量
		private String frequency; // 执行频次
		private String days; // 执行天数
		private String from; // 产地
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
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public BigDecimal getNum() {
			return num;
		}
		public void setNum(BigDecimal num) {
			this.num = num;
		}
		public BigDecimal getSum() {
			return sum;
		}
		public void setSum(BigDecimal sum) {
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
