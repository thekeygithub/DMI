package com.pay.cloud.util.hint;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @Description: 通用PROPERTIES文件读取器
 * @ClassName: Propertie 
 * @author: zhengping.hu
 * @date: 2015年11月20日 上午10:40:04
 */
public enum Propertie {
	APPLICATION("/application.properties")
	,HINT("/Hint.properties")
	,H5HINT("/H5Hint.properties")
	,IDCARD("/idcard.properties")
	//custom properties files······
	;
	
	private Properties property;
	private final String url;
	
	private Propertie(String url) {
		this.property = Propertie.loadUrlProperties(url);
		this.url=url;
	}

	private Properties getProperty() {
		if (this.property == null || this.property.isEmpty() || this.property.size() == 0) {
			this.property = Propertie.loadUrlProperties(this.url);
		}
		return this.property;
	}
	
	/**
	 * 
	 * @Description: PROPERTIES文件加载器
	 * @Title: loadUrlProperties 
	 * @param resourceName "/"
	 * @return Properties
	 */
	private static Properties loadUrlProperties(String resourceName) {
		Properties props = new Properties();
		InputStream is = null;
		URL url = Propertie.class.getResource(resourceName);
		URLConnection con;
		try {
			con = url.openConnection();
			con.setUseCaches(false);
			is = con.getInputStream();
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}
	
	/**
	 * 
	 * @Description: 获取PROPERTIES文件中VALUE的值
	 * @Title: getValue 
	 * @param key PROPERTIES文件中KEY的名称
	 * @return String PROPERTIES文件中VALUE的值
	 */
	public String value(Object key) {
		if (StringUtils.isNotBlank(key+"")) {
			if(getProperty().containsKey(key+"")){
				return getProperty().get(key+"").toString();
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * @Description: 获取Property对象keySet
	 * @Title: keySet 
	 * @return Set<Object>
	 */
	public Set<Object> keySet() {
		return getProperty().keySet();
	}
	
	//custom property method······
}