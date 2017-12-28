package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S04InputRefundDetailBean 
* @Description: TODO(退费明细对账接口入口参数) 
* @author Lee 刘峰
* @date 2017年4月11日 下午15:42:37 
*
 */
public class S04InputRefundDetailBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String S04_INP_NO01;//医院编码
	private String S04_INP_NO02;//就诊序号
	private String S04_INP_NO03;//退款流水号
	private String S04_INP_NO04;//交易时间
	private Double S04_INP_NO05;//费用总额
	private Double S04_INP_NO06;//自费总额(非医保)
	private Double S04_INP_NO07;//药品乙类自负
	private Double S04_INP_NO08;//医保费用
	private Double S04_INP_NO09;//合计报销金额
	private String S04_INP_NO10;//对账类型
	private String S04_INP_NO11;//机构
	
	public String getS04_INP_NO01() {
		return S04_INP_NO01;
	}
	public void setS04_INP_NO01(String s04_INP_NO01) {
		S04_INP_NO01 = s04_INP_NO01;
	}
	public String getS04_INP_NO02() {
		return S04_INP_NO02;
	}
	public void setS04_INP_NO02(String s04_INP_NO02) {
		S04_INP_NO02 = s04_INP_NO02;
	}
	public String getS04_INP_NO03() {
		return S04_INP_NO03;
	}
	public void setS04_INP_NO03(String s04_INP_NO03) {
		S04_INP_NO03 = s04_INP_NO03;
	}
	public String getS04_INP_NO04() {
		return S04_INP_NO04;
	}
	public void setS04_INP_NO04(String s04_INP_NO04) {
		S04_INP_NO04 = s04_INP_NO04;
	}
	public Double getS04_INP_NO05() {
		return S04_INP_NO05;
	}
	public void setS04_INP_NO05(Double s04_INP_NO05) {
		S04_INP_NO05 = s04_INP_NO05;
	}
	public Double getS04_INP_NO06() {
		return S04_INP_NO06;
	}
	public void setS04_INP_NO06(Double s04_INP_NO06) {
		S04_INP_NO06 = s04_INP_NO06;
	}
	public Double getS04_INP_NO07() {
		return S04_INP_NO07;
	}
	public void setS04_INP_NO07(Double s04_INP_NO07) {
		S04_INP_NO07 = s04_INP_NO07;
	}
	public Double getS04_INP_NO08() {
		return S04_INP_NO08;
	}
	public void setS04_INP_NO08(Double s04_INP_NO08) {
		S04_INP_NO08 = s04_INP_NO08;
	}
	public Double getS04_INP_NO09() {
		return S04_INP_NO09;
	}
	public void setS04_INP_NO09(Double s04_INP_NO09) {
		S04_INP_NO09 = s04_INP_NO09;
	}
	public String getS04_INP_NO10() {
		return S04_INP_NO10;
	}
	public void setS04_INP_NO10(String s04_INP_NO10) {
		S04_INP_NO10 = s04_INP_NO10;
	}
	public String getS04_INP_NO11() {
		return S04_INP_NO11;
	}
	public void setS04_INP_NO11(String s04_INP_NO11) {
		S04_INP_NO11 = s04_INP_NO11;
	}

}