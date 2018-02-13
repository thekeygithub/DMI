package com.models.cloud.pay.escrow.mi.pangu.request;
/**
 * 药店月账单查询
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午11:40:36
 */
public class YdAccoutInfoReq extends BaseReq{

	private String chaxlx;//查询类型
	private String kaissj;//开始时间
	private String jiessj;//结束时间
	
	public String getChaxlx() {
		return chaxlx;
	}
	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}
	public String getKaissj() {
		return kaissj;
	}
	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}
	public String getJiessj() {
		return jiessj;
	}
	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}
}
