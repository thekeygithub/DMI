package com.ts.controller.mts.HandlerCenter.HandlerManager.Impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.IRsHandler;
import com.ts.controller.mts.HandlerCenter.QueueManager.QueueStandardByRs;
import com.ts.controller.mts.HandlerCenter.TemplateManager.IRsSaveTemplate;
import com.ts.entity.mts.RsRuleDetailT;
import com.ts.util.Logger;
import com.ts.util.SpringContextUtils;

import net.sf.json.JSONObject;

/**
 * 标化结果管理
 * @ClassName:RsHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年11月29日下午3:05:30
 */
@Service
public final class RsHandler  implements IRsHandler
{
    private static Logger  log = Logger.getLogger("RsHandler");
    
    private  RsHandler() {}
    
    /* 创建实例 */
    private static class InstanceHolder{ 
    	
        public static RsHandler instance = new RsHandler();
    }
    public  static RsHandler getInstance(){
    	
        return InstanceHolder.instance;
    }

    // 多线程执行时， 线程数。  
    private int threadCount = 3;
    
    public int getThreadCount()
    {
        return threadCount;
    }

    public void setThreadCount(int threadCount)
    {
        this.threadCount = threadCount;
    }

    // 线程池管理 
    private  ExecutorService schedluled = null;
    
    @Override
    public Object rsQueueHandler() throws Exception 
    {
        try
        {
            if(schedluled != null) return "OK";
            schedluled = Executors.newFixedThreadPool(this.threadCount);
            for(int i = 0 ; i < threadCount ; i++)
            {
                rsExceClass rsExce = new  rsExceClass();
                schedluled.execute(rsExce);
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            return "NO";
        }
        return "OK";
    }
    
    public boolean SingletonRs(JSONObject rs ) throws Exception
    {
        rsExceClass rsExce = new rsExceClass();
        boolean rsBool = rsExce.SingletonRs(rs);
        rsExce = null;
        return rsBool;
    }
    /**
     * 具体执行线程方法
     * @author autumn
     *
     */
    private class rsExceClass implements Runnable 
    {
        @Override
        public void run()
        {
            //TODO 需要从队列里面获取数据 
            while(true)
            {
                if(QueueStandardByRs.getQueueStandarSize() > 0 )//结果队列不为空 则执行线程
                {
                    log.debug( "线程信息 : " + Thread.currentThread());
                    try
                    {
                        //取队列中的一个
                        JSONObject rs = QueueStandardByRs.getQueueStandar();
                        boolean rsBool = this.SingletonRs(rs);
                    }
                    catch(Exception e )
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        log.debug("捕获集合为空，等待5秒后继续捕获！");
                        Thread.sleep(5000);
                    }catch(Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        private boolean SingletonRs(JSONObject rs) throws Exception
        {
            //根据rs的area区域及cluster聚类确定执行哪个方法
            // 区域代码 
            String area         = rs.getString("area");
            // 聚类代码
            String cluster      = rs.getString("cluster");
            // 获取处理 拼装 key 
            String key = area + "_" + cluster ;
            // 结果处理规则实体 
            RsRuleDetailT rsRuleBean =  CacheManager.getRsRuleDetail(key);
            if(rsRuleBean == null )
            {
                log.debug("缓存中未找到该类型的标化结果规则配置：" + key );
                return false;
            }
            //根据配置的方法执行具体的方法
            String  exceprogram = rsRuleBean.getAPPLY_METHOD();
            IRsSaveTemplate thExce =(IRsSaveTemplate)SpringContextUtils.getBean(exceprogram);
            return thExce.RsSave(rs);
        }
    }
    
    /**
     * 并发关闭所有线程 ，
     */
    public void closeMultiple()
    {
        try
        {
            if(schedluled !=  null) 
            {
                schedluled.shutdown();
                schedluled = null;
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }

}
