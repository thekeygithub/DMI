package com.models.cloud.pay.escrow.mi.pangu.response;

import java.util.List;
/**
 * 慢性病结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 下午7:35:45
 */
public class MxbRes extends BaseResponse {

	private List<Mxb> mxblist;
	
	public List<Mxb> getMxblist() {
		return mxblist;
	}

	public void setMxblist(List<Mxb> mxblist) {
		this.mxblist = mxblist;
	}

	public static class Mxb{
		private String bingzbm;
		private String bingzmc;
		public String getBingzbm() {
			return bingzbm;
		}
		public void setBingzbm(String bingzbm) {
			this.bingzbm = bingzbm;
		}
		public String getBingzmc() {
			return bingzmc;
		}
		public void setBingzmc(String bingzmc) {
			this.bingzmc = bingzmc;
		}
	}
}
