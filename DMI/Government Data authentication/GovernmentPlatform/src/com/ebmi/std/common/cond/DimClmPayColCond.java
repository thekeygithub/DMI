package com.ebmi.std.common.cond;

/**
 * 结算单支付金额字段配置表查询条件类
 * @author yanjie.ji
 * @date 2016-1-4
 * @time 下午1:47:11
 *
 */
public class DimClmPayColCond {
	private String fee_col_name;
	private String gp_code;
	private String valid_flag;
	
	public String getFee_col_name() {
		return fee_col_name;
	}
	public void setFee_col_name(String fee_col_name) {
		this.fee_col_name = fee_col_name;
	}
	public String getGp_code() {
		return gp_code;
	}
	public void setGp_code(String gp_code) {
		this.gp_code = gp_code;
	}
	public String getValid_flag() {
		return valid_flag;
	}
	public void setValid_flag(String valid_flag) {
		this.valid_flag = valid_flag;
	}
}
