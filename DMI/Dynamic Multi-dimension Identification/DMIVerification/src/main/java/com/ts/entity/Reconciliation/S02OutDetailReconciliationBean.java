package com.ts.entity.Reconciliation;

import java.io.Serializable;

/**
 * 
* @ClassName: S02OutDetailReconciliationBean 
* @Description: TODO(明细对账出参) 
* @author Lee 刘峰
* @date 2017年3月31日 下午10:42:37 
*
 */
public class S02OutDetailReconciliationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer S02_OUT_NO01;//交易状态:0成功，小于0失败
	private String S02_OUT_NO02;//错误信息
	private String S02_OUT_NO03;//写社会保障卡结果
	private String S02_OUT_NO04;//扣银行卡结果
	private String S02_OUT_NO05;//更新后IC卡数据
	private String S02_OUT_NO06;//对账成功标志:0：对帐成功标志-1：表示对帐数据不一致
	private String S02_OUT_NO07;//对帐不一致信息
	
	public Integer getS02_OUT_NO01() {
		return S02_OUT_NO01;
	}
	public void setS02_OUT_NO01(Integer s02_OUT_NO01) {
		S02_OUT_NO01 = s02_OUT_NO01;
	}
	public String getS02_OUT_NO02() {
		return S02_OUT_NO02;
	}
	public void setS02_OUT_NO02(String s02_OUT_NO02) {
		S02_OUT_NO02 = s02_OUT_NO02;
	}
	public String getS02_OUT_NO03() {
		return S02_OUT_NO03;
	}
	public void setS02_OUT_NO03(String s02_OUT_NO03) {
		S02_OUT_NO03 = s02_OUT_NO03;
	}
	public String getS02_OUT_NO04() {
		return S02_OUT_NO04;
	}
	public void setS02_OUT_NO04(String s02_OUT_NO04) {
		S02_OUT_NO04 = s02_OUT_NO04;
	}
	public String getS02_OUT_NO05() {
		return S02_OUT_NO05;
	}
	public void setS02_OUT_NO05(String s02_OUT_NO05) {
		S02_OUT_NO05 = s02_OUT_NO05;
	}
	public String getS02_OUT_NO06() {
		return S02_OUT_NO06;
	}
	public void setS02_OUT_NO06(String s02_OUT_NO06) {
		S02_OUT_NO06 = s02_OUT_NO06;
	}
	public String getS02_OUT_NO07() {
		return S02_OUT_NO07;
	}
	public void setS02_OUT_NO07(String s02_OUT_NO07) {
		S02_OUT_NO07 = s02_OUT_NO07;
	}
	
}