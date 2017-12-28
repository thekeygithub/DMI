package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S02InputDetailReconciliationBean 
* @Description: TODO(明细对账入参) 
* @author Lee 刘峰
* @date 2017年3月31日 下午10:42:37 
*
 */
public class S02InputDetailReconciliationBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String S02_INP_NO01;//医院编码
	private String S02_INP_NO02;//就诊号
	private String S02_INP_NO03;//结算流水号
	private String S02_INP_NO04;//交易时间
	private Double S02_INP_NO05;//费用总额
	private Double S02_INP_NO06;//自费总额(非医保)
	private String S02_INP_NO07;//药品乙类自负
	private Double S02_INP_NO08;//医保费用
	private Double S02_INP_NO09;//合计报销金额
	private String S02_INP_NO10;//对账类型
	private String S02_INP_NO11;//机构
	
	public String getS02_INP_NO01() {
		return S02_INP_NO01;
	}
	public void setS02_INP_NO01(String s02_INP_NO01) {
		S02_INP_NO01 = s02_INP_NO01;
	}
	public String getS02_INP_NO02() {
		return S02_INP_NO02;
	}
	public void setS02_INP_NO02(String s02_INP_NO02) {
		S02_INP_NO02 = s02_INP_NO02;
	}
	public String getS02_INP_NO03() {
		return S02_INP_NO03;
	}
	public void setS02_INP_NO03(String s02_INP_NO03) {
		S02_INP_NO03 = s02_INP_NO03;
	}
	public String getS02_INP_NO04() {
		return S02_INP_NO04;
	}
	public void setS02_INP_NO04(String s02_INP_NO04) {
		S02_INP_NO04 = s02_INP_NO04;
	}
	public Double getS02_INP_NO05() {
		return S02_INP_NO05;
	}
	public void setS02_INP_NO05(Double s02_INP_NO05) {
		S02_INP_NO05 = s02_INP_NO05;
	}
	public Double getS02_INP_NO06() {
		return S02_INP_NO06;
	}
	public void setS02_INP_NO06(Double s02_INP_NO06) {
		S02_INP_NO06 = s02_INP_NO06;
	}
	public String getS02_INP_NO07() {
		return S02_INP_NO07;
	}
	public void setS02_INP_NO07(String s02_INP_NO07) {
		S02_INP_NO07 = s02_INP_NO07;
	}
	public Double getS02_INP_NO08() {
		return S02_INP_NO08;
	}
	public void setS02_INP_NO08(Double s02_INP_NO08) {
		S02_INP_NO08 = s02_INP_NO08;
	}
	public Double getS02_INP_NO09() {
		return S02_INP_NO09;
	}
	public void setS02_INP_NO09(Double s02_INP_NO09) {
		S02_INP_NO09 = s02_INP_NO09;
	}
	public String getS02_INP_NO10() {
		return S02_INP_NO10;
	}
	public void setS02_INP_NO10(String s02_INP_NO10) {
		S02_INP_NO10 = s02_INP_NO10;
	}
	public String getS02_INP_NO11() {
		return S02_INP_NO11;
	}
	public void setS02_INP_NO11(String s02_INP_NO11) {
		S02_INP_NO11 = s02_INP_NO11;
	}
	
}