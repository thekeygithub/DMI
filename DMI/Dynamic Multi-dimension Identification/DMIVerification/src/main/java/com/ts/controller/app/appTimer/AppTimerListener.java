package com.ts.controller.app.appTimer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;

import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.util.Logger;

public class AppTimerListener implements ServletContextListener {
	private static Logger log = Logger.getLogger(AppTimerListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("==============加载Api定时任务====开始===============");
		try {
			boolean on_off = Boolean.parseBoolean(ReadPropertiesFiles.getValue("solr.on_off"));
			if (on_off) {
				int hour = Integer.parseInt(ReadPropertiesFiles.getValue("solr.timer_hour"));// 执行小时数（0~23）
				int minute = Integer.parseInt(ReadPropertiesFiles.getValue("solr.timer_minute"));// 执行分钟数（0~59）
				int second = Integer.parseInt(ReadPropertiesFiles.getValue("solr.timer_second"));// 执行秒数（0~59）
				int seconds_fullImport = Integer
						.parseInt(ReadPropertiesFiles.getValue("solr.timer_seconds_fullImport"));// 全量索引间隔时间（单位：秒）
				QuartzUtil.addJobContainsStartTime(hour, minute, second, "SolrFullImport_Job", "SolrFullImport_Trigger",
						AppSolrFullImportTimer.class, seconds_fullImport);

				int seconds_deltaImport = Integer
						.parseInt(ReadPropertiesFiles.getValue("solr.timer_seconds_deltaImport"));// 增量索引时间间隔（单位：秒）
				QuartzUtil.addJobForAnHourLater("SolrDeltaImport_Job", "SolrDeltaImport_Trigger",
						AppSolrDeltaImportTimer.class, seconds_deltaImport);
			} else {
				log.info("-------------------未开启加载solr开关-------------------");
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			log.info("==============加载Api定时任务====失败===============");
		}
		log.info("==============加载Api定时任务====结束===============");
	}
}
