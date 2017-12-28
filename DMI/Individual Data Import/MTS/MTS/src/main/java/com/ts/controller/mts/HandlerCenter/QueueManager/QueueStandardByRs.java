package com.ts.controller.mts.HandlerCenter.QueueManager;

import java.util.LinkedList;

/**
 * 标化结果数据列队
 * @author autumn
 *
 */
public class  QueueStandardByRs 
{

    // 标化结果数据集合
    private static LinkedList<Object>  queueStandard = new LinkedList<Object>();
    
    public static <T> void addQueueStandar(T entity)
    {
        queueStandard.addLast(entity);
    }
      
    public static <T> T getQueueStandar()
    {
        synchronized(QueueStandardByRs.class)  
        {
            if(queueStandard.size() >0)
            {
                return (T) queueStandard.removeFirst();    
            }
            return null;
        }
    }
    
    public static int getQueueStandarSize()
    {
        return queueStandard.size();
    }
    
}
