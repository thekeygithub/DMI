package com.ts.plugin;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ts.util.SSLClient;


/**
 * http client的工具类，用于取得某个url返回的内容 
 * 
 * @author yangqin@dhgate.com
 * 
 */
public class TsHttpsClient {
    private static final int sotimeout = 5000;
    private static final int connectTimeout = 1000;
    private static final Logger log=Logger.getLogger(TsHttpsClient.class);
    private static final String ENCODING_UTF_8 = "UTF-8";
	/**
     * 返回一个http request 的responseString
     * 
     * 
     * @param url
     * @param parameters 要post的parameters
     * 
     * @return string
     * @throws Exception
     */
    public static String postRequest(String url, Map<String, String> parameters) throws Exception {
        return postRequest(url, parameters, 0,null,null,ENCODING_UTF_8);
    }
    
    public static String postRequest(String url, Map<String, String> parameters,Map<String,String> requestHeadMap,String body,String encoding) throws Exception {
        return postRequest(url, parameters, 0,requestHeadMap,body,encoding);
    }
    
    public static String postRequest(String url, Map<String, String> parameters, int timeoutmiliseconds,Map<String,String> requestHeadMap,String body,String encoding) throws Exception {

        HttpClient client = new HttpClient();

        if(timeoutmiliseconds>0){
            HttpClientParams params = new HttpClientParams();
            params.setSoTimeout(timeoutmiliseconds);
            client.setParams(params);
        }
        
        client.getParams().setContentCharset(encoding);

        PostMethod post = null;
        post = new PostMethod(url);
        
        if(ENCODING_UTF_8.equalsIgnoreCase(encoding)){
        	post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        }else{
        	post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
        }
        if(requestHeadMap!=null){
        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                post.setRequestHeader(entry.getKey(),entry.getValue());
            }
        }
        if(StringUtils.isNotBlank(body)){
        	post.setRequestBody(body);
        }
        try{
            // 处理提交parameters
            Set<String> parameterkeySet = parameters.keySet();
            Iterator<String> parameterkeySetNames = parameterkeySet.iterator();
            while (parameterkeySetNames.hasNext()) {
                String parameterkey = parameterkeySetNames.next();
                //System.out.println(parameterkey);
                if(parameterkey!=null){
                	String parametervalue=parameters.get(parameterkey);
                	if(parametervalue!=null){
                		post.setParameter(parameterkey, parametervalue);
                		//System.out.println(parameterkey);
                	}
                }
            }

            int result = client.executeMethod(post);

            if (result == HttpStatus.SC_OK) {

                InputStream inputStream = post.getResponseBodyAsStream();
                return getResponseAsString(inputStream, encoding);
            } else {
                return null;
            }
        }catch(Exception e){
            throw e;
        }finally{
            post.releaseConnection();
        }
    }

    /**
     * use get method to return http request response String
     * 
     * @param url
     * @return String
     * @throws Exception
     */
    public static String getRequest(String url){
    	if(url==null || "".equals(url)) return "";
        HttpClient client = new HttpClient();

        client.getParams().setContentCharset("UTF-8");
        
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler()); 
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        method.setRequestHeader("Connection", "close");
        //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        
        String retstring=null;
        try {
            int result = client.executeMethod(method);
            if (result == HttpStatus.SC_OK) {
            	//byte[] responseBody = method.getResponseBody();
            	// Deal with the response.
            	// Use caution: ensure correct character encoding and is not binary data
            	//retstring= new String(responseBody,"UTF-8");
                retstring = method.getResponseBodyAsString();   
            } 
        } catch (IOException e) {
            log.error("IOException :" +e.getMessage());
        } finally {
        	method.releaseConnection();
        }
        return retstring;
    }
    
	public static String getRequestWithTimeout(String url,
			Map<String, String> requestHeadMap, String encoding, int connTime,
			int soTime) throws Exception {
    	if(url==null || "".equals(url)) return "";
    	
        HttpClient client = new HttpClient();
        if(connTime==0){
        	connTime=connectTimeout;
        }
        if(soTime==0){
        	soTime=sotimeout;
        }
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTime);
        client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
        
        client.getParams().setContentCharset(encoding);
        
        GetMethod method = new GetMethod(url);
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
        method.addRequestHeader("Connection", "close");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        
        if(requestHeadMap!=null){
        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                method.addRequestHeader(entry.getKey(),entry.getValue());
            }
        }
        
        String retstring=null;
        try {
            int result = client.executeMethod(method);

            if (result == HttpStatus.SC_OK) {
                retstring = method.getResponseBodyAsString();   

                InputStream inputStream = method.getResponseBodyAsStream();
                retstring = getResponseAsString(inputStream, encoding);
            } 
        } catch (IOException e) {
        	retstring="-1";
        	throw e;
        } finally {
        	method.releaseConnection();
        }
        return retstring;
    }
    
    public static InputStream getRequestInputStream(String url,Map<String,String> requestHeadMap,String encoding, int connTime,int soTime){
    	
    	InputStream retStream = null;
    	
    	if(url==null || "".equals(url)) return null;
        HttpClient client = new HttpClient();
        if(connTime==0){
        	connTime=connectTimeout;
        }
        if(soTime==0){
        	soTime=sotimeout;
        }
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTime);
        client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
        
        client.getParams().setContentCharset(encoding);
        
        GetMethod method = new GetMethod(url);
        if(ENCODING_UTF_8.equalsIgnoreCase(encoding)){
        	method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        }else{
        	method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
        }
        method.addRequestHeader("Connection", "close");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        
        if(requestHeadMap!=null){
        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                method.addRequestHeader(entry.getKey(),entry.getValue());
            }
        }
        
        String retstring=null;
        try {
            int result = client.executeMethod(method);

            if (result == HttpStatus.SC_OK) {
                retStream = method.getResponseBodyAsStream();
            } 
        } catch (IOException e) {
        	log.error(e.getMessage());
        	retstring="-1";
        } 
        return retStream;
    }
    
    public static InputStream getRequestInputStream(String url){
    	if(url==null || "".equals(url)) return null;
    	
    	GetMethod getMethod = new GetMethod(url);
    	HttpClient client = new HttpClient();        
        InputStream retStream = null;
		try {
			int result = client.executeMethod(getMethod);
            if (result == HttpStatus.SC_OK) {
            	//byte[] responseBody = getMethod.getResponseBody();
            	// Deal with the response.
            	// Use caution: ensure correct character encoding and is not binary data            
            	retStream = getMethod.getResponseBodyAsStream(); 
            } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retStream = null;
		}
         
         return retStream;    	
    }
    
    public static byte[] getRequestByte(String url){
    	if(url==null || "".equals(url)) return null;
    	
    	GetMethod getMethod = new GetMethod(url);
    	getMethod.setRequestHeader("Connection", "close");
    	
    	HttpClient client = new HttpClient();   
    	
    	byte[] responseBody = null;
		try {
			int result = client.executeMethod(getMethod);
            if (result == HttpStatus.SC_OK) {
            	responseBody = getMethod.getResponseBody();
            	// Deal with the response.
            	// Use caution: ensure correct character encoding and is not binary data            
            	//retStream = getMethod.getResponseBodyAsStream(); 
            } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseBody = null;
		}
         
         return responseBody;    	
    }

    private static String getResponseAsString(InputStream is, String encoding) throws UnsupportedEncodingException, IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString(encoding);

    }
    
    public static String postMultipart(String url,File file){
    	String result = null;
    	//String url = "http://wwww.baidu.com/fileuploadservlet?functionname=desc&supplierid=402880f100f59ccd0100f59cd37d0004&imagebannername=test";
    	
    	HttpClient client = new HttpClient();
    	PostMethod post = new PostMethod(url);
    	try{
    		FilePart fp =  new FilePart(file.getName(), file);
            
        	MultipartRequestEntity mrp =   new  MultipartRequestEntity( new  Part[] { fp} , post
                    .getParams());
        	post.setRequestEntity(mrp);
            
            int rst = client.executeMethod(post);

            if (rst == HttpStatus.SC_OK) {

                InputStream inputStream = post.getResponseBodyAsStream();
                result = getResponseAsString(inputStream, "UTF-8");
                log.info(result);
                
            } else {
            	log.warn("url: "+url +" | httpstatus: " + rst);
            }
    	}catch(Exception e){
    		log.error(e);
    	}finally{
            post.releaseConnection();
        }
    	return result;
    }
    /**
     * POST请求获取数据
     * @Title: postMTSJson 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param path   URL地址
     * @param @param post   参数信息json格式
     * @param @return    设定文件 
     * @return String    返回类型 
     * @throws
     */
	public static String postMTSJson(String path, String post) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			// conn.setConnectTimeout(10000);//连接超时 单位毫秒
			// conn.setReadTimeout(2000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
//				httpURLConnection.setRequestProperty("contentType", "UTF-8");  
			httpURLConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(post);// post的参数 xx=xx&yy=yy
			// flush输出流的缓冲
			printWriter.flush();
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toString("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String postMTSJsonSSL(String path, String post) {
		if(path.contains("https"))
		{
			org.apache.http.client.HttpClient httpClient = null;
	        HttpPost httpPost = null;
	        String result = "";
	        try {
	            httpClient = new com.ts.util.SSLClient();
	            httpPost = new HttpPost(path);
	            // 设置参数
	            StringEntity se = new StringEntity(post);
	            se.setContentEncoding("UTF-8");
	            se.setContentType("application/json");// 发送json数据需要设置contentType
	            httpPost.setEntity(se);
	            HttpResponse response = httpClient.execute(httpPost);
	            if (response != null) {
	                HttpEntity resEntity = response.getEntity();
	                
	                if (resEntity != null) {
	                    result = EntityUtils.toString(resEntity, "UTF-8");
//	                    map.put("ret", EntityUtils.toString(resEntity, "UTF-8"));
//	                    map.put("res", response.toString());
//	                    result = map;
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return result;  
		}else{
			URL url = null;
			try {
				url = new URL(path);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("POST");// 提交模式
				// conn.setConnectTimeout(10000);//连接超时 单位毫秒
				// conn.setReadTimeout(2000);//读取超时 单位毫秒
				// 发送POST请求必须设置如下两行
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setDoInput(true);
//					httpURLConnection.setRequestProperty("contentType", "UTF-8");  
				httpURLConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");
				// 获取URLConnection对象对应的输出流
				PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
				// 发送请求参数
				printWriter.write(post);// post的参数 xx=xx&yy=yy
				// flush输出流的缓冲
				printWriter.flush();
				// 开始获取数据
				BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len;
				byte[] arr = new byte[1024];
				while ((len = bis.read(arr)) != -1) {
					bos.write(arr, 0, len);
					bos.flush();
				}
				bos.close();
				return bos.toString("utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}