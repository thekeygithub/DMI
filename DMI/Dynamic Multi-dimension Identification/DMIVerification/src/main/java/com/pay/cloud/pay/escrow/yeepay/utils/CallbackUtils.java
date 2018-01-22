package com.pay.cloud.pay.escrow.yeepay.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * UrlConnection通讯辅助类
 * 
 * @author junning.li
 * 
 */
public class CallbackUtils {
	// 设置URL缓存时间为1小时
	static {
		System.setProperty("sun.net.inetaddr.ttl", "3600");
//		System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
	}
	
	public CallbackUtils(){
	}

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(CallbackUtils.class);

	/**
	 * 默认字符编码
	 */
	public static final String DEFAULT_CHARSET = "GBK";

	public static final String HTTP_METHOD_POST = "POST";

	public static final String HTTP_METHOD_GET = "GET";
	
	public static final String HTTP_ERROR_MESSAGE = "http_error_message";

	/**
	 * 默认超时设置(120秒)
	 */
	public static final int DEFAULT_TIMEOUT = 120000;

	/**
	 * 默认提交方式
	 */
	public static final String HTTP_METHOD_DEFAULT = "GET";

	public static final String HTTP_PREFIX = "http://";

	public static final String HTTPS_PREFIX = "https://";

	//最多只读取500000字符
	public static final int MAX_FETCHSIZE = 500000;
	
	/**
	 * Ip正则表达式
	 */
	public static String IPREG = new String("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$");
	/**
	 * 中国地区主域
	 */
	static Set DOMAINSUFFIX = new HashSet(){{add("me");add("tw");add("cat");add("jobs");add("tel");add("xxx");add("mail");add("kid");add("us");add("cc");add("hk");add("tv");add("com");add("edu");add("gov");add("net");add("org");add("bbr");add("cn");add("firm");add("store");add("web");add("arts");add("rec");add("info");add("nom");add("biz");add("name");add("pro");add("coop");add("aero");add("museum");add("mil");add("mobi");add("pro");add("travel");add("int");add("post");add("asia");}};	
	
	/**
	 * 判断一个host是否是Ip类型
	 */
	public static boolean isIpAddress(String domain){
		return Pattern.matches(IPREG, domain);
	}
	
	/**
	 * 获取域名的主域
	 * 只专门针对国内用的域名予以解析
	 * urlLink需要有协议开头，否则返回urlLink本身
	 * @param str
	 */
	public static String getMainDomain(String urlLink){
		if(urlLink==null||urlLink.equals("")){
			return null;
		}
		urlLink = urlLink.toLowerCase();
		try {
			if(!urlLink.startsWith("http://")&&!urlLink.startsWith("https://")){
				urlLink="http://"+urlLink;
			}
			URL url = new URL(urlLink);
			String domain = url.getHost();
			return getDomain(domain);
		}catch (MalformedURLException e) {
			return urlLink;
		}
	}
	
	public static String getHost(String urlLink){
		try {
			URL url = new URL(urlLink);
			return url.getHost();
		}catch (MalformedURLException e) {
			return urlLink;
		} 
	}
	
	private static String getDomain(String host){
		if(host==null||host.equals("")){
			return null;
		}
		host = host.toLowerCase();
		if(isIpAddress(host)){
			return host;
		}
		String terms[] = host.split("\\.");
		for(int i=terms.length-1;i>=0;i--){
			if(DOMAINSUFFIX.contains(terms[i])){
				continue;
			}else{
				return terms[i];
			}
		}
		return host;
	}
	
	public static String httpRequest(String url, String method) {
		return httpRequest(url, "", method, DEFAULT_CHARSET);
	}

	public static String httpRequest(String url, String queryString,
			String method) {
		return httpRequest(url, queryString, method, DEFAULT_CHARSET);
	}

	public static String httpRequest(String url, Map params, String method) {
		return httpRequest(url, params, method, DEFAULT_CHARSET);
	}

	public static String httpPost(String url, Map params) {
		return httpRequest(url, params, HTTP_METHOD_POST, DEFAULT_CHARSET);
	}

	public static String httpPost(String url, String queryString) {
		return httpRequest(url, queryString, HTTP_METHOD_POST, DEFAULT_CHARSET);
	}

	public static String httpGet(String url, Map params) {
		return httpRequest(url, params, HTTP_METHOD_GET, DEFAULT_CHARSET);
	}

	public static String httpGet(String url, String queryString) {
		return httpRequest(url, queryString, HTTP_METHOD_GET, DEFAULT_CHARSET);
	}

	/**
	 * 以建立HttpURLConnection方式发送请求
	 * 
	 * @param targetUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方式
	 * @param charSet
	 * @return 通讯失败返回null, 否则返回服务端输出
	 */
	public static String httpRequest(String url, Map params, String method,
			String charSet) {
		String queryString = parseQueryString(params, charSet);
		return httpRequest(url, queryString, method, charSet);
	}

	public static String httpRequest(String targetUrl, String queryString,
			String sMethod, String charSet) {
		return httpRequest(targetUrl, queryString, sMethod, charSet, true);
	}
	
	/**
	 * 
	 * @param targetUrl
	 * @param queryString
	 * @param sMethod
	 * @param charSet
	 * @param sslVerify
	 * @return
	 */
	public static String httpRequest(String targetUrl, String queryString,
			String sMethod, String charSet, boolean sslVerify) {
		
		HttpURLConnection urlConn = null;
		URL destURL = null;
		boolean httpsFlag = false;
		if (targetUrl == null || targetUrl.trim().length() == 0) {
			throw new IllegalArgumentException("invalid targetUrl : "
					+ targetUrl);
		}
		targetUrl = targetUrl.trim();

		// if(targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)){
		// throw new IllegalArgumentException("unsupport protocal : https");
		// }
		if (targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)) {
			httpsFlag = true;
		} else if (!targetUrl.toLowerCase().startsWith(HTTP_PREFIX)) {
			targetUrl = HTTP_PREFIX + targetUrl;
		}

		// if(!targetUrl.toLowerCase().startsWith(HTTP_PREFIX) &&
		// !targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)){
		// targetUrl = HTTP_PREFIX+targetUrl;
		// }
		if (queryString != null) {
			queryString = queryString.trim();
		}

		// 把参数sMethod转换为大写的字符串method，然后用method进行后续处理,lzhang 2009-1-12
		String method = null;
		if (sMethod != null) {
			method = sMethod.toUpperCase();
		}
		
		if (method == null
				|| !(method.equals(HTTP_METHOD_POST) || method
						.equals(HTTP_METHOD_GET))) {
			throw new IllegalArgumentException("invalid http method : "
					+ method);
		}

		String baseUrl = "";
		String params = "";
		String fullUrl = "";

		int index = targetUrl.indexOf("?");
		if (index != -1) {
			baseUrl = targetUrl.substring(0, index);
			params = targetUrl.substring(index + 1);
		} else {
			baseUrl = targetUrl;
		}
		if (queryString != null && queryString.trim().length() != 0) {
			if (params.trim().length() > 0) {
				params += "&" + queryString;
			} else {
				params += queryString;
			}
		}

		fullUrl = baseUrl + (params.trim().length() == 0 ? "" : ("?" + params));
		StringBuffer result = new StringBuffer(2000);
		try {
			if (method.equals(HTTP_METHOD_POST)) {
				destURL = new URL(baseUrl);
			} else {
				destURL = new URL(fullUrl);
			}
			
			urlConn = (HttpURLConnection)destURL.openConnection();
			
			//是否不进行SSL验证
			
			if(!sslVerify && (urlConn instanceof HttpsURLConnection || urlConn instanceof com.sun.net.ssl.HttpsURLConnection)){
				System.out.println(urlConn.getClass()+"-------");
				SSLContext sc = SSLContext.getInstance("SSL");
		        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
		        if(urlConn instanceof HttpsURLConnection){
		        	HttpsURLConnection httpsUrlCon = (HttpsURLConnection)urlConn;
		        	httpsUrlCon.setSSLSocketFactory(sc.getSocketFactory());
		        	httpsUrlCon.setHostnameVerifier(new TrustAnyHostnameVerifier());
		        }
		        if(urlConn instanceof com.sun.net.ssl.HttpsURLConnection){
		        	com.sun.net.ssl.HttpsURLConnection httpsUrlCon = (com.sun.net.ssl.HttpsURLConnection)urlConn;
		        	httpsUrlCon.setSSLSocketFactory(sc.getSocketFactory());
		        	httpsUrlCon.setHostnameVerifier(new TrustAnyHostnameVerifierOld());
		        }
			}
	        
			logger.info("-->"+urlConn.getClass());
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=" + charSet);
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setAllowUserInteraction(false);
			urlConn.setUseCaches(false);
			urlConn.setRequestMethod(method);
			urlConn.setConnectTimeout(DEFAULT_TIMEOUT);
			urlConn.setReadTimeout(DEFAULT_TIMEOUT);

			if (method.equals(HTTP_METHOD_POST)) {
				OutputStream os = urlConn.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, charSet);
				osw.write(params);
				osw.flush();
				osw.close();
			}

			BufferedInputStream is = new BufferedInputStream(urlConn
					.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charSet));
			String temp = null;
			
			while ((temp = br.readLine()) != null) {
				result.append(temp);
				result.append("\n");
				
				if(result.length()>MAX_FETCHSIZE){
					break;
				}
			}
			int responseCode = urlConn.getResponseCode();
			logger.info("------------------ResponseCode[" + responseCode
					+ "],and targetUrl[" + targetUrl + "] queryString[" + queryString
					+ "]" + "and resultLength[" + result.length() + "]");
			if (responseCode != HttpURLConnection.HTTP_OK) {
				return null;
			}
			return result.toString();
		} catch (Exception e) {
			logger.warn("connection error : " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
	}

	public static String httpRequest(String targetUrl, String queryString,
			String sMethod, String charSet,String requestProperty, boolean sslVerify) {
		HttpURLConnection urlConn = null;
		URL destURL = null;
		boolean httpsFlag = false;
		if (targetUrl == null || targetUrl.trim().length() == 0) {
			throw new IllegalArgumentException("invalid targetUrl : "
					+ targetUrl);
		}
		targetUrl = targetUrl.trim();

		// if(targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)){
		// throw new IllegalArgumentException("unsupport protocal : https");
		// }
		if (targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)) {
			httpsFlag = true;
		} else if (!targetUrl.toLowerCase().startsWith(HTTP_PREFIX)) {
			targetUrl = HTTP_PREFIX + targetUrl;
		}

		// if(!targetUrl.toLowerCase().startsWith(HTTP_PREFIX) &&
		// !targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)){
		// targetUrl = HTTP_PREFIX+targetUrl;
		// }
		if (queryString != null) {
			queryString = queryString.trim();
		}

		// 把参数sMethod转换为大写的字符串method，然后用method进行后续处理,lzhang 2009-1-12
		String method = null;
		if (sMethod != null) {
			method = sMethod.toUpperCase();
		}
		
		if (method == null
				|| !(method.equals(HTTP_METHOD_POST) || method
						.equals(HTTP_METHOD_GET))) {
			throw new IllegalArgumentException("invalid http method : "
					+ method);
		}

		String baseUrl = "";
		String params = "";
		String fullUrl = "";

		int index = targetUrl.indexOf("?");
		if (index != -1) {
			baseUrl = targetUrl.substring(0, index);
			params = targetUrl.substring(index + 1);
		} else {
			baseUrl = targetUrl;
		}
		if (queryString != null && queryString.trim().length() != 0) {
			if (params.trim().length() > 0) {
				params += "&" + queryString;
			} else {
				params += queryString;
			}
		}

		fullUrl = baseUrl + (params.trim().length() == 0 ? "" : ("?" + params));
		StringBuffer result = new StringBuffer(2000);
		try {
			if (method.equals(HTTP_METHOD_POST)) {
				destURL = new URL(baseUrl);
			} else {
				destURL = new URL(fullUrl);
			}
			
			urlConn = (HttpURLConnection)destURL.openConnection();
			
			//是否不进行SSL验证
			
			if(!sslVerify && (urlConn instanceof HttpsURLConnection || urlConn instanceof com.sun.net.ssl.HttpsURLConnection)){
				System.out.println(urlConn.getClass()+"-------");
				SSLContext sc = SSLContext.getInstance("SSL");
		        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
		        if(urlConn instanceof HttpsURLConnection){
		        	HttpsURLConnection httpsUrlCon = (HttpsURLConnection)urlConn;
		        	httpsUrlCon.setSSLSocketFactory(sc.getSocketFactory());
		        	httpsUrlCon.setHostnameVerifier(new TrustAnyHostnameVerifier());
		        }
		        if(urlConn instanceof com.sun.net.ssl.HttpsURLConnection){
		        	com.sun.net.ssl.HttpsURLConnection httpsUrlCon = (com.sun.net.ssl.HttpsURLConnection)urlConn;
		        	httpsUrlCon.setSSLSocketFactory(sc.getSocketFactory());
		        	httpsUrlCon.setHostnameVerifier(new TrustAnyHostnameVerifierOld());
		        }
			}
	        
			logger.info("-->"+urlConn.getClass());
			urlConn.setRequestProperty("Content-Type",
					requestProperty);
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setAllowUserInteraction(false);
			urlConn.setUseCaches(false);
			urlConn.setRequestMethod(method);
			urlConn.setConnectTimeout(DEFAULT_TIMEOUT);
			urlConn.setReadTimeout(DEFAULT_TIMEOUT);

			if (method.equals(HTTP_METHOD_POST)) {
				OutputStream os = urlConn.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, charSet);
				osw.write(params);
				osw.flush();
				osw.close();
			}

			BufferedInputStream is = new BufferedInputStream(urlConn
					.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charSet));
			String temp = null;
			
			while ((temp = br.readLine()) != null) {
				result.append(temp);
				result.append("\n");
				
				if(result.length()>MAX_FETCHSIZE){
					break;
				}
			}
			int responseCode = urlConn.getResponseCode();
			logger.info("------------------ResponseCode[" + responseCode
					+ "],and targetUrl[" + targetUrl + "] queryString[" + queryString
					+ "]" + "and resultLength[" + result.length() + "]");
			if (responseCode != HttpURLConnection.HTTP_OK) {
				return null;
			}
			return result.toString();
		} catch (Exception e) {
			logger.warn("connection error : " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
	}
	
	/**
	 * 把参数map转换成URL
	 * 
	 * @param params
	 * @param charSet
	 * @return
	 */
	public static String parseQueryString(Map params, String charSet) {
		if (null == params || params.keySet().size() == 0) {
			return "";
		}
		StringBuffer queryString = new StringBuffer(2000);
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = String.valueOf(i.next());
			Object obj = params.get(key);
			String value = "";
			if (obj != null) {
				value = obj.toString();
			}
			try {
				value = URLEncoder.encode(value, charSet);
			} catch (UnsupportedEncodingException ex) {
				logger.info("encode url error: " + ex.getMessage());
			}
			queryString.append(key);
			queryString.append("=");
			queryString.append(value);
			queryString.append("&");
		}
		String result = queryString.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	
	public static String parseUrl(String targetUrl, String queryString) {
		if (targetUrl == null || targetUrl.trim().length() == 0) {
			throw new IllegalArgumentException("invalid targetUrl : "
					+ targetUrl);
		}
		targetUrl = targetUrl.trim();
		
		if (!targetUrl.startsWith("/") && !targetUrl.toLowerCase().startsWith(HTTP_PREFIX)
				&& !targetUrl.toLowerCase().startsWith(HTTPS_PREFIX)) {
			targetUrl = HTTP_PREFIX + targetUrl;
		}

		if (queryString != null) {
			queryString = queryString.trim();
		}
		String baseUrl = "";
		String paramString = "";
		String fullUrl = "";
		int index = targetUrl.indexOf("?");
		if (index != -1) {
			baseUrl = targetUrl.substring(0, index);
			paramString = targetUrl.substring(index + 1);
		} else {
			baseUrl = targetUrl;
		}
		if (queryString != null && queryString.trim().length() != 0) {
			if (paramString.trim().length() > 0) {
				paramString += "&" + queryString;
			} else {
				paramString += queryString;
			}
		}
		fullUrl = baseUrl
				+ (paramString.trim().length() == 0 ? "" : ("?" + paramString));
		return fullUrl;
	}

	public static String parseUrl(String targetUrl, Map params, String charSet) {
		String queryString = parseQueryString(params, charSet);
		return parseUrl(targetUrl, queryString);
	}

	public static Map parseQueryString(String queryString) {
		if (queryString == null) {
			throw new IllegalArgumentException("queryString must be specified");
		}

		int index = queryString.indexOf("?");
		if (index > 0) {
			queryString = queryString.substring(index + 1);
		}

		String[] keyValuePairs = queryString.split("&");
		Map <String, String>map = new HashMap<String, String>();
		for (String keyValue : keyValuePairs) {
			if (keyValue.indexOf("=") == -1) {
				continue;
			}
			String[] args = keyValue.split("=");
			if (args.length == 2) {
				map.put(args[0], args[1]);
			}
			if (args.length == 1) {
				map.put(args[0], "");
			}
		}
		return map;
	}

	public static String parseUrl(String queryString) {
		if (queryString == null) {
			throw new IllegalArgumentException("queryString must be specified");
		}

		int index = queryString.indexOf("?");
		String targetUrl = null;
		if (index > 0) {
			targetUrl = queryString.substring(0, index);
		} else {
			targetUrl = queryString;
		}
		return targetUrl;
	}
	
	private static class TrustAnyTrustManager implements X509TrustManager {
	    
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    
    private static class  TrustAnyHostnameVerifierOld implements com.sun.net.ssl.HostnameVerifier{
		public boolean verify(String arg0, String arg1) {
			return true;
		}
    }
    
    public static void main(String[] args){
    	System.out.println(httpGet("https://www.yeepay.com/",""));
    }
}