package com.pay.cloud.pay.escrow.mi.pangu.response;

import java.util.List;

public class MzXiaofxxRes extends BaseResponse{

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
		private String tongczfje;//统筹支付金额
		private String dabjzjjzf;//大病救助基金支付
		private String gongwybzzf;//公务员补助支付
		private String qiybcjjzf;//企业补充基金支付
		private String shizbz;//师职补助
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
		public String getTongczfje() {
			return tongczfje;
		}
		public void setTongczfje(String tongczfje) {
			this.tongczfje = tongczfje;
		}
		public String getDabjzjjzf() {
			return dabjzjjzf;
		}
		public void setDabjzjjzf(String dabjzjjzf) {
			this.dabjzjjzf = dabjzjjzf;
		}
		public String getGongwybzzf() {
			return gongwybzzf;
		}
		public void setGongwybzzf(String gongwybzzf) {
			this.gongwybzzf = gongwybzzf;
		}
		public String getQiybcjjzf() {
			return qiybcjjzf;
		}
		public void setQiybcjjzf(String qiybcjjzf) {
			this.qiybcjjzf = qiybcjjzf;
		}
		public String getShizbz() {
			return shizbz;
		}
		public void setShizbz(String shizbz) {
			this.shizbz = shizbz;
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
