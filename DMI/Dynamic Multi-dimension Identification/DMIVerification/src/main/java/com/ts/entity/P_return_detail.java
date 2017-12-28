package com.ts.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ProjectName：API
 * ClassName：P_return_detail
 * Description：TODO(退费总额、退费明细对账实体类)
 * @Copyright：
 * @Company：
 * @author：Lee 刘峰
 * @version 
 * Create Date：2017年4月11日 上午10:36:03
 */
public class P_return_detail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID
	private Integer DATA_TYPE;//数据类型
	private String API_TYPE;//业务接口编号
	private String REQ_NO;//请求流水号
	private String TIME_STAMP;//时间戳
	private String GROUP_ID;//机构ID
	private String GROUP_NAME;//机构name
	private String HOS_CODE;//医院编码
	private String HOS_NAME;//医院名称
	private Date FINAL_DATE;//结算时间
	private Integer RETURN_NUM;//退费记录数
	private Double TOTAL_FEE;//退费费用总额
	private Double SELF_PAY;//退费自费总额(非医保)
	private Double SELF_NEG;//退费药品乙类自负
	private Double MED_FEE;//退费医保费用
	private Double RETURN_FEE;//退费合计报销金额
	private Integer CHECK_TYPE;//对账类型
	private String VISIT_NO;//医院交易流水号
	private String FINAL_NO;//结算流水号
	private String SUPER;//结算流水号
	private Date DATA_DATE;//接口数据时间
	private Double CASH_TOTAL;//合计现金支付
	
	public P_return_detail() {
		super();
	}
	public P_return_detail(P_return_detail returnDetailParam) {
		ID = returnDetailParam.getID();
		DATA_TYPE = returnDetailParam.getDATA_TYPE();
		API_TYPE = returnDetailParam.getAPI_TYPE();
		REQ_NO = returnDetailParam.getREQ_NO();
		TIME_STAMP = returnDetailParam.getTIME_STAMP();
		GROUP_ID = returnDetailParam.getGROUP_ID();
		GROUP_NAME = returnDetailParam.getGROUP_NAME();
		HOS_CODE = returnDetailParam.getHOS_CODE();
		HOS_NAME = returnDetailParam.getHOS_NAME();
		FINAL_DATE = returnDetailParam.getFINAL_DATE();
		RETURN_NUM = returnDetailParam.getRETURN_NUM();
		TOTAL_FEE = returnDetailParam.getTOTAL_FEE();
		SELF_PAY = returnDetailParam.getSELF_PAY();
		SELF_NEG = returnDetailParam.getSELF_NEG();
		MED_FEE = returnDetailParam.getMED_FEE();
		RETURN_FEE = returnDetailParam.getRETURN_FEE();
		CHECK_TYPE = returnDetailParam.getCHECK_TYPE();
		VISIT_NO = returnDetailParam.getVISIT_NO();
		FINAL_NO = returnDetailParam.getFINAL_NO();
		SUPER = returnDetailParam.getSUPER();
		DATA_DATE = returnDetailParam.getDATA_DATE();
		CASH_TOTAL = returnDetailParam.getCASH_TOTAL();
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Integer getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(Integer dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
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
	public String getGROUP_ID() {
		return GROUP_ID;
	}
	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}
	public String getHOS_CODE() {
		return HOS_CODE;
	}
	public void setHOS_CODE(String hOS_CODE) {
		HOS_CODE = hOS_CODE;
	}
	public Date getFINAL_DATE() {
		return FINAL_DATE;
	}
	public void setFINAL_DATE(Date fINAL_DATE) {
		FINAL_DATE = fINAL_DATE;
	}
	public Integer getRETURN_NUM() {
		return RETURN_NUM;
	}
	public void setRETURN_NUM(Integer rETURN_NUM) {
		RETURN_NUM = rETURN_NUM;
	}
	public Double getTOTAL_FEE() {
		return TOTAL_FEE;
	}
	public void setTOTAL_FEE(Double tOTAL_FEE) {
		TOTAL_FEE = tOTAL_FEE;
	}
	public Double getSELF_PAY() {
		return SELF_PAY;
	}
	public void setSELF_PAY(Double sELF_PAY) {
		SELF_PAY = sELF_PAY;
	}
	public Double getSELF_NEG() {
		return SELF_NEG;
	}
	public void setSELF_NEG(Double sELF_NEG) {
		SELF_NEG = sELF_NEG;
	}
	public Double getMED_FEE() {
		return MED_FEE;
	}
	public void setMED_FEE(Double mED_FEE) {
		MED_FEE = mED_FEE;
	}
	public Double getRETURN_FEE() {
		return RETURN_FEE;
	}
	public void setRETURN_FEE(Double rETURN_FEE) {
		RETURN_FEE = rETURN_FEE;
	}
	public Integer getCHECK_TYPE() {
		return CHECK_TYPE;
	}
	public void setCHECK_TYPE(Integer cHECK_TYPE) {
		CHECK_TYPE = cHECK_TYPE;
	}
	public String getVISIT_NO() {
		return VISIT_NO;
	}
	public void setVISIT_NO(String vISIT_NO) {
		VISIT_NO = vISIT_NO;
	}
	public String getFINAL_NO() {
		return FINAL_NO;
	}
	public void setFINAL_NO(String fINAL_NO) {
		FINAL_NO = fINAL_NO;
	}
	public String getGROUP_NAME() {
		return GROUP_NAME;
	}
	public void setGROUP_NAME(String gROUP_NAME) {
		GROUP_NAME = gROUP_NAME;
	}
	public String getSUPER() {
		return SUPER;
	}
	public void setSUPER(String sUPER) {
		SUPER = sUPER;
	}
	public String getHOS_NAME() {
		return HOS_NAME;
	}
	public void setHOS_NAME(String hOS_NAME) {
		HOS_NAME = hOS_NAME;
	}
	public Date getDATA_DATE() {
		return DATA_DATE;
	}
	public void setDATA_DATE(Date dATA_DATE) {
		DATA_DATE = dATA_DATE;
	}
	public Double getCASH_TOTAL() {
		return CASH_TOTAL;
	}
	public void setCASH_TOTAL(Double cASH_TOTAL) {
		CASH_TOTAL = cASH_TOTAL;
	}
	
	
}