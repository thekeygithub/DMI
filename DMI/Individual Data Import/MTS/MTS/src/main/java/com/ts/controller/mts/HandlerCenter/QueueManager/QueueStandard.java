package com.ts.controller.mts.HandlerCenter.QueueManager;

import java.util.LinkedList;

/**
 * 待标化数据列队
 * @author autumn
 *
 */
public class  QueueStandard 
{

    // 待标化数据集合
    private static LinkedList<Object>  queueStandard = new LinkedList<Object>();
    
    private static LinkedList<Object> queueStandardByExceRs  = new LinkedList<Object>();
    
    /**
     * 添加 待标化数据  列队
     * @param entity
     */
    public static <T> void addQueueStandar(T entity)
    {
        queueStandard.addLast(entity);
    }
      
    /**
     * 获得 待标化数据  列队数据 
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static <T> T getQueueStandar()
    {
        synchronized(QueueStandard.class)  
        {
            if(queueStandard.size() >0)
            {
                return (T) queueStandard.removeFirst();    
            }
            return null;
        }
    }
    
    /**
     * 获得待标化数据 队列个数
     * @return
     */
    public static int getQueueStandarSize()
    {
        return queueStandard.size();
    }
    
    
    /**
     * 执行结果 列队
     * @param entity
     */
    public static <T> void addQueueStandarByExceRs(T entity)
    {
        queueStandardByExceRs.addLast(entity);
    }
      
    
    /**
     * 获得  执行结果 数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static <T> T getQueueStandarByExceRs()
    {
        synchronized(QueueStandard.class)  
        {
            if(queueStandardByExceRs.size() >0)
            {
                return (T) queueStandardByExceRs.removeFirst();    
            }
            return null;
        }
    }
    
    /**
     * 获得 执行结果 列队 个数
     * @return
     */
    public static int getQueueStandarSizeByExceRs()
    {
        return queueStandardByExceRs.size();
    }
    
    /**
     * 清空 执行结果 列队 。
     */
    public static void cleanQueueStandarSizeByExceRs()
    {
        queueStandardByExceRs.clear();
    }
    
}
