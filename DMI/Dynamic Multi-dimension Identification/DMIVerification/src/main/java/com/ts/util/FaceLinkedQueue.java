package com.ts.util;

import java.util.LinkedList;

import com.ts.entity.FaceComparison.P_face_compare;

public class FaceLinkedQueue {

	private FaceLinkedQueue() {}
	
	private static LinkedList<P_face_compare> QueuePay = new LinkedList<P_face_compare>();
	
	/**
	 * 获得数据队中数据集
	 * @return
	 */
	public static P_face_compare getQueuePay()
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
	 * 获得数据数据集在 队列个数
	 */
	public static int getQueuePaySize()
	{
		synchronized (QueuePay) 
		{
			return QueuePay.size();	
		}
	}
	
	/**
	 * 将数据总数据集放到队列中
	 * @param rs
	 */
	public static void setQueuePay(P_face_compare rs)
	{
		synchronized (QueuePay) 
		{
			QueuePay.addLast(rs);   
		}
	}
	
}