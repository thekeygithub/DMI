package com.ts.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: P_dict 
* @Description: TODO(数据字典表) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_dict implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//数据字典ID					
	private String D_ID;//数据项类型ID					
	private String D_KEY;//数据项KEY					
	private String D_VALUE;//数据项VALUE				
	private Integer ORDERS;//数据项排序					
	private Integer IS_DISABLE;//停用标记 1停用，0启用		
	private Date UPD_TIME;//更新时间					
	private String UPD_USER;//更新人					
	private String MEMO;//备注
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getD_ID() {
		return D_ID;
	}
	public void setD_ID(String d_ID) {
		D_ID = d_ID;
	}
	public String getD_KEY() {
		return D_KEY;
	}
	public void setD_KEY(String d_KEY) {
		D_KEY = d_KEY;
	}
	public String getD_VALUE() {
		return D_VALUE;
	}
	public void setD_VALUE(String d_VALUE) {
		D_VALUE = d_VALUE;
	}
	public Integer getORDERS() {
		return ORDERS;
	}
	public void setORDERS(Integer oRDERS) {
		ORDERS = oRDERS;
	}
	public Integer getIS_DISABLE() {
		return IS_DISABLE;
	}
	public void setIS_DISABLE(Integer iS_DISABLE) {
		IS_DISABLE = iS_DISABLE;
	}
	public Date getUPD_TIME() {
		return UPD_TIME;
	}
	public void setUPD_TIME(Date uPD_TIME) {
		UPD_TIME = uPD_TIME;
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