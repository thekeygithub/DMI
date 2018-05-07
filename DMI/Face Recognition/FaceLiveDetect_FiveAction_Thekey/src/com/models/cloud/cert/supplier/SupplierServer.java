package com.models.cloud.cert.supplier;

import com.models.cloud.cert.exception.CodeException;
import com.models.cloud.cert.info.CertResult;
import com.models.cloud.cert.info.EbaoFourParam;
/**
 * 实名认证供应商接口
 * @author yanjie.ji
 * @date 2016年7月13日 
 * @time 下午2:06:06
 */
public interface SupplierServer {

	/**
	 * 实名认证四要素 
	 * @param param 实名认证四要素
	 * @return
	 * @throws Exception
	 */
	public CertResult doCertFour(EbaoFourParam param)throws CodeException;
}
