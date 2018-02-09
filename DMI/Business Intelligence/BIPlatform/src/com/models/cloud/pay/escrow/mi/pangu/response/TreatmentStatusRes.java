package com.models.cloud.pay.escrow.mi.pangu.response;
/**
 * 医疗待遇封锁信息结果
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午11:55:40
 */
public class TreatmentStatusRes extends BaseResponse {
	private String shebkzt;

	public String getShebkzt() {
		return shebkzt;
	}
	public void setShebkzt(String shebkzt) {
		this.shebkzt = shebkzt;
	}
}
