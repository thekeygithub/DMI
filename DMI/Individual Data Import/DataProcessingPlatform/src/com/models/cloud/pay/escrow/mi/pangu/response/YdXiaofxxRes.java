package com.models.cloud.pay.escrow.mi.pangu.response;

import java.util.List;
/**
 * 药店账单列表
 * @author yanjie.ji
 * @date 2016年12月1日 
 * @time 下午3:13:50
 */
public class YdXiaofxxRes extends BaseResponse {
	private List<Xiaofxx> xiaofList;
	
	public List<Xiaofxx> getXiaofList() {
		return xiaofList;
	}

	public void setXiaofList(List<Xiaofxx> xiaofList) {
		this.xiaofList = xiaofList;
	}

	public static class Xiaofxx{
		private String jiesrq;//结算日期
		private String renc;//人次
		private String yilfzje;//医疗费总金额
		private String zhanghzf;//帐户支付
		private String xianjzf;//现金支付
		public String getJiesrq() {
			return jiesrq;
		}
		public void setJiesrq(String jiesrq) {
			this.jiesrq = jiesrq;
		}
		public String getRenc() {
			return renc;
		}
		public void setRenc(String renc) {
			this.renc = renc;
		}
		public String getYilfzje() {
			return yilfzje;
		}
		public void setYilfzje(String yilfzje) {
			this.yilfzje = yilfzje;
		}
		public String getZhanghzf() {
			return zhanghzf;
		}
		public void setZhanghzf(String zhanghzf) {
			this.zhanghzf = zhanghzf;
		}
		public String getXianjzf() {
			return xianjzf;
		}
		public void setXianjzf(String xianjzf) {
			this.xianjzf = xianjzf;
		}
	}
}
