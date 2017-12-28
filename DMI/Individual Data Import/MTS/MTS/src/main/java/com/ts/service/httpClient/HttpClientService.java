package com.ts.service.httpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.ts.bean.HttpResult;

public interface HttpClientService {

	public String doGet(String url) throws ClientProtocolException, IOException;
	
	public String doGet(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException;
	
	public HttpResult doPost(String url, Map<String, String> params) throws IOException;
	
	public HttpResult doPost(String url) throws IOException;
	
	public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException;
}
