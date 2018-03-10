package com.models.cloud.pay.escrow.mi.pangu.response;


/**
 * 获取服务器时间结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 上午11:25:39
 */
public class SystemDateRes extends BaseResponse {

	private String sysdate;

	public String getSysdate() {
		return sysdate;
	}

	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}
	
}
