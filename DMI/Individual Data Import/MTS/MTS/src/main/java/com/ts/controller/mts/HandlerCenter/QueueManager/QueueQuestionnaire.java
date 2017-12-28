package com.ts.controller.mts.HandlerCenter.QueueManager;

import java.util.LinkedList;

/**
 *  问题单数据列队
 * @author autumn
 *
 */
public class  QueueQuestionnaire 
{
    // 问题单数据
    private static LinkedList<Object>  queueStandard = new LinkedList<Object>();
    
    public static <T> void setQueueStandar(T entity)
    {
        queueStandard.addLast(entity);
    }
    
    public static <T> T getQueueStandar()
    {
        synchronized(QueueQuestionnaire.class)
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
