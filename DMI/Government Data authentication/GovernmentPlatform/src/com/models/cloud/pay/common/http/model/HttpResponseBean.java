package com.models.cloud.pay.common.http.model;

import java.io.InputStream;


/**
 * Http返回结果Bean
 *
 */
public class HttpResponseBean {
	
	//状态编码
	private int statusCode;
	//内容
	private String entityContent;
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getEntityContent() {
		return entityContent;
	}

	public void setEntityContent(String entityContent) {
		this.entityContent = entityContent;
	}

}
