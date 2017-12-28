package com.ts.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* @ClassName: P_inte_info 
* @Description: TODO(接口数据信息) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_inte_info implements Serializable {

	private static final long serialVersionUID = 1L;                                                            
	private String ID;//主键ID																					
	private String USER_ID;//用户信息ID																		
	private String API_TYPE;//业务接口编号																		
	private Integer DATA_TYPE;//1门诊预结算，2门诊结算，3门诊退费退号，4住院预结算，5出院结算，6住院明细查询，7交易结果查询，8参保人结果及享受状态查询，9确认交易，10参保人信息接口
	private Integer DATA_STAT;//状态定义：参见字典															
	private String REQ_NO;//请求流水号																		
	private String TIME_STAMP;//时间戳																		
	private String HOS_CODE;//医院编码
	private Integer PAY_TYPE;//现金支付方式	
	private String BUSI_TYPE;//参加字典																		
	private String VISIT_NO;//就诊序列号																	
	private String DIS_CODE;//疾病编号																		
	private String REG_NO;//登记编号																		
	private String DIS_NAME;//疾病名称																		
	private String DIS_DESC;//疾病描述																		
	private Integer BILL_NUM;//本次结算单据张数																
	private Integer DEAL_STAT;//0成功，小于0失败															
	private String ERROR;//错误信息																			
	private String CARD_RES;//0表示不写或写卡成功，其他表示写卡错误信息									
	private String BANK_RES;//0表示不扣或扣成功															
	private String IC_DATA;//更新后IC卡数据																	
	private Integer OVER_FLAG;//提示标记(1:有提示信息 0或者其他：无提示信息)								
	private Integer SPEC_FLAG;//0非特种病结算，1特种病结算													
	private String FINAL_DATE;//结算时间																		
	private String FINAL_NO;//结算流水号																	
	private String ABO_DEAL_NO;//要作废的结算交易号															
	private String OPERATOR;//经办人																		
	private Integer IS_REPET;//是否重复退费																	
	private String RETURN_NO;//退费交易流水号																
	private String RETURN_DATE;//退费结算日期																	
	private String REG_DEAL_NO;//住院登记交易号																
	private Integer FINAL_NUM;//本次结算明细条数															
	private String IN_DEAL_NO;//住院结算交易交流水号														
	private String IN_REG_NO;//住院登记流水号																
	private String DETAIL_LIST;//明细序号列表																
	private String FAIL_DETAIL_LIST;//未上传成功的明细序号列表												
	private String SEARCH_TYPE_NO;//待查询用户交易类型号													
	private String SEARCH_DEAL_NO;//待查询的用户交易流水号													
	private Integer IS_OK;//用户交易是否成功																
	private String DEAL_DATE;//交易时间																		
	private String DEAL_FINAL_NO;//交易结算流水号															
	private String DEAL_STEP;//交易处于阶段																	
	private String DEAL_TYPE;//交易类型																		
	private String MED_DEAL_NO;//医保交易流水号																
	private String DEAL_NO;//交易流水号																		
	private String HOS_RES;//掌医事务结果																	
	private String ADD_INFO;//附加消息
	private String BANK_NO;//银行卡信息
	private String APPR_NO;//银行卡信息
	private Integer HAVE_STAT;//享受状态
	private String GROUP_ID;//机构Code
	
	private P_user pUser = new P_user();//用户信息
	private List<P_result> pResult = new ArrayList<P_result>() ;//计算结果
	private List<P_bill>  pBill = new ArrayList<P_bill>() ;//结算单据
	private List<P_bill_item>  pBillItem = new ArrayList<P_bill_item>() ;//未匹配上的收费项目单据 
	private List<P_over_detail> pOverDetail = new ArrayList<P_over_detail>() ;//超限明细表
	private List<P_fee> pFee = new ArrayList<P_fee>() ;//费用汇总信息
	private List<P_fund> pFund = new ArrayList<P_fund>() ;//基金分段信息
	
	/**
	 * 添加计算结果信息 
	 * @Title      : addPReuslt 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addPReuslt(P_result pr)
	{
		this.pResult.add(pr);
	}
	/**
	 * 添加结算单据
	 * @Title      : addpBill 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addpBill(P_bill pr)
	{
		this.pBill.add(pr);
	}
	/**
	 * 添加未匹配上的收费项目单据
	 * @Title      : addpBill 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addpBillItem(P_bill_item pr)
	{
		this.pBillItem.add(pr);
	}
	/**
	 * 添加超限明细表
	 * @Title      : addpOverDetail 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addpOverDetail(P_over_detail pr)
	{
		this.pOverDetail.add(pr);
	}
	/**
	 * 添加费用汇总信息
	 * @Title      : addpFee 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addpFee(P_fee pr)
	{
		this.pFee.add(pr);
	}
	/**
	 * 添加基金分段信息
	 * @Title      : addpFund 
	 * @Description: TODO
	 * @param pr     : void
	 * @author     :lee
	 * Create Date : 2017年3月29日 下午3:59:22
	 * @throws
	 */
	public void  addpFund(P_fund pr)
	{
		this.pFund.add(pr);
	}
	
	public P_user getPUser() {
		return pUser;
	}
	public void setPUser(P_user pUser) {
		this.pUser = pUser;
	}
	public List<P_result> getPResult() {
		return pResult;
	}
	public List<P_bill> getPBill() {
		return pBill;
	}
	public List<P_bill_item> getPBillItem() {
		return pBillItem;
	}
	public List<P_over_detail> getPOverDetail() {
		return pOverDetail;
	}
	public List<P_fee> getPFee() {
		return pFee;
	}
	public List<P_fund> getPFund() {
		return pFund;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getAPI_TYPE() {
		return API_TYPE;
	}
	public void setAPI_TYPE(String aPI_TYPE) {
		API_TYPE = aPI_TYPE;
	}
	public Integer getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(Integer dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public Integer getDATA_STAT() {
		return DATA_STAT;
	}
	public void setDATA_STAT(Integer dATA_STAT) {
		DATA_STAT = dATA_STAT;
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
	public String getHOS_CODE() {
		return HOS_CODE;
	}
	public void setHOS_CODE(String hOS_CODE) {
		HOS_CODE = hOS_CODE;
	}
	public String getBUSI_TYPE() {
		return BUSI_TYPE;
	}
	public void setBUSI_TYPE(String bUSI_TYPE) {
		BUSI_TYPE = bUSI_TYPE;
	}
	public String getVISIT_NO() {
		return VISIT_NO;
	}
	public void setVISIT_NO(String vISIT_NO) {
		VISIT_NO = vISIT_NO;
	}
	public String getDIS_CODE() {
		return DIS_CODE;
	}
	public void setDIS_CODE(String dIS_CODE) {
		DIS_CODE = dIS_CODE;
	}
	public String getREG_NO() {
		return REG_NO;
	}
	public void setREG_NO(String rEG_NO) {
		REG_NO = rEG_NO;
	}
	public String getDIS_NAME() {
		return DIS_NAME;
	}
	public void setDIS_NAME(String dIS_NAME) {
		DIS_NAME = dIS_NAME;
	}
	public String getDIS_DESC() {
		return DIS_DESC;
	}
	public void setDIS_DESC(String dIS_DESC) {
		DIS_DESC = dIS_DESC;
	}
	public Integer getBILL_NUM() {
		return BILL_NUM;
	}
	public void setBILL_NUM(Integer bILL_NUM) {
		BILL_NUM = bILL_NUM;
	}
	public Integer getDEAL_STAT() {
		return DEAL_STAT;
	}
	public void setDEAL_STAT(Integer dEAL_STAT) {
		DEAL_STAT = dEAL_STAT;
	}
	public String getERROR() {
		return ERROR;
	}
	public void setERROR(String eRROR) {
		ERROR = eRROR;
	}
	public String getCARD_RES() {
		return CARD_RES;
	}
	public void setCARD_RES(String cARD_RES) {
		CARD_RES = cARD_RES;
	}
	public String getBANK_RES() {
		return BANK_RES;
	}
	public void setBANK_RES(String bANK_RES) {
		BANK_RES = bANK_RES;
	}
	public String getIC_DATA() {
		return IC_DATA;
	}
	public void setIC_DATA(String iC_DATA) {
		IC_DATA = iC_DATA;
	}
	public Integer getOVER_FLAG() {
		return OVER_FLAG;
	}
	public void setOVER_FLAG(Integer oVER_FLAG) {
		OVER_FLAG = oVER_FLAG;
	}
	public Integer getSPEC_FLAG() {
		return SPEC_FLAG;
	}
	public void setSPEC_FLAG(Integer sPEC_FLAG) {
		SPEC_FLAG = sPEC_FLAG;
	}
	public String getFINAL_DATE() {
		return FINAL_DATE;
	}
	public void setFINAL_DATE(String fINAL_DATE) {
		FINAL_DATE = fINAL_DATE;
	}
	public String getFINAL_NO() {
		return FINAL_NO;
	}
	public void setFINAL_NO(String fINAL_NO) {
		FINAL_NO = fINAL_NO;
	}
	public String getABO_DEAL_NO() {
		return ABO_DEAL_NO;
	}
	public void setABO_DEAL_NO(String aBO_DEAL_NO) {
		ABO_DEAL_NO = aBO_DEAL_NO;
	}
	public String getOPERATOR() {
		return OPERATOR;
	}
	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}
	public Integer getIS_REPET() {
		return IS_REPET;
	}
	public void setIS_REPET(Integer iS_REPET) {
		IS_REPET = iS_REPET;
	}
	public String getRETURN_NO() {
		return RETURN_NO;
	}
	public void setRETURN_NO(String rETURN_NO) {
		RETURN_NO = rETURN_NO;
	}
	public String getRETURN_DATE() {
		return RETURN_DATE;
	}
	public void setRETURN_DATE(String rETURN_DATE) {
		RETURN_DATE = rETURN_DATE;
	}
	public String getREG_DEAL_NO() {
		return REG_DEAL_NO;
	}
	public void setREG_DEAL_NO(String rEG_DEAL_NO) {
		REG_DEAL_NO = rEG_DEAL_NO;
	}
	public Integer getFINAL_NUM() {
		return FINAL_NUM;
	}
	public void setFINAL_NUM(Integer fINAL_NUM) {
		FINAL_NUM = fINAL_NUM;
	}
	public String getIN_DEAL_NO() {
		return IN_DEAL_NO;
	}
	public void setIN_DEAL_NO(String iN_DEAL_NO) {
		IN_DEAL_NO = iN_DEAL_NO;
	}
	public String getIN_REG_NO() {
		return IN_REG_NO;
	}
	public void setIN_REG_NO(String iN_REG_NO) {
		IN_REG_NO = iN_REG_NO;
	}
	public String getDETAIL_LIST() {
		return DETAIL_LIST;
	}
	public void setDETAIL_LIST(String dETAIL_LIST) {
		DETAIL_LIST = dETAIL_LIST;
	}
	public String getFAIL_DETAIL_LIST() {
		return FAIL_DETAIL_LIST;
	}
	public void setFAIL_DETAIL_LIST(String fAIL_DETAIL_LIST) {
		FAIL_DETAIL_LIST = fAIL_DETAIL_LIST;
	}
	public String getSEARCH_TYPE_NO() {
		return SEARCH_TYPE_NO;
	}
	public void setSEARCH_TYPE_NO(String sEARCH_TYPE_NO) {
		SEARCH_TYPE_NO = sEARCH_TYPE_NO;
	}
	public String getSEARCH_DEAL_NO() {
		return SEARCH_DEAL_NO;
	}
	public void setSEARCH_DEAL_NO(String sEARCH_DEAL_NO) {
		SEARCH_DEAL_NO = sEARCH_DEAL_NO;
	}
	public Integer getIS_OK() {
		return IS_OK;
	}
	public void setIS_OK(Integer iS_OK) {
		IS_OK = iS_OK;
	}
	public String getDEAL_DATE() {
		return DEAL_DATE;
	}
	public void setDEAL_DATE(String dEAL_DATE) {
		DEAL_DATE = dEAL_DATE;
	}
	public String getDEAL_FINAL_NO() {
		return DEAL_FINAL_NO;
	}
	public void setDEAL_FINAL_NO(String dEAL_FINAL_NO) {
		DEAL_FINAL_NO = dEAL_FINAL_NO;
	}
	public String getDEAL_STEP() {
		return DEAL_STEP;
	}
	public void setDEAL_STEP(String dEAL_STEP) {
		DEAL_STEP = dEAL_STEP;
	}
	public String getDEAL_TYPE() {
		return DEAL_TYPE;
	}
	public void setDEAL_TYPE(String dEAL_TYPE) {
		DEAL_TYPE = dEAL_TYPE;
	}
	public String getMED_DEAL_NO() {
		return MED_DEAL_NO;
	}
	public void setMED_DEAL_NO(String mED_DEAL_NO) {
		MED_DEAL_NO = mED_DEAL_NO;
	}
	public String getDEAL_NO() {
		return DEAL_NO;
	}
	public void setDEAL_NO(String dEAL_NO) {
		DEAL_NO = dEAL_NO;
	}
	public String getHOS_RES() {
		return HOS_RES;
	}
	public void setHOS_RES(String hOS_RES) {
		HOS_RES = hOS_RES;
	}
	public String getADD_INFO() {
		return ADD_INFO;
	}
	public void setADD_INFO(String aDD_INFO) {
		ADD_INFO = aDD_INFO;
	}
	public Integer getPAY_TYPE() {
		return PAY_TYPE;
	}
	public void setPAY_TYPE(Integer pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}
	public Integer getHAVE_STAT() {
		return HAVE_STAT;
	}
	public void setHAVE_STAT(Integer hAVE_STAT) {
		HAVE_STAT = hAVE_STAT;
	}
	public String getBANK_NO() {
		return BANK_NO;
	}
	public void setBANK_NO(String bANK_NO) {
		BANK_NO = bANK_NO;
	}
	public String getAPPR_NO() {
		return APPR_NO;
	}
	public void setAPPR_NO(String aPPR_NO) {
		APPR_NO = aPPR_NO;
	}
	public String getGROUP_ID() {
		return GROUP_ID;
	}
	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}
	
}