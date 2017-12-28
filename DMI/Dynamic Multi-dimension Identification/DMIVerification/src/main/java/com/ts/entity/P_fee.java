package com.ts.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: P_fee 
* @Description: TODO(费用汇总信息) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_fee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String ID;//主键ID			
	private String IN_ID;//接口信息ID		
	private String SORT;//发票归类			
	private String NAME;//归类名称			
	private Double TOTAL_FEE;//总费用		
	private Double SELF_FEE;//自费费用		
	private Double NEG_FEE;//自负费用		
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getIN_ID() {
		return IN_ID;
	}
	public void setIN_ID(String iN_ID) {
		IN_ID = iN_ID;
	}
	public String getSORT() {
		return SORT;
	}
	public void setSORT(String sORT) {
		SORT = sORT;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public Double getTOTAL_FEE() {
		return TOTAL_FEE;
	}
	public void setTOTAL_FEE(Double tOTAL_FEE) {
		TOTAL_FEE = tOTAL_FEE;
	}
	public Double getSELF_FEE() {
		return SELF_FEE;
	}
	public void setSELF_FEE(Double sELF_FEE) {
		SELF_FEE = sELF_FEE;
	}
	public Double getNEG_FEE() {
		return NEG_FEE;
	}
	public void setNEG_FEE(Double nEG_FEE) {
		NEG_FEE = nEG_FEE;
	}
	
}