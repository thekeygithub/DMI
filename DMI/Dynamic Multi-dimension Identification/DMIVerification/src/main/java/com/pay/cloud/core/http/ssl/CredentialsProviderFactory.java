/**
 * Copyright 2014-2014 the original author or authors.
 */
package com.pay.cloud.core.http.ssl;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 * 凭证工厂
 * @author xiao.wang
 * @version 1.0
 *
 */
public class CredentialsProviderFactory {
	
	private AuthScope authScope;
	
	private Credentials credentials;
	
	/**
	 * 创建凭证工厂
	 * @return
	 */
	public static CredentialsProviderFactory create() {
		return new CredentialsProviderFactory();
	}
	
	/**
	 * 创建凭证
	 * @return CredentialsProvider
	 */
	public CredentialsProvider build() {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(this.getAuthScope(), this.getCredentials());
		return credsProvider;
	}

	public AuthScope getAuthScope() {
		return authScope;
	}

	public void setAuthScope(AuthScope authScope) {
		this.authScope = authScope;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
}