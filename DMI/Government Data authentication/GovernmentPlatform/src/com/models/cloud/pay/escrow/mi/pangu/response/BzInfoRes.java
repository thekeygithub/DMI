package com.models.cloud.pay.escrow.mi.pangu.response;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 病种信息结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 下午7:38:39
 */
public class BzInfoRes extends BaseResponse {

	private List<Bzinfo> bzlist;
	
	public List<Bzinfo> getBzlist() {
		return bzlist;
	}

	public void setBzlist(List<Bzinfo> bzlist) {
		this.bzlist = bzlist;
	}

	@XStreamAlias("bzinfo")
	public static class Bzinfo{
		private String jibbm;
		private String jibmc;
		private String jibfl;
		private String bingzlb;
		private String pinyzjm;
		private String wubzjm;
		private String bingzbxbz;
		private String beiz;
		public String getJibbm() {
			return jibbm;
		}
		public void setJibbm(String jibbm) {
			this.jibbm = jibbm;
		}
		public String getJibmc() {
			return jibmc;
		}
		public void setJibmc(String jibmc) {
			this.jibmc = jibmc;
		}
		public String getJibfl() {
			return jibfl;
		}
		public void setJibfl(String jibfl) {
			this.jibfl = jibfl;
		}
		public String getBingzlb() {
			return bingzlb;
		}
		public void setBingzlb(String bingzlb) {
			this.bingzlb = bingzlb;
		}
		public String getBingzbxbz() {
			return bingzbxbz;
		}
		public void setBingzbxbz(String bingzbxbz) {
			this.bingzbxbz = bingzbxbz;
		}
		public String getBeiz() {
			return beiz;
		}
		public void setBeiz(String beiz) {
			this.beiz = beiz;
		}
		public String getPinyzjm() {
			return pinyzjm;
		}
		public void setPinyzjm(String pinyzjm) {
			this.pinyzjm = pinyzjm;
		}
		public String getWubzjm() {
			return wubzjm;
		}
		public void setWubzjm(String wubzjm) {
			this.wubzjm = wubzjm;
		}
	}
}
