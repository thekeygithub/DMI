package com.ts.controller.app.SearchAPI.BusinessAPI.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ts.util.DbFH;

/**
 * @描述：当spring容器初始化完成后就会执行该类中的onApplicationEvent方法
 * @作者：SZ
 * @时间：2016年10月31日 上午10:49:34
 */
public class ReadPropertiesFiles {
	/**
	 * 读取apiconfig.properties 配置文件 返回solr服务相应的请求路径
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getSolrUrl(String solrProjectName) {
		InputStream inputStream = DbFH.class.getClassLoader().getResourceAsStream("apiconfig.properties");
		Properties pros = new Properties();
		String url = null;
		try {
			pros.load(inputStream);
			url = pros.getProperty("solr.query_http_protocol") + "://" + pros.getProperty("solr.query_ip") + ":"
					+ pros.getProperty("solr.query_port") + "/" + pros.getProperty("solr.query_project") + "/"
					+ pros.getProperty(solrProjectName);
		} catch (IOException e) {
			// 读取配置文件出错
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	/**
	 * 根据key读取apiconfig.properties 配置文件 返回值
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getValue(String key) {
		InputStream inputStream = DbFH.class.getClassLoader().getResourceAsStream("apiconfig.properties");
		Properties pros = new Properties();
		String value = null;
		try {
			pros.load(inputStream);
			value = pros.getProperty(key);
		} catch (IOException e) {
			// 读取配置文件出错
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
