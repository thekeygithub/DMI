package com.ts.controller.app.appTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.ts.util.Logger;

public class QuartzUtil {

	private final static String JOB_GROUP_NAME = "QUARTZ_JOBGROUP_NAME";
	private final static String TRIGGER_GROUP_NAME = "QUARTZ_TRIGGERGROUP_NAME";
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger log = Logger.getLogger(QuartzUtil.class);

	/**
	 * @描述：添加任务的方法包含起始时间
	 * @作者：SZ
	 * @时间：2016年11月23日 上午10:31:58
	 * @param hour
	 *            定时任务启动小时数(0~23)
	 * @param minute
	 *            定时任务启动分钟数(0~59)
	 * @param second
	 *            定时任务启动秒数(0~59)
	 * @param jobName
	 *            任务名
	 * @param triggerName
	 *            触发器名
	 * @param jobClass
	 *            执行任务的类
	 * @param seconds
	 *            间隔时间 单位:秒
	 * @throws SchedulerException
	 */
	public static void addJobContainsStartTime(int hour, int minute, int second, String jobName, String triggerName,
			Class<? extends Job> jobClass, int seconds) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();

		Date runTime = DateBuilder.dateOf(hour, minute, second);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
				.startAt(runTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever())
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		log.info("==============添加定时任务类名：" + jobClass + "  启动时间：" + hour + ":" + minute + ":" + second + "   时间间隔："
				+ seconds + "s=================");
	}
	
	public static void addJobForAnHourLater(String jobName, String triggerName, Class<? extends Job> jobClass, int seconds) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
		Date runTime = DateBuilder.evenHourDateAfterNow();//一小时后
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
				.startAt(runTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever())
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		log.info("==============添加定时任务类名：" + jobClass + "  启动时间：" + sf.format(runTime) + "   时间间隔："
				+ seconds + "s=================");
	}
}