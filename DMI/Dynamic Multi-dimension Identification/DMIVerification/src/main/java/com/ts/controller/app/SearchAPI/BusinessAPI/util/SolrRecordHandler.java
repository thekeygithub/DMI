package com.ts.controller.app.SearchAPI.BusinessAPI.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ts.util.Logger;

/**
 * @描述：solr记录处理类
 * @作者：SZ
 * @时间：2016年11月23日 下午12:21:56
 */
public class SolrRecordHandler {
	private static Logger log = Logger.getLogger(SolrRecordHandler.class);

	/**
	 * @描述：全量导入数据 导入前删除之前索引
	 * @作者：SZ
	 * @时间：2016年9月7日 下午4:42:49
	 * @param url
	 */
	public static void fullImport(String DEFAULT_URL) {
		long begintime = System.currentTimeMillis();
		try {
			URL url = new URL(DEFAULT_URL);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.connect(); // 获取连接
			urlcon.getInputStream();
			log.info(DEFAULT_URL + "总共执行时间为：" + (System.currentTimeMillis() - begintime) + "毫秒");
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	/**
	 * @描述：是否更新成功
	 * @作者：SZ
	 * @时间：2016年12月8日 下午2:29:16
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isIdle(String urlstr) {
		urlstr += "/dataimport?command=status";
		boolean result = false;
		try {
			while(!result){
				URL url = new URL(urlstr);
				HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
				urlcon.connect(); // 获取连接
				InputStream is = urlcon.getInputStream();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
				StringBuffer xmlstr = new StringBuffer();
				String str = null;
				while ((str = buffer.readLine()) != null) {
					xmlstr.append(str);
				}
				Document doc = DocumentHelper.parseText(xmlstr.toString());
				Element root = doc.getRootElement();
				List<Element> list = root.elements();
				for (Element e : list) {
					if("status".equals(e.attributeValue("name"))){
						if("idle".equals(e.getText())){
							result = true;
							break;
						}else{
							log.info("------------更新处理中："+urlstr);
						}
					}
				}
				if(result){
					break;
				}else{
					Thread.sleep(2000);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return result;
	}
}