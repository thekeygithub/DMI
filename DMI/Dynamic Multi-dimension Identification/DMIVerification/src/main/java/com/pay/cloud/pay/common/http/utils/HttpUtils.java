package com.pay.cloud.pay.common.http.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.pay.cloud.pay.common.http.model.HttpResponseBean;

/**
 * Http访问工具类
 */
public class HttpUtils {
	
	//日志
	private static Logger log = Logger.getLogger(HttpUtils.class);
	
	/** 
     * 定义编码格式 UTF-8 
     */  
    public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8"; 
    
    /**
     * 定义编码格式 GBK 
     */
    public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";  
	
    
    
    public static String httpPostWithJSON(String url, String json,String charset) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault(); 
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-type","application/json; charset="+charset);  
        httpPost.setHeader("Accept", "application/json");  
        httpPost.setEntity(new StringEntity(json, Charset.forName(charset)));  
        HttpResponse res =  httpClient.execute(httpPost);
        return EntityUtils.toString(res.getEntity(), charset);
    }
	
	/**
	 * HttpPost调用
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static HttpResponseBean httpPost(String requestURL,Map<String,String> params,String charset) throws Exception{
		//校验输入参数
		if (StringUtils.isBlank(requestURL)){
			log.error("the request url is blank.");
			throw new Exception("the request url is blank.");
		}
		//字符集为空时，默认为UTF-8
		if (StringUtils.isBlank(charset)){
			charset = URL_PARAM_DECODECHARSET_UTF8;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpPost httpPost = new HttpPost(requestURL);
		HttpResponseBean bean = new HttpResponseBean();
		try {		
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			//循环遍历加载参数
			for (Entry<String, String> entry : params.entrySet()) {  
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
			}  
	        httpPost.setEntity(new UrlEncodedFormEntity(formparams, charset));
	        HttpResponse httpResponse = httpclient.execute(httpPost);//执行
	        //服务器返回状态
	        int statusCode =  httpResponse.getStatusLine().getStatusCode();  
	        //返回服务器响应的HTML代码                                 
	        String entityContent = "";
	        InputStream entityStream = null;
            if (httpResponse.getEntity() != null) {
            	entityContent = EntityUtils.toString(httpResponse.getEntity(), charset);  
            	entityStream = httpResponse.getEntity().getContent();
            }else{
            	log.info("ResponseEntity is null.");
            }
            //组织返回结果
            bean.setStatusCode(statusCode);
            bean.setEntityContent(entityContent);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e.toString());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	/**
	 * HttpGet调用
	 * @param requestURL
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static HttpResponseBean httpGet(String requestURL,String charset) throws Exception{
		//校验输入参数
		if (StringUtils.isBlank(requestURL)){
			log.error("the request url is blank.");
			throw new Exception("the request url is blank.");
		}
		//字符集为空时，默认为UTF-8
		if (StringUtils.isBlank(charset)){
			charset = URL_PARAM_DECODECHARSET_UTF8;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpGet httpGet = new HttpGet(requestURL); 
        HttpResponseBean bean = new HttpResponseBean();
		try {
			// 执行get请求
			HttpResponse httpResponse = httpclient.execute(httpGet);
			//服务器返回状态
	        int statusCode =  httpResponse.getStatusLine().getStatusCode();  
	        //返回服务器响应的HTML代码                                 
	        String entityContent = "";
            if (httpResponse.getEntity() != null) {
            	entityContent = EntityUtils.toString(httpResponse.getEntity(), URL_PARAM_DECODECHARSET_UTF8);     
            }
            //组织返回结果
            bean.setStatusCode(statusCode);
            bean.setEntityContent(entityContent);
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e.toString());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return bean;
	}
    
    /**
	 * HttpPost调用
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static HttpResponseBean httpPost1(String requestURL,Map<String,Object> params,String charset) throws Exception{
		//校验输入参数
		if (StringUtils.isBlank(requestURL)){
			log.error("the request url is blank.");
			throw new Exception("the request url is blank.");
		}
		//字符集为空时，默认为UTF-8
		if (StringUtils.isBlank(charset)){
			charset = URL_PARAM_DECODECHARSET_UTF8;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpPost httpPost = new HttpPost(requestURL);
		HttpResponseBean bean = new HttpResponseBean();
		try {		
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			//循环遍历加载参数
			for (Entry<String, Object> entry : params.entrySet()) {  
				formparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()))); 
			}  
	        httpPost.setEntity(new UrlEncodedFormEntity(formparams, charset));
	        HttpResponse httpResponse = httpclient.execute(httpPost);//执行
	        //服务器返回状态
	        int statusCode =  httpResponse.getStatusLine().getStatusCode();  
	        //返回服务器响应的HTML代码                                 
	        String entityContent = "";
            if (httpResponse.getEntity() != null) {
            	entityContent = EntityUtils.toString(httpResponse.getEntity(), charset);  
            }else{
            	log.info("ResponseEntity is null.");
            }
            //组织返回结果
            bean.setStatusCode(statusCode);
            bean.setEntityContent(entityContent);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e.toString());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	

}
