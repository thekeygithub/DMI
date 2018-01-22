/**
 * Copyright 2014-2014 the original author or authors.
 */
package com.pay.cloud.core.http;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import com.pay.cloud.core.http.ssl.AnyOneTrustStrategy;

/**
 * 注册访问方式工厂
 * @author xiao.wang
 * @version 1.0
 *
 */
public class RegistryFactory {

	/**
	 * 创建连接工厂
	 * @return Registry<ConnectionSocketFactory>
	 */
	public static Registry<ConnectionSocketFactory> create() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
		registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());

		try {
			System.setProperty ("jsse.enableSNIExtension", "false");
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyOneTrustStrategy()).build();
			
			LayeredConnectionSocketFactory lcsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			registryBuilder.register("https", lcsf);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return registryBuilder.build();
	}
}