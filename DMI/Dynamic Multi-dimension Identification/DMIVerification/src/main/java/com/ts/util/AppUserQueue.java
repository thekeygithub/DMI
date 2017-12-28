package com.ts.util;

import java.util.LinkedList;

import com.ts.entity.app.AppToken;
import com.ts.entity.app.SysRecycleLog;

/**
 * 队列
 * @author Administrator
 *
 */
public final class AppUserQueue 
{

	private static LinkedList<AppToken> QueueAppToken = new LinkedList<AppToken>();
	private static LinkedList<SysRecycleLog> QueueSysRecycleLog = new LinkedList<SysRecycleLog>();
	
	/**
	 * 获得token队中数据集
	 * @return
	 */
	public static AppToken getQueueAppToken()
	{
		synchronized (QueueAppToken) 
		{
			if(QueueAppToken.size() >  0 )
			{
				return QueueAppToken.removeFirst();	
			}
		}
		return null;
	}
	/**
	 * 获得日志队中数据集
	 * @return
	 */
	public static SysRecycleLog getQueueSysRecycleLog()
	{
		synchronized (QueueSysRecycleLog) 
		{
			if(QueueSysRecycleLog.size() >  0 )
			{
				return QueueSysRecycleLog.removeFirst();	
			}
		}
		return null;
	}
	
	/**
	 * 获得Token数据集 在 队列 个数
	 */
	public static int getQueueAppTokenSize()
	{
		synchronized (QueueAppToken) 
		{
			return QueueAppToken.size();	
		}
	}
	/**
	 * 获得日志数据集 在 队列 个数
	 */
	public static int getQueueSysRecycleLogSize()
	{
		synchronized (QueueSysRecycleLog) 
		{
			return QueueSysRecycleLog.size();	
		}
	}
	
	/**
	 * 将Token总数据集 放到队列中
	 * @param rs
	 */
	public static void setQueueAppToken(AppToken rs)
	{
		synchronized (QueueAppToken) 
		{
			QueueAppToken.addLast(rs);
		}
	}
	/**
	 * 将日志总数据集 放到队列中
	 * @param rs
	 */
	public static void setQueueSysRecycleLog(SysRecycleLog srl)
	{
		synchronized (QueueSysRecycleLog) 
		{
			QueueSysRecycleLog.addLast(srl);
		}
	}
	
	private  AppUserQueue() {}
}