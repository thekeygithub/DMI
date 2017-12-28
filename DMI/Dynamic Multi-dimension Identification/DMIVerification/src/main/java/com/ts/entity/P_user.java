package com.ts.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * ProjectName：API
 * ClassName：P_user
 * Description：TODO(用户信息实体类)
 * @Copyright：
 * @Company：
 * @author：Lee 李世博
 * @version 
 * Create Date：2017年3月28日 下午1:36:03
 */
public class P_user implements Serializable{
	
	/**
	 * @Fields serialVersionUID : TODO(说明)
	 */ 
	private static final long serialVersionUID = 1L;
	private String ID;  //主键ID
	private String API_TYPE; //固定编号V0
	private String REQ_NO; //请求流水号
	private String TIME_STAMP;//时间戳
	private String ID_CARD;	//身份证
	private String CARD_NO;	//市民卡号
	private String IC_CARD;	//识别码
	private String NAME;	//用户姓名
	private Integer CARD_STAT;	//1新发卡;2补卡;3换卡;4挂失;5解挂;6注销;9银行卡激活状态;
	private Date CREATE_DATE; //创建日期
	
	
	private P_insu_info pInsuInfo = new P_insu_info();
	
	public P_insu_info getPInsuInfo() {
		return pInsuInfo;
	}
	public void setPInsuInfo(P_insu_info pInsuInfo) {
		this.pInsuInfo = pInsuInfo;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAPI_TYPE() {
		return API_TYPE;
	}
	public void setAPI_TYPE(String aPI_TYPE) {
		API_TYPE = aPI_TYPE;
	}
	public String getREQ_NO() {
		return REQ_NO;
	}
	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}
	public String getTIME_STAMP() {
		return TIME_STAMP;
	}
	public void setTIME_STAMP(String tIME_STAMP) {
		TIME_STAMP = tIME_STAMP;
	}
	public String getID_CARD() {
		return ID_CARD;
	}
	public void setID_CARD(String iD_CARD) {
		ID_CARD = iD_CARD;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}
	public String getIC_CARD() {
		return IC_CARD;
	}
	public void setIC_CARD(String iC_CARD) {
		IC_CARD = iC_CARD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public Integer getCARD_STAT() {
		return CARD_STAT;
	}
	public void setCARD_STAT(Integer cARD_STAT) {
		CARD_STAT = cARD_STAT;
	}
	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	
}