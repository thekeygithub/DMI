package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S04OutRefundDetailBean 
* @Description: TODO(退费明细对账接口返回参数)
* @author Lee 刘峰
* @date 2017年4月11日 下午15:42:37 
*
 */
public class S04OutRefundDetailBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer S04_OUT_NO01;//交易状态
	private String S04_OUT_NO02;//错误信息
	private String S04_OUT_NO03;//写社会保障卡结果
	private String S04_OUT_NO04;//扣银行卡结果
	private String S04_OUT_NO05;//更新后IC卡数据
	private String S04_OUT_NO06;//对账成功标志
	private String S04_OUT_NO07;//对帐不一致信息
	
	public Integer getS04_OUT_NO01() {
		return S04_OUT_NO01;
	}
	public void setS04_OUT_NO01(Integer s04_OUT_NO01) {
		S04_OUT_NO01 = s04_OUT_NO01;
	}
	public String getS04_OUT_NO02() {
		return S04_OUT_NO02;
	}
	public void setS04_OUT_NO02(String s04_OUT_NO02) {
		S04_OUT_NO02 = s04_OUT_NO02;
	}
	public String getS04_OUT_NO03() {
		return S04_OUT_NO03;
	}
	public void setS04_OUT_NO03(String s04_OUT_NO03) {
		S04_OUT_NO03 = s04_OUT_NO03;
	}
	public String getS04_OUT_NO04() {
		return S04_OUT_NO04;
	}
	public void setS04_OUT_NO04(String s04_OUT_NO04) {
		S04_OUT_NO04 = s04_OUT_NO04;
	}
	public String getS04_OUT_NO05() {
		return S04_OUT_NO05;
	}
	public void setS04_OUT_NO05(String s04_OUT_NO05) {
		S04_OUT_NO05 = s04_OUT_NO05;
	}
	public String getS04_OUT_NO06() {
		return S04_OUT_NO06;
	}
	public void setS04_OUT_NO06(String s04_OUT_NO06) {
		S04_OUT_NO06 = s04_OUT_NO06;
	}
	public String getS04_OUT_NO07() {
		return S04_OUT_NO07;
	}
	public void setS04_OUT_NO07(String s04_OUT_NO07) {
		S04_OUT_NO07 = s04_OUT_NO07;
	}
	
}