package com.ts.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.ts.util.CommonUtils;
import com.ts.util.ConnectionPoolManager;
import com.ts.util.DBinit;
import com.ts.util.IConnectionPool;

/**
 * 
 * 类名称：RedisDataLoadListener.java 类描 ：初始化装载Redis数据 作者：李巍
 * 
 * 时间：2016.09.06
 * 
 * @version 1.0
 */
public class RedisDataLoadListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(RedisDataLoadListener.class);
	public static Map cityMap = new HashMap();
	public static Map entityTypeMap = new HashMap();
	public static Map<String,String> enNameMap = new HashMap();
	public static Map<String,String> cnNameMap = new HashMap();
	static {
		// 开封、易保本地
		cityMap.put("410200", "KF");
		cityMap.put("100000", "YB");
		// CN --EN
		entityTypeMap.put("疾病", "disease");
		entityTypeMap.put("症状", "symptom");
		entityTypeMap.put("药品名", "medicine_pn");
		entityTypeMap.put("规格", "specifications");
		entityTypeMap.put("包材", "packing_spe");
		entityTypeMap.put("医疗器材", "medical_dv");
		// EN -CN
		enNameMap.put("disease", "疾病");
		enNameMap.put("symptom", "症状");
		enNameMap.put("treatment", "治疗项目");
		enNameMap.put("diagnosis_name", "辅助检查");
		enNameMap.put("other_diagnosis", "其他诊疗项目");
		enNameMap.put("instrument", "器材");
		enNameMap.put("medicine_cn", "药品-通用名");
		enNameMap.put("medicine_pn", "药品-产品名");
		enNameMap.put("medicine_mn", "药品-商品名");
		enNameMap.put("medicine", "药品名");
		enNameMap.put("dosage_form", "剂型");
		enNameMap.put("specifications", "规格");
		enNameMap.put("packing_spe", "包装规格");
		enNameMap.put("packing_material", "包材");
		enNameMap.put("enterprise", "企业机构");
		enNameMap.put("department", "科室");
		enNameMap.put("address", "地址");
		//  CN - EN
		cnNameMap.put("疾病","disease");
		cnNameMap.put("症状","symptom");
		cnNameMap.put("治疗项目","treatment" );
		cnNameMap.put("辅助检查","diagnosis_name");
		cnNameMap.put("其他诊疗项目","other_diagnosis");
		cnNameMap.put("器材","instrument");
		cnNameMap.put("药品-通用名","medicine_cn");
		cnNameMap.put("药品-产品名","medicine_pn");
		cnNameMap.put("药品-商品名","medicine_mn");
		cnNameMap.put("药品名","medicine");
		cnNameMap.put("剂型","dosage_form");
		cnNameMap.put("规格","specifications");
		cnNameMap.put("包装规格","packing_spe");
		cnNameMap.put("包材","packing_material");
		cnNameMap.put("企业机构","enterprise");
		cnNameMap.put("科室","department");
		cnNameMap.put("地址","address");
	}

	/**
	 * 
	 * @方法名称: contextInitialized
	 * @功能描述: 初始化加载
	 * @作者:李巍
	 * @创建时间:2016年9月28日 下午2:55:49
	 * @param event
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		String isreload = CommonUtils.getPropValue("redis.properties", "redis.isreload");
		if ("on".equals(isreload)) {
			new Thread(new DataLoadThread2()).start();
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	public static IConnectionPool initDB() {
		Thread dbSKThread = DBinit.dbSKInit();
		dbSKThread.start();
		try {
			dbSKThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IConnectionPool pool;
		pool = ConnectionPoolManager.getInstance().getPool("skPool");
		return pool;
	}

	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
	}

	public static void main(String[] args) {
		new Thread(new DataLoadThread()).start();
	}
}