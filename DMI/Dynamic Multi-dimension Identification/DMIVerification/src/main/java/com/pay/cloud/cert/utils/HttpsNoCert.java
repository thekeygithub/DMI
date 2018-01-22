package com.pay.cloud.cert.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import com.pay.cloud.util.ValidateUtils;

/**
 * HTTPS 无证书
 * @author yanjie.ji
 * @date 2016年7月14日 
 * @time 上午9:45:21
 */
public class HttpsNoCert {
	/**
	 * 默认HTTP METHOD
	 */
	private static final String DEFAULT_METHOD = "POST";
	/**
	 * 默认HTTP CONTENT_TYPE
	 */
	private static final String DEFAULT_CONTENT_TYPE = "text/html";
	/**
	 * 默认HTTP CHARSET
	 */
	private static final String DEFAULT_CHARSET = "utf-8";
	/**
	 * 默认HTTP ACCEPT
	 */
	private static final String DEFAULT_ACCEPT= "text/xml,text/javascript,text/html,application/json";
	/**
	 * 默认HTTP CONNECT_TIME_OUT
	 */
	private static final int CONNECT_TIME_OUT = 5000;
	/**
	 * 默认HTTP READ_TIME_OUT
	 */
	private static final int READ_TIME_OUT = 30000;
	/**
	 * 默认HTTP
	 */
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url,String params)throws Exception{
		return doPost(url,params,DEFAULT_METHOD,DEFAULT_CONTENT_TYPE,DEFAULT_CHARSET,CONNECT_TIME_OUT,READ_TIME_OUT);
	}
	/**
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url,String params,String method)throws Exception{
		return doPost(url,params,method,DEFAULT_CONTENT_TYPE,DEFAULT_CHARSET,CONNECT_TIME_OUT,READ_TIME_OUT);
	}
	/**
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @param content_type
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url,String params,String method,String content_type)throws Exception{
		return doPost(url,params,method,content_type,DEFAULT_CHARSET,CONNECT_TIME_OUT,READ_TIME_OUT);
	}
	/**
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @param content_type
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url,String params,String method,String content_type,String charset)throws Exception{
		return doPost(url,params,method,content_type,charset,CONNECT_TIME_OUT,READ_TIME_OUT);
	}
	/**
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @param content_type
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params,String method,String content_type,String charset,
			int connectTimeout, int readTimeout) throws Exception {
		if(StringUtils.isNotEmpty(content_type)&&StringUtils.isNotEmpty(charset))
			content_type = content_type+";chartset="+charset;
		byte[] content = {};
		if (params != null) {
			content = params.getBytes(charset);
		}
		return doPost(url,content,method, content_type,charset,connectTimeout, readTimeout);
	}
	/**
	 * 
	 * @param url
	 * @param content
	 * @param method
	 * @param ctype
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	private static String doPost(String url, byte[] content,String method, String ctype,String charset,
			int connectTimeout, int readTimeout) throws Exception {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				SSLContext ctx = SSLContext.getInstance("TLS");
				ctx.init(null,new TrustManager[] { new NoCertTrustManager() },	null);
				SSLContext.setDefault(ctx);
				
				SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
		        SchemeRegistry sr = ccm.getSchemeRegistry();
		        sr.register(new Scheme("https", 443, ssf));
		        
				conn = getConn(url,method,ctype,content.length,connectTimeout,readTimeout);
				conn.setSSLSocketFactory(ctx.getSocketFactory());
				conn.setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
			} catch (Exception e) {
				throw e;
			}
			try {
				out = conn.getOutputStream();
				out.write(content);
				out.flush();
				out.close();
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		if(ValidateUtils.isNotEmpty(rsp)){
			rsp = rsp.trim();
			if(rsp.startsWith("\"")){
				rsp = rsp.substring(1);
			}
			if(rsp.endsWith("\"")){
				rsp = rsp.substring(0, rsp.length() - 1);
			}
			if(rsp.contains("\\")){
				rsp = rsp.replace("\\", "");
			}
		}
		return rsp;
	}
	/**
	 * 
	 * @param url
	 * @param method
	 * @param ctype
	 * @param length
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static HttpsURLConnection getConn(String url,String method,String ctype,int length,
			int connectTimeout, int readTimeout) throws MalformedURLException, IOException{
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept",DEFAULT_ACCEPT);
		conn.setRequestProperty("User-Agent", USER_AGENT);
//		conn.setRequestProperty("Content-Type", ctype);
		conn.setRequestProperty("Content-Length", String.valueOf(length));
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}
	/**
	 * 
	 * @author yanjie.ji
	 * @date 2016年7月18日 
	 * @time 上午11:30:24
	 */
	private static class NoCertTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}
	/**
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	private static String getResponseAsString(HttpURLConnection conn)
			throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}
	/**
	 * 
	 * @param stream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
	/**
	 * 
	 * @param ctype
	 * @return
	 */
	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

}
