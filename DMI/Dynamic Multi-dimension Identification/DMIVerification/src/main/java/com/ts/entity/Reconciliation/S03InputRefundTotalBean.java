package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S03InputRefundTotalBean 
* @Description: TODO(退费对账接口入口参数) 
* @author Lee 刘峰
* @date 2017年4月11日 上午10:42:37 
*
 */
public class S03InputRefundTotalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String S03_INP_NO01;//医院编码
	private String S03_INP_NO02;//对账开始时间
	private String S03_INP_NO03;//对账结束时间
	private Integer S03_INP_NO04;//退费记录条数
	private Double S03_INP_NO05;//退费费用总额
	private Double S03_INP_NO06;//退费自费总额(非医保)
	private Double S03_INP_NO07;//退费药品乙类自负
	private Double S03_INP_NO08;//退费医保费用
	private Double S03_INP_NO09;//退费合计报销金额
	private String S03_INP_NO10;//对账类型
	private String S03_INP_NO11;//机构
	private String DATA_TYPE;//数据类型
	
	public String getS03_INP_NO01() {
		return S03_INP_NO01;
	}
	public void setS03_INP_NO01(String s03_INP_NO01) {
		S03_INP_NO01 = s03_INP_NO01;
	}
	public String getS03_INP_NO02() {
		return S03_INP_NO02;
	}
	public void setS03_INP_NO02(String s03_INP_NO02) {
		S03_INP_NO02 = s03_INP_NO02;
	}
	public String getS03_INP_NO03() {
		return S03_INP_NO03;
	}
	public void setS03_INP_NO03(String s03_INP_NO03) {
		S03_INP_NO03 = s03_INP_NO03;
	}
	public Integer getS03_INP_NO04() {
		return S03_INP_NO04;
	}
	public void setS03_INP_NO04(Integer s03_INP_NO04) {
		S03_INP_NO04 = s03_INP_NO04;
	}
	public Double getS03_INP_NO05() {
		return S03_INP_NO05;
	}
	public void setS03_INP_NO05(Double s03_INP_NO05) {
		S03_INP_NO05 = s03_INP_NO05;
	}
	public Double getS03_INP_NO06() {
		return S03_INP_NO06;
	}
	public void setS03_INP_NO06(Double s03_INP_NO06) {
		S03_INP_NO06 = s03_INP_NO06;
	}
	public Double getS03_INP_NO07() {
		return S03_INP_NO07;
	}
	public void setS03_INP_NO07(Double s03_INP_NO07) {
		S03_INP_NO07 = s03_INP_NO07;
	}
	public Double getS03_INP_NO08() {
		return S03_INP_NO08;
	}
	public void setS03_INP_NO08(Double s03_INP_NO08) {
		S03_INP_NO08 = s03_INP_NO08;
	}
	public Double getS03_INP_NO09() {
		return S03_INP_NO09;
	}
	public void setS03_INP_NO09(Double s03_INP_NO09) {
		S03_INP_NO09 = s03_INP_NO09;
	}
	public String getS03_INP_NO10() {
		return S03_INP_NO10;
	}
	public void setS03_INP_NO10(String s03_INP_NO10) {
		S03_INP_NO10 = s03_INP_NO10;
	}
	public String getS03_INP_NO11() {
		return S03_INP_NO11;
	}
	public void setS03_INP_NO11(String s03_INP_NO11) {
		S03_INP_NO11 = s03_INP_NO11;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	
}