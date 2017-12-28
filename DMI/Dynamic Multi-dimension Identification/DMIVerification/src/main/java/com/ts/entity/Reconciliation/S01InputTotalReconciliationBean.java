package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S01InputTotalReconciliationBean 
* @Description: TODO(总额对账入参) 
* @author Lee 刘峰
* @date 2017年3月31日 下午10:42:37 
*
 */
public class S01InputTotalReconciliationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String S01_INP_NO01;//医院编码
	private String S01_INP_NO02;//交易开始时间
	private String S01_INP_NO03;//交易结束时间
	private String S01_INP_NO04;//支付总数:确认交易成功 + 退费 = 支付总数
	private Integer S01_INP_NO05;//确认交易成功记录数
	private Integer S01_INP_NO06;//退费记录条数
	private Double S01_INP_NO07;//费用总额
	private Double S01_INP_NO08;//自费总额(非医保)
	private Double S01_INP_NO09;//药品乙类自负
	private Double S01_INP_NO10;//医保费用
	private Double S01_INP_NO11;//合计报销金额
	private String S01_INP_NO12;//对账类型:0门诊和住院，1门诊，2住院
	private String S01_INP_NO13;//机构
	private String DATA_TYPE;//数据类型
	
	public String getS01_INP_NO01() {
		return S01_INP_NO01;
	}
	public void setS01_INP_NO01(String s01_INP_NO01) {
		S01_INP_NO01 = s01_INP_NO01;
	}
	public String getS01_INP_NO02() {
		return S01_INP_NO02;
	}
	public void setS01_INP_NO02(String s01_INP_NO02) {
		S01_INP_NO02 = s01_INP_NO02;
	}
	public String getS01_INP_NO03() {
		return S01_INP_NO03;
	}
	public void setS01_INP_NO03(String s01_INP_NO03) {
		S01_INP_NO03 = s01_INP_NO03;
	}
	public String getS01_INP_NO04() {
		return S01_INP_NO04;
	}
	public void setS01_INP_NO04(String s01_INP_NO04) {
		S01_INP_NO04 = s01_INP_NO04;
	}
	public Integer getS01_INP_NO05() {
		return S01_INP_NO05;
	}
	public void setS01_INP_NO05(Integer s01_INP_NO05) {
		S01_INP_NO05 = s01_INP_NO05;
	}
	public Integer getS01_INP_NO06() {
		return S01_INP_NO06;
	}
	public void setS01_INP_NO06(Integer s01_INP_NO06) {
		S01_INP_NO06 = s01_INP_NO06;
	}
	public Double getS01_INP_NO07() {
		return S01_INP_NO07;
	}
	public void setS01_INP_NO07(Double s01_INP_NO07) {
		S01_INP_NO07 = s01_INP_NO07;
	}
	public Double getS01_INP_NO08() {
		return S01_INP_NO08;
	}
	public void setS01_INP_NO08(Double s01_INP_NO08) {
		S01_INP_NO08 = s01_INP_NO08;
	}
	public Double getS01_INP_NO09() {
		return S01_INP_NO09;
	}
	public void setS01_INP_NO09(Double s01_INP_NO09) {
		S01_INP_NO09 = s01_INP_NO09;
	}
	public Double getS01_INP_NO10() {
		return S01_INP_NO10;
	}
	public void setS01_INP_NO10(Double s01_INP_NO10) {
		S01_INP_NO10 = s01_INP_NO10;
	}
	public Double getS01_INP_NO11() {
		return S01_INP_NO11;
	}
	public void setS01_INP_NO11(Double s01_INP_NO11) {
		S01_INP_NO11 = s01_INP_NO11;
	}
	public String getS01_INP_NO12() {
		return S01_INP_NO12;
	}
	public void setS01_INP_NO12(String s01_INP_NO12) {
		S01_INP_NO12 = s01_INP_NO12;
	}
	public String getS01_INP_NO13() {
		return S01_INP_NO13;
	}
	public void setS01_INP_NO13(String s01_INP_NO13) {
		S01_INP_NO13 = s01_INP_NO13;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	
}