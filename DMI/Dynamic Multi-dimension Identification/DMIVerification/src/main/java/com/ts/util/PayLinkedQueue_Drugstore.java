package com.ts.util;

import java.util.LinkedList;

import com.ts.entity.P_drugstore_inte_info;

public class PayLinkedQueue_Drugstore {

	private static LinkedList<P_drugstore_inte_info> QueuePay = new LinkedList<P_drugstore_inte_info>();
	/**
	 * 获得PAY数据队中数据集
	 * @return
	 */
	public static P_drugstore_inte_info getQueuePay()
	{
		synchronized (QueuePay) 
		{
			if(QueuePay.size() >  0 )
			{
				return	QueuePay.removeFirst();
			}
		}
		return null;
	}
	/**
	 * 获得PAY数据数据集 在 队列 个数
	 */
	public static int getQueuePaySize()
	{
		synchronized (QueuePay) 
		{
			return QueuePay.size();	
		}
	}
	
	/**
	 * 将PAY数据总数据集 放到队列中
	 * @param rs
	 */
	public static void setQueuePay(P_drugstore_inte_info rs)
	{
		synchronized (QueuePay) 
		{
			QueuePay.addLast(rs);   
		}
	}
	
	/**
	 * 获得PAY数据修改队中数据集
	 * @return
	 */
	public static P_drugstore_inte_info getQueuePayUP()
	{
//		synchronized (QueuePay) 
//		{
//			if(QueuePay.size() >  0 )
//			{
//				return	QueuePay.removeFirst();
//			}
//		}
		return null;
	}
	/**
	 * 获得PAY数据修改数据集 在 队列 个数
	 */
	public static int getQueuePayUPSize()
	{
//		synchronized (QueuePay) 
//		{
//			return QueuePay.size();	
//		}
	    return 0;
	}
	/**
	 * 将PAY数据修改总数据集 放到队列中
	 * @param rs
	 */
	public static void setQueuePayUP(P_drugstore_inte_info rs)
	{
//		synchronized (QueuePay) 
//		{
//			QueuePay.addLast(rs);
//		}
	}
	private PayLinkedQueue_Drugstore(){}
}
