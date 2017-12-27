package com.ebmi.std.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.core.utils.JsonStringUtils;

public class HttpTools {
    private static RequestConfig requestConfig;  
    private static final int MAX_TIMEOUT = 7000;  
  
    static {  
        RequestConfig.Builder configBuilder = RequestConfig.custom();  
        // 设置连接超时  
        configBuilder.setConnectTimeout(MAX_TIMEOUT);  
        // 设置读取超时  
        configBuilder.setSocketTimeout(MAX_TIMEOUT);  
        // 设置从连接池获取连接实例的超时  
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);  
        // 在提交请求之前 测试连接是否可用  
        configBuilder.setStaleConnectionCheckEnabled(true);  
        requestConfig = configBuilder.build();  
    }  
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数应该是JSON对象的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static String sendJsonPost(String url, JSONObject jsonParam) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		try {
            Map<String, String> params = new HashMap<String, String>();
            params = JsonStringUtils.jsonStringToObject(jsonParam.toString(), Map.class);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
    		Set<String> keySets = params.keySet();
    		for(String key: keySets){
    			String value = params.get(key);
    			list.add(new BasicNameValuePair(key, value));
    		}
    		httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			httpPost.setConfig(requestConfig);
			String resData = "";
			try {
				response = httpclient.execute(httpPost);
	            // 请求结束，返回结果  
	            resData = EntityUtils.toString(response.getEntity());
			} finally {  
                response.close();  
            }
            return resData;
		}
        catch(Exception e){
        	e.printStackTrace();
        	return null;
        }finally{
        	if (httpclient != null) {  
                try {  
                    httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }
			httpPost.releaseConnection();
		}
	}
	
	/**
	 * 向指定 URL 发送SSL的POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数应该是JSON对象的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws IOException 
	 */
	public static String sendJsonPostSSL(String url, JSONObject jsonParam) {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(createConnMgr()).setDefaultRequestConfig(requestConfig).build();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(jsonParam.toString(),"UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding("UTF-8");  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);
			String resData = "";
			try {
				response = httpclient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
	            if (statusCode != HttpStatus.SC_OK) {  
	                return null;  
	            }
	            HttpEntity entity = response.getEntity();  
	            if (entity == null) {  
	                return null;  
	            }
	            // 请求结束，返回结果  
	            resData = EntityUtils.toString(entity, "utf-8");
			} finally {  
                response.close();  
            }
            return resData;
		}
        catch(Exception e){
        	e.printStackTrace();
        	return null;
        }finally{
        	if (httpclient != null) {  
                try {  
                    httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }
			httpPost.releaseConnection();
		}
	}
	
	/**
     * 主要使用这个接口通过http发送POST请求
     * @param url
     * @param 
     * @return
     */
	public static String httpConnectionPost(String urlStr, String code){
		String res = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			// 把数据写入请求的Body
            out.write(code);
            out.flush();
            out.close();
            try {
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                	res = readString(conn.getInputStream());
                } else {
                    if (conn.getErrorStream() != null) {
                    	res = readString(conn.getErrorStream());
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			
		}
		return res;
	}
	
	/**
	 * 向慧芯金服接口 URL 发送POST方法的请求
	 * 
	 * @param urlStr
	 *            发送请求的 URL
	 * @param fileMap
	 *            请求参数
	 * @return 所代表远程资源的响应结果
	 */
	public static String uploadImg(String urlStr, Map<String, String> fileMap){
		String res = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
				
			//这里设置Body数据类型,即图片类型,这里可根据具体图片格式进行调整 image/jpeg、image/png、application/octet-stream
			conn.setRequestProperty("Content-Type", "image/jpeg");
			//打开连接
			OutputStream out = new DataOutputStream(conn.getOutputStream());

			if (fileMap != null) {
				Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) continue;
						
					// 图片数据发送
					File file = new File(inputValue);
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			out.flush();
			out.close();

			//读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			
		}
		return res;
	}
	
    /**
     * 主要使用这个接口 Json 构成 Request Body
     * 
     * @return
     */
    public static JSONObject httpPostEx(String url, String entity) {
        HttpURLConnection conn = null;
        JSONObject jObject = null;
        try {
            URL connUrl = new URL(url);
            conn = (HttpURLConnection) connUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
                          
            // 得到请求的输出流对象  
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());  
            // 把数据写入请求的Body  
            out.write(entity);  
            out.flush();  
            out.close(); 
           
            String errString = null;
            try {
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    jObject = new JSONObject(readString(conn.getInputStream()));
                } else {
                    if (conn.getErrorStream() != null) {
                        errString = readString(conn.getErrorStream());
                        jObject = new JSONObject(errString);
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }

        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
		return jObject;

    }
    
    /**
     * 处理流文件生成字符编码
     * 
     * @return
     */
    private static String readString(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        byte[] toBuf = null;
        try {
            len = in.read(buf);
            while (len > 0) {
                out.write(buf, 0, len);
                len = in.read(buf);
            }
            toBuf = out.toByteArray();

            return new String(toBuf, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    /**
     * 处理流文件生成字符编码
     * 
     * @return
     */
	public static String dataControl(String str){
		if(str!=null && "0".equals(str)){
			return str;
		}
		float flat = Float.parseFloat(str) / 1024 / 1024;
		Double ble = Double.parseDouble(String.valueOf(flat));
		return String.format("%.3f", ble);
	}
	
    /** 
     * 创建SSL安全连接 
     * 
     * @return 
     */  
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {  
        SSLConnectionSocketFactory sslsf = null;  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
  
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {  
  
                @Override  
                public boolean verify(String arg0, SSLSession arg1) {  
                    return true;  
                }  
  
                @Override  
                public void verify(String host, SSLSocket ssl) throws IOException {  
                }  
  
                @Override  
                public void verify(String host, X509Certificate cert) throws SSLException {  
                }  
  
                @Override  
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {  
                }  
            });  
        } catch (GeneralSecurityException e) {  
            e.printStackTrace();  
        }
        return sslsf;
    }
    
    /**  
     * httpclient4.5.2版  
     * 忽略服务器证书，采用信任机制  
     * @return  
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static HttpClientConnectionManager createConnMgr(){
        try {
//          SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslContext, new String[] { "TLSv1" }, null,
//                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SSLConnectionSocketFactory sslsf = createSSLConnSocketFactory();
			Registry registry = RegistryBuilder. create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslsf).build();
			PoolingHttpClientConnectionManager connMgr1 = new PoolingHttpClientConnectionManager(registry);
            connMgr1.setMaxTotal(100);
            connMgr1.setDefaultMaxPerRoute(100);
            return connMgr1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	public static void main(String[] args) throws Exception {
        //发送 GET 请求
		JSONObject jsonparam = new JSONObject();
//        String s=HttpTools.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
//        System.out.println(s);
//		jsonparam.put("q", "name3:感冒");
//		jsonparam.put("wt", "json");
//		jsonparam.put("indent", "true");
//        //发送 POST 请求
//        String sr=HttpTools.sendJsonPost("http://10.10.50.33:8080/solrone/shishiku/select", jsonparam);

		jsonparam.put("token", "6b458bffbf806d6935e105412213411f");
		jsonparam.put("code", "ssfe333fd");
		jsonparam.put("gid", "信息");
		jsonparam.put("apitype", "6");
		jsonparam.put("ispage", "1");
		jsonparam.put("pageno", "1");
		jsonparam.put("pagesize", "10");
		jsonparam.put("totalpage", "1");
		jsonparam.put("totals", "1");
		jsonparam.put("format", "2");
		JSONObject jsonparam1 = new JSONObject();
		jsonparam1.put("requestHandler", "/select");
		JSONArray json1 = new JSONArray();
		json1.put("question:我感冒了怎么办？");
		jsonparam1.put("query", json1);
		JSONArray json2 = new JSONArray();
		json2.put("question:*");
		jsonparam1.put("filterQuery", json2);
		JSONObject json3 = new JSONObject();
		jsonparam1.put("sortField", json3);
		jsonparam.put("wheres", jsonparam1);
		JSONArray jsonparam2 = new JSONArray();
		jsonparam2.put("p_id");
		jsonparam2.put("question");
		jsonparam2.put("answer");
		jsonparam.put("showfield", jsonparam2);

//		jsonparam.put("user", "kl");
//		jsonparam.put("pwd", "1");

        String sr=HttpTools.sendJsonPostSSL("https://10.10.50.13/app/BusinessHandler.do", jsonparam);
        System.out.println(sr);
    }
}