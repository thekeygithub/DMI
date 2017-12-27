package com.dhcc.ts.library.solr.timer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dhcc.ts.library.solr.SolrQueryLibrary;

/**
 * @描述：定时任务工作内容
 * @作者：SZ
 * @时间：2017年5月6日 下午7:08:06
 */
public class SolrDeltaImport implements Job{
	
	private static final Logger logger = Logger.getLogger(SolrDeltaImport.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		doSomething();
	}
	
	public void doSomething(){
		updateSolrData();
		logger.info("---------------solr增量更新-----------------");
	}
	
	private static void updateSolrData() {
		//增量更新
		String DEFAULT_URL = SolrQueryLibrary.DEFAULT_URL+"/dataimport?command=delta-import";
		importData(DEFAULT_URL);
	}
	
	/**
	 * @描述：导入数据
	 * @作者：SZ
	 * @时间：2017年5月6日 下午7:07:50
	 * @param DEFAULT_URL
	 */
	private static void importData(String DEFAULT_URL){
		long begintime = System.currentTimeMillis();
		try {
			URL url = new URL(DEFAULT_URL);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.connect(); // 获取连接
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l).append("/n");
			}
//			System.out.println(bs.toString());

			logger.info("总共执行时间为：" + (System.currentTimeMillis() - begintime) + "毫秒");
		} catch (IOException e) {
			logger.info(e);
		}
	}
}
