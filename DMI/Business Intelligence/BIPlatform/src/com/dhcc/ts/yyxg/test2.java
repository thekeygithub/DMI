package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dhcc.ts.database.DBManager_HIS;

import net.sf.json.JSONObject;


public class test2 {
	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static String httpPost(String url, JSONObject jsonParam) {
		return httpPost(url, jsonParam, false);
	}

	/**
	 * @描述：post请求
	 * @作者：SZ
	 * @时间：2017年2月16日 下午1:00:42
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static String httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
		// post请求返回结果
		String jsonResult = null;
//		HttpClient httpClient = HttpClientFactory.createHttpClient();
		HttpClient httpClient = HttpClients.custom().build();
		HttpPost method = new HttpPost(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					jsonResult = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					// jsonResult = JSONObject.fromObject(str);
				} catch (Exception e) {
					System.out.println("post请求提交失败:" + url + "=======" + e);
				}
			}
		} catch (IOException e) {
			System.out.println("post请求提交失败:" + url + "=======" + e);
		}
		return jsonResult;
	}
	
	public static Map<String ,String> readerXML(String xml){
		Map<String ,String> map = new HashMap<String, String>();
		try {
			Document document = DocumentHelper.parseText(xml);  
            Element bookstore = document.getRootElement();  
            Iterator storeit = bookstore.elementIterator(); 
			
			while(storeit.hasNext()){
				Element bookElement = (Element) storeit.next();
				List<Attribute> attributes = bookElement.attributes();
				
				for(Attribute attribute : attributes){  
                    System.out.println(attribute.getName());
                }
				
				Iterator bookit = bookElement.elementIterator();  
	            while(bookit.hasNext()){
	            	Element child = (Element) bookit.next();  
                    String nodeName = child.getName();
                    System.out.println(nodeName);
	            }
			}
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	

	public static void main(String[] args) throws SQLException {
		
		String url = "http://10.10.50.15:8080/MTS/mtsStandardize/standardizeDataForZNWD.do";
	
		String kfurl = "http://113.6.246.23/MTS/mtsStandardize/standardizeData.do";
		JSONObject jsonParam = new JSONObject();

//		jsonParam.put("visitId", "123456789");
//		jsonParam.put("visitType", "01");
//		jsonParam.put("dataSource", "10001");
//		jsonParam.put("dataType", "02");
//		jsonParam.put("parameters", "05@#&颈椎病，偏身麻木@|&09@#&I63.903");

		jsonParam.put("areaId", "410202");
		jsonParam.put("parameters", "高血压");
//		
//		
		String str = httpPost(url, jsonParam);
		
		System.out.println(str);
		
//		String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" + "<student name=\"student\">"
//				+ "<age name=\"age2\">12</age>" 
//				+ "<age name=\"age1\">11</age>" 
//				+ "<id name=\"id\">1</id>"
//				+"<ZYX> <ZYX.1>13071550</ZYX.1><ZYX.7>李宜清</ZYX.7><ZYX.28>05</ZYX.28></ZYX>"
//				+ "<name name=\"name\">is_zhoufeng</name>" + "</student>"
//		;
//		
//		System.out.println("=========");
//		readerXML(xml);
		
		test2 t = new test2();
		t.test();
//		String ss = "\"移动性浊音\": \"none\",\"叩痛\": \"none\"";
//		System.out.println(ss.replace("\"", ""));
	}
	
	@SuppressWarnings("unchecked")
	private void test() throws SQLException {
		String sql = "select hr.cmzh, hre.ccfzd, hre.ccfh from H_REG hr inner join H_RECIPE hre on hr.cmzh = hre.cmzh";
		DBManager_HIS db_his = new DBManager_HIS();
		List<Map<String, Object>> list = db_his.executeQueryListHashMap(sql);
		for (Map<String, Object> map : list) {
			String cmzh = (String) map.get("CMZH"); // 门诊号
			String ccfzd = (String) map.get("CCFZD");
			String ccfh = (String) map.get("CCFH"); // 处方号
			
			String sssql = "select CYPMC from H_RECIPE_DETAIL where ccfh='"+ccfh+"'";
			List<Map<String, Object>> yplist = db_his.executeQueryListHashMap(sssql);
			StringBuilder ss = new StringBuilder();
			if (yplist != null && yplist.size() > 0) {
				for (Map<String, Object> m : yplist) {
					ss.append(",").append(m.get("CYPMC"));
					
				}
			}
			
			String url = "http://10.10.50.15:8080/MTS/mtsStandardize/standardizeDataForZNWD.do";
			JSONObject jsonParam = new JSONObject();
			if ("?".equals(ccfzd)) continue;
			
			jsonParam.put("areaId", "410202");
			jsonParam.put("parameters", ccfzd + ss);
			
			System.out.println("=====调用参数====" + cmzh + ":" + jsonParam.toString());
			String str = httpPost(url, jsonParam);
			System.out.println("=====调用结果====" + str);
			JSONObject jobj = JSONObject.fromObject(str);
			JSONObject result = (JSONObject)jobj.get("result");
			JSONObject obj = (JSONObject)result.get("NlpValue");
			JSONObject o = (JSONObject) obj.get("诊断");
			JSONObject y = (JSONObject) obj.get("药品");
			String ccfzd_std = o == null ? "":o.toString().replace("\"", "");
			String cypmc_std = y == null ? ",":y.toString().replace("\"", "");
			ccfzd_std = ccfzd_std.substring(1, ccfzd_std.length()-1); // 诊断标准化
			cypmc_std = cypmc_std.substring(1, cypmc_std.length()-1); // 药品标准
			String yp = ss.length() > 0 ? "@#&"+ss.substring(1):"";
			String param = ccfzd+yp; // 调用前参数      诊断@#&药品1,药品2
			String ssql = "insert into h_diag_std (cmzh, ccfzd, ccfzd_std, ccfh, cypmc_std) values ('"+cmzh+"','"+param+"','"+ccfzd_std+"','"+ccfh+"','"+cypmc_std+"')";
//			String ssssql = "update h_diag_std set CYPMC_STD='"+cypmc_std+"' where ccfh = '" + ccfh + "'";
			System.out.println(ssql);
//			db_his.executeUpdate(ssql);
		}
		db_his.close();
	}
	
	
}
