package com.ts.entity;

import java.io.Serializable;
/**
 * 
 * ProjectName：API
 * ClassName：P_result
 * Description：TODO(计算结果信息)
 * @Copyright：
 * @Company：
 * @author：Lee 李世博
 * @version 
 * Create Date：2017年3月28日 下午2:53:49
 */
public class P_result  implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO(说明)
	 */ 
	private static final long serialVersionUID = 1L;
	private String ID; //主键ID
	private String IN_ID;	//接口信息ID
	private Double TOTAL_FEE;	//费用总额
	private Double SELF_PAY;	//自费总额(非医保)
	private Double SELF_NEG;	//药品乙类自负
	private Double MED_FEE;     //医保费用
	private Double TRAN_FEE;	//转院自费
	private Double LEVEL_PAY;	//起付线
	private Double SELF_CASH_PAY;	//个人自费现金支付
	private Double NEG_CASH_PAY;	//个人自负现金支付
	private Double CASH_TOTAL;	//合计现金支付
	private Double RETURN_FEE;	//合计报销金额
	private Double I_PAY;		//历年帐户支付
	private Double O_PAY;	//当年帐户支付
	private Double WHOLE_PAY;	//职保统筹基金支付
	private Double ADD_PAY;	//补充保险支付
	private Double SER_PAY;	//公务员补助支付
	private Double COM_PAY;	//单位补助支付
	private Double SALV_PAY;	//医疗救助支付
	private Double RETIRE_PAY;	//离休基金支付
	private Double FUND_PAY;	//二乙基金支付
	private Double MODEL_PAY;	//劳模医疗补助支付
	private Double CIVIL_PAY;	//民政救助支付
	private Double PRIV_PAY;	//优抚救助支付
	private Double DPF_PAY; 	//残联基金支付
	private Double PLAN_PAY;	//计生基金支付
	private Double HURT_PAY;	//工伤基金支付
	private Double PROC_PAY;	//生育基金支付
	private Double I_BALANCE;	//结算后当年个帐余额
	private Double O_BALANCE;	//结算后历年个帐余额
	private Double INSU_PAY;	//合保统筹基金支付
	private Double INSU_SALV_PAY;	//合保大病救助支付
	private Double CHARITY;	//慈善基金
	private Double MUTUAL;	//共济账户支出
	
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
	public Double getTRAN_FEE() {
		return TRAN_FEE;
	}
	public void setTRAN_FEE(Double tRAN_FEE) {
		TRAN_FEE = tRAN_FEE;
	}
	public Double getLEVEL_PAY() {
		return LEVEL_PAY;
	}
	public void setLEVEL_PAY(Double lEVEL_PAY) {
		LEVEL_PAY = lEVEL_PAY;
	}
	public Double getSELF_CASH_PAY() {
		return SELF_CASH_PAY;
	}
	public void setSELF_CASH_PAY(Double sELF_CASH_PAY) {
		SELF_CASH_PAY = sELF_CASH_PAY;
	}
	public Double getNEG_CASH_PAY() {
		return NEG_CASH_PAY;
	}
	public void setNEG_CASH_PAY(Double nEG_CASH_PAY) {
		NEG_CASH_PAY = nEG_CASH_PAY;
	}
	public Double getCASH_TOTAL() {
		return CASH_TOTAL;
	}
	public void setCASH_TOTAL(Double cASH_TOTAL) {
		CASH_TOTAL = cASH_TOTAL;
	}
	public Double getRETURN_FEE() {
		return RETURN_FEE;
	}
	public void setRETURN_FEE(Double rETURN_FEE) {
		RETURN_FEE = rETURN_FEE;
	}
	public Double getI_PAY() {
		return I_PAY;
	}
	public void setI_PAY(Double i_PAY) {
		I_PAY = i_PAY;
	}
	public Double getO_PAY() {
		return O_PAY;
	}
	public void setO_PAY(Double o_PAY) {
		O_PAY = o_PAY;
	}
	public Double getWHOLE_PAY() {
		return WHOLE_PAY;
	}
	public void setWHOLE_PAY(Double wHOLE_PAY) {
		WHOLE_PAY = wHOLE_PAY;
	}
	public Double getADD_PAY() {
		return ADD_PAY;
	}
	public void setADD_PAY(Double aDD_PAY) {
		ADD_PAY = aDD_PAY;
	}
	public Double getSER_PAY() {
		return SER_PAY;
	}
	public void setSER_PAY(Double sER_PAY) {
		SER_PAY = sER_PAY;
	}
	public Double getCOM_PAY() {
		return COM_PAY;
	}
	public void setCOM_PAY(Double cOM_PAY) {
		COM_PAY = cOM_PAY;
	}
	public Double getSALV_PAY() {
		return SALV_PAY;
	}
	public void setSALV_PAY(Double sALV_PAY) {
		SALV_PAY = sALV_PAY;
	}
	public Double getRETIRE_PAY() {
		return RETIRE_PAY;
	}
	public void setRETIRE_PAY(Double rETIRE_PAY) {
		RETIRE_PAY = rETIRE_PAY;
	}
	public Double getFUND_PAY() {
		return FUND_PAY;
	}
	public void setFUND_PAY(Double fUND_PAY) {
		FUND_PAY = fUND_PAY;
	}
	public Double getMODEL_PAY() {
		return MODEL_PAY;
	}
	public void setMODEL_PAY(Double mODEL_PAY) {
		MODEL_PAY = mODEL_PAY;
	}
	public Double getCIVIL_PAY() {
		return CIVIL_PAY;
	}
	public void setCIVIL_PAY(Double cIVIL_PAY) {
		CIVIL_PAY = cIVIL_PAY;
	}
	public Double getPRIV_PAY() {
		return PRIV_PAY;
	}
	public void setPRIV_PAY(Double pRIV_PAY) {
		PRIV_PAY = pRIV_PAY;
	}
	public Double getDPF_PAY() {
		return DPF_PAY;
	}
	public void setDPF_PAY(Double dPF_PAY) {
		DPF_PAY = dPF_PAY;
	}
	public Double getPLAN_PAY() {
		return PLAN_PAY;
	}
	public void setPLAN_PAY(Double pLAN_PAY) {
		PLAN_PAY = pLAN_PAY;
	}
	public Double getHURT_PAY() {
		return HURT_PAY;
	}
	public void setHURT_PAY(Double hURT_PAY) {
		HURT_PAY = hURT_PAY;
	}
	public Double getPROC_PAY() {
		return PROC_PAY;
	}
	public void setPROC_PAY(Double pROC_PAY) {
		PROC_PAY = pROC_PAY;
	}
	public Double getI_BALANCE() {
		return I_BALANCE;
	}
	public void setI_BALANCE(Double i_BALANCE) {
		I_BALANCE = i_BALANCE;
	}
	public Double getO_BALANCE() {
		return O_BALANCE;
	}
	public void setO_BALANCE(Double o_BALANCE) {
		O_BALANCE = o_BALANCE;
	}
	public Double getINSU_PAY() {
		return INSU_PAY;
	}
	public void setINSU_PAY(Double iNSU_PAY) {
		INSU_PAY = iNSU_PAY;
	}
	public Double getINSU_SALV_PAY() {
		return INSU_SALV_PAY;
	}
	public void setINSU_SALV_PAY(Double iNSU_SALV_PAY) {
		INSU_SALV_PAY = iNSU_SALV_PAY;
	}
	public Double getCHARITY() {
		return CHARITY;
	}
	public void setCHARITY(Double cHARITY) {
		CHARITY = cHARITY;
	}
	public Double getMUTUAL() {
		return MUTUAL;
	}
	public void setMUTUAL(Double mUTUAL) {
		MUTUAL = mUTUAL;
	}
	
}