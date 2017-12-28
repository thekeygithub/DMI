package com.ts.entity;

import java.io.Serializable;
import java.util.Date;
/**
 *  P_dll_server  DLL服务配置
 * @author fus
 *
 */
public class P_dll_server implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ID; //主键
	private String HOSP_NAME; //医院名称
	private String HOSP_ID; //医院ID
	private String DLL_ADDRESS; //dll调用地址
	private Date CREATE_DATE; //创建时间
	private String LOCAL_IP; //本地ip
	private Integer CURRENT_STATUS; //当前状态 1-启用，0-停止 
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getHOSP_NAME() {
		return HOSP_NAME;
	}
	public void setHOSP_NAME(String hOSP_NAME) {
		HOSP_NAME = hOSP_NAME;
	}
	public String getHOSP_ID() {
		return HOSP_ID;
	}
	public void setHOSP_ID(String hOSP_ID) {
		HOSP_ID = hOSP_ID;
	}
	public String getDLL_ADDRESS() {
		return DLL_ADDRESS;
	}
	public void setDLL_ADDRESS(String dLL_ADDRESS) {
		DLL_ADDRESS = dLL_ADDRESS;
	}
	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	public String getLOCAL_IP() {
		return LOCAL_IP;
	}
	public void setLOCAL_IP(String lOCAL_IP) {
		LOCAL_IP = lOCAL_IP;
	}
	public Integer getCURRENT_STATUS() {
		return CURRENT_STATUS;
	}
	public void setCURRENT_STATUS(Integer cURRENT_STATUS) {
		CURRENT_STATUS = cURRENT_STATUS;
	}
	
	
	
	
}
