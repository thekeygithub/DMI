package com.pay.cloud.cert.server;

import com.pay.cloud.cert.info.CertResult;
import com.pay.cloud.cert.info.EbaoFourParam;
/**
 * 实名认证接口
 * @author yanjie.ji
 * @date 2016年7月13日 
 * @time 上午10:22:17
 */
public interface CertServer {
	/**
	 * 实名认证供应商数组
	 */
	static final short[] CERT_SUPPLIER_TYPES={
		CertServer.CERT_SUPPLIER_TYPE_PH,
		CertServer.CERT_SUPPLIER_TYPE_HF
	};
	
	/**
	 * 实名认证供应商--普惠
	 */
	public static final short CERT_SUPPLIER_TYPE_PH=1;
	/**
	 * 实名认证供应商--华付
	 */
	public static final short CERT_SUPPLIER_TYPE_HF=2;
	/**
	 * 实名认证四要素 
	 * 轮询调用供应商
	 * @param param 实名认证四要素
	 * @return
	 * @throws Exception
	 */
	public CertResult doCertFour(EbaoFourParam param);
	/**
	 * 实名认证四要素
	 * @param type  供应商类别 
	 * @param param 实名认证四要素
	 * @return
	 * @throws Exception
	 */
	public CertResult doCertFour(short type,EbaoFourParam param);

}
