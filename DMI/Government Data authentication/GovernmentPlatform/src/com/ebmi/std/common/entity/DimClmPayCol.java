package com.ebmi.std.common.entity;

/**
 * 结算单支付金额字段配置
 * @author yanjie.ji
 * @date 2016-1-4
 * @time 下午1:44:11
 *
 */
public class DimClmPayCol {
	
	private String fee_col_name;
	private String pa_col_name;
	private String disp_name;
	private String orig_col_name;
	private int gp_code;
	private int ord_num;
	private String sum_col_name;
	private int valid_flag;
	private String remark;
	
	public String getFee_col_name() {
		return fee_col_name;
	}
	public void setFee_col_name(String fee_col_name) {
		this.fee_col_name = fee_col_name;
	}
	public String getPa_col_name() {
		return pa_col_name;
	}
	public void setPa_col_name(String pa_col_name) {
		this.pa_col_name = pa_col_name;
	}
	public String getDisp_name() {
		return disp_name;
	}
	public void setDisp_name(String disp_name) {
		this.disp_name = disp_name;
	}
	public String getOrig_col_name() {
		return orig_col_name;
	}
	public void setOrig_col_name(String orig_col_name) {
		this.orig_col_name = orig_col_name;
	}
	public int getGp_code() {
		return gp_code;
	}
	public void setGp_code(int gp_code) {
		this.gp_code = gp_code;
	}
	public int getOrd_num() {
		return ord_num;
	}
	public void setOrd_num(int ord_num) {
		this.ord_num = ord_num;
	}
	public String getSum_col_name() {
		return sum_col_name;
	}
	public void setSum_col_name(String sum_col_name) {
		this.sum_col_name = sum_col_name;
	}
	public int getValid_flag() {
		return valid_flag;
	}
	public void setValid_flag(int valid_flag) {
		this.valid_flag = valid_flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
