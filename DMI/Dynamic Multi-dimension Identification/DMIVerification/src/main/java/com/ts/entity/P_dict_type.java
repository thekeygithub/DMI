package com.ts.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: P_dict_type 
* @Description: TODO(数据字典项类型表) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_dict_type implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//数据字典ID		
	private String NAME;//数据项字段名称		
	private String DESCR;//数据项字段描述	
	private Date UPD_DATE;//更新时间		
	private String UPD_USER;//更新人		
	private String MEMO;//备注		
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDESCR() {
		return DESCR;
	}
	public void setDESCR(String dESCR) {
		DESCR = dESCR;
	}
	public Date getUPD_DATE() {
		return UPD_DATE;
	}
	public void setUPD_DATE(Date uPD_DATE) {
		UPD_DATE = uPD_DATE;
	}
	public String getUPD_USER() {
		return UPD_USER;
	}
	public void setUPD_USER(String uPD_USER) {
		UPD_USER = uPD_USER;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	
}