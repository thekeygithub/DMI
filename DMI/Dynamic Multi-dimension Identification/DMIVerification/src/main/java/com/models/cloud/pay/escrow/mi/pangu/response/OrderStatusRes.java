package com.models.cloud.pay.escrow.mi.pangu.response;


/**
 * 结算记录状态查询结果
 * @author yanjie.ji
 * @date 2016年12月14日 
 * @time 下午5:46:42
 */
public class OrderStatusRes extends BaseResponse {

	private String youxbz;//有效标示
	private String jingbr;//经办人

	public String getYouxbz() {
		return youxbz;
	}
	public void setYouxbz(String youxbz) {
		this.youxbz = youxbz;
	}
	public String getJingbr() {
		return jingbr;
	}
	public void setJingbr(String jingbr) {
		this.jingbr = jingbr;
	}
	
}
