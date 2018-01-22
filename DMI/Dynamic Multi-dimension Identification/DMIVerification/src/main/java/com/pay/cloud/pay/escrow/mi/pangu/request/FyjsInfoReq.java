package com.pay.cloud.pay.escrow.mi.pangu.request;


/**
 * 医保费用结算信息下载
 * @author yanjie.ji
 * @date 2016年11月18日 
 * @time 下午5:53:52
 */
public class FyjsInfoReq extends BaseReq {
	
	private String kssj;
	private String jssj;
	
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
}
