package com.dhcc.ts.library.solr.timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时更新solr启动监听
 * @author Administrator
 *
 */
public class SolrImportListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			SchedulerFactory schedFact=new StdSchedulerFactory();
	        Scheduler sched=schedFact.getScheduler();
	        sched.start();
	        JobDetail jobDetail=new JobDetail("mySolrDeltaImport","SolrDeltaImport",SolrDeltaImport.class);
	        CronTrigger trigger=new  CronTrigger("SolrDeltaImportTrigger","Trigger");
//	        trigger.setCronExpression("0 */10 * * * ?"); //每十分钟执行一次
	        trigger.setCronExpression("0 0 2 * * ?"); //每天两点执行一次
	        sched.scheduleJob(jobDetail, trigger);
	        
	        //接口ping
	        JobDetail jobDetail1=new JobDetail("myInterfacePing","InterfacePing",InterfacePing.class);
	        CronTrigger trigger1=new  CronTrigger("InterfacePingTrigger","Trigger");
	        trigger1.setCronExpression("0 */5 * * * ?"); //每十分钟执行一次
//	        trigger1.setCronExpression("0 0 2 * * ?"); //每天两点执行一次
	        sched.scheduleJob(jobDetail1, trigger1);
	        
	        //BI
	        JobDetail biDetail=new JobDetail("MyBiDetail","BiDetail",BiDetail.class);
	        CronTrigger biTrigger=new  CronTrigger("BiTrigger","Trigger");
//	        biTrigger.setCronExpression("0 */1 * * * ?"); //每十分钟执行一次
	        biTrigger.setCronExpression("0 0 2 * * ?"); //每天两点执行一次
	        sched.scheduleJob(biDetail, biTrigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}