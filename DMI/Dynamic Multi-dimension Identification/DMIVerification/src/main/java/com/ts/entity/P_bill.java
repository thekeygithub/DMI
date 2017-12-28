package com.ts.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* @ClassName: P_bill 
* @Description: TODO(结算单据) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */

public class P_bill implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID			
	private String IN_ID;//接口信息ID		
	private String BILL_NO;//单据号码		
	private String CODE;//科室代码			
	private String NAME;//科室名称			
	private String DOC_NO;//医生编码		
	private Integer NUM;//收费明细条数		
	
	private List<P_bill_item> pBillItem =  new ArrayList<P_bill_item>(); //收费项目
	
	/** 
	 * ProjectName：API
	 * ClassName：P_bill
	 * Description：TODO(收费项目)
	 * @Copyright：
	 * @Company：
	 * @author：Lee 李世博
	 * @version 
	 * Create Date：2017年3月29日 下午4:30:09
	 */ 
	public void addPbillItem(P_bill_item pb){
		this.pBillItem.add(pb);
	}
	
	public List<P_bill_item> getPBillItem() {
		return pBillItem;
	}
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
	public String getBILL_NO() {
		return BILL_NO;
	}
	public void setBILL_NO(String bILL_NO) {
		BILL_NO = bILL_NO;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDOC_NO() {
		return DOC_NO;
	}
	public void setDOC_NO(String dOC_NO) {
		DOC_NO = dOC_NO;
	}
	public Integer getNUM() {
		return NUM;
	}
	public void setNUM(Integer nUM) {
		NUM = nUM;
	}
	
}