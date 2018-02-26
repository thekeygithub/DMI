/**
 * Copyright 2014-2014 the original author or authors.
 */
package com.models.cloud.core.http.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.TrustStrategy;

/**
 * 信任任何SSL请求，这里需要加强，在下一阶段开启证书明细验证。
 * @author xiao.wang
 * @version 1.0
 */
public class AnyOneTrustStrategy implements TrustStrategy {

	/**
	 * 信任验证，有需要可在这个方法里面增加具体验证
	 */
	@Override
	public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		return true;
	}

}
