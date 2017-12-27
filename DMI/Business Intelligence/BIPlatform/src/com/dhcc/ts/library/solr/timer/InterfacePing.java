package com.dhcc.ts.library.solr.timer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dhcc.common.util.SystemConfig;
import com.dhcc.ts.library.solr.SolrQueryLibrary;
import com.dhcc.ts.library.solr.mailUtil.SendEmail;
import com.dhcc.ts.mtsService.MTSUtil;

import net.sf.json.JSONObject;

/**
 * @描述：项目接口是否可以测试
 * @作者：SZ
 * @时间：2017年5月11日 上午11:39:00
 */
public class InterfacePing implements Job {
	private static final Logger logger = Logger.getLogger(InterfacePing.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		doSomeThing();
	}

	// 0时发送邮件，防止连续发送告警邮件
	private static int solrType = 0;
	private static int nlpType = 0;
	private static int mtsType = 0;
	private static int mtsPingType = 0;
	private static int count = 12;// 每五分钟执行一次doSomeThing()方法，间隔一个小时发送一次

	public static void doSomeThing() {

		String HOST_IP = SystemConfig.getConfigInfomation("com.dhcc.ts.cpa.properties.CPAResources", "DEPLOYMENT_HOST_IP");
		String CURRENT_IP = null;
		try {
			CURRENT_IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//只有在服务端部署才启动此验证
		if (HOST_IP.equals(CURRENT_IP)) {
			if (SolrQueryLibrary.solrPing()) {
//				logger.info("-------------------Solr连接正常-------------------");
				solrType = 0;
			} else {
				if (0 == solrType % count) {
					SendEmail.sendEmail("医疗数据交换平台Solr链接异常", "请检查solr服务器是否正常！", 0, 0);
				}
				solrType++;
			}

			try {
				JSONObject json = MTSUtil.testPing();
				JSONObject jsonResult = JSONObject.fromObject(json.get("result"));
				JSONObject jsonNlpValue = JSONObject.fromObject(jsonResult.get("NlpValue"));
				if(jsonNlpValue.isEmpty()){
					if (0 == nlpType % count) {
						SendEmail.sendEmail("医疗数据交换平台NLP服务异常", "现在医疗数据交换平台连接MTS接口出现问题，因调用MTS接口无NLP返回结果集导致的，请黄老师查明原因！", 1, 1);
					}
					nlpType++;
				}else{
//					logger.info("-------------------NLP连接正常-------------------");
					nlpType = 0;
					JSONObject value = JSONObject.fromObject(jsonNlpValue.get("诊断"));
					String result = (String) value.get("高血压");
					
					if (result.startsWith("高血压病")) {
//						logger.info("-------------------MTS连接正常-------------------");
						mtsType = 0;
					} else {
						if (0 == mtsType % count) {
							SendEmail.sendEmail("医疗数据交换平台MTS服务异常", "现在医疗数据交换平台连接MTS接口出现问题，因调用接口传参高血压的MTS返回结果为：" + result + ",不是预期的返回值，请夏老师查明原因！", 1, 2);
						}
						mtsType++;
					}
				}
				mtsPingType = 0;
			} catch (Exception e) {
				logger.info("-------------------MTS请求服务器异常-------------------" + e.getMessage());
				if (0 == mtsPingType % count) {
					SendEmail.sendEmail("医疗数据交换平台MTS服务异常", "现在医疗数据交换平台连接MTS接口出现问题，请夏老师查明原因！倾向于链接异常！", 1, 2);
				}
				mtsPingType++;
			}
		}
	}

	public static void main(String[] args) {
		doSomeThing();
	}
}
