package com.ts.controller.mts.HandlerCenter.HandlerManager.Impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.IFlowHandler;
import com.ts.controller.mts.HandlerCenter.HandlerManager.IMagazineHandler;
import com.ts.controller.mts.HandlerCenter.QueueManager.QueueStandard;
import com.ts.controller.mts.HandlerCenter.Util.FactoryJsonInput;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.util.Logger;
import com.ts.util.SpringContextUtils;

import net.sf.json.JSONObject;

/**
 * 整体流程管理者
 * @author autumn
 *
 */
@Service
public final class FlowHandler  implements IFlowHandler
{

    private static Logger  log = Logger.getLogger("FlowHandler");
    // 多线程执行时， 线程数。
    private int threadCount = 2;
    
    private Object  getJsonObject(JSONObject rsEntity , String keyName)
    {
        Object rs = "";
        try
        {
            rs = rsEntity.get(keyName) == null ?"":rsEntity.get(keyName);
        }
        catch(Exception e)
        {
            log.debug("JSON中为找到key为：" +  keyName);//e.printStackTrace();
        }
        return rs;
    }
    
    @Override
    public JSONObject SingletonHandler(JSONObject rsEntity) throws Exception 
    {
//        // 原始数据
//        String originalData = rsEntity.getString("originalData");
//        // 当前分割后原始数据
//        JSONArray jarray = rsEntity.getJSONArray("currOrigDatas");
//        String currOrigData = "" ;
//        if(jarray.size() > 0 )
//            currOrigData = jarray.getJSONObject(0).getString("currOrigData");
        // 区域代码 
        String area         = rsEntity.getString("area");
        // 聚类代码
        String cluster      = rsEntity.getString("cluster");
        // 标化类型 
//        String standardType = getJsonObject(rsEntity, "standardType").toString();
        // 标示流程入口 
        String orderBy      = getJsonObject(rsEntity, "orderBy") == null ?"1":getJsonObject(rsEntity, "orderBy").toString();
        //目标流程 id   
        String exceMemid    = getJsonObject(rsEntity, "exceMemid").toString();
        
        // 获取流程 拼装 key 
        String key = area + "_" + cluster +  "_" + ("0".equals(orderBy) ?exceMemid:"") ;
        // 业务流程实体 
        MatchRuleDetailT flowBean =  CacheManager.getMatchRuleDetail(key);
        if(flowBean == null )
        {
            log.debug("缓存中为找到该流程：" + key );
            return rsEntity;
        }
        
        String currentMemid = flowBean.getMEM_ID();
        rsEntity.put("standardType", flowBean.getMEM_DATA_CODE());
        
        // 流程级别   APPLY_METHOD 执行之前  执行  名称
        rsEntity.put("exceMemid", currentMemid);
        String beforeExce       = flowBean.getBeforeAPPLY_METHOD(); 
        rsEntity.put("flowBeforApplyMethod", beforeExce);
        // 程序执行前 执行  。
        if(beforeExce != null && "".equals(beforeExce))
            rsEntity = ((IMagazineHandler)SpringContextUtils.getBean(beforeExce)).ToolhandlerBefor(rsEntity);
        
        // 嵌套流程入口 ID  在  exceprogram  之前     和  在 beforeExce 之后   执行嵌套流程  
        String beforSubFlow     = flowBean.getBeforeExceSubFlow();
        if(beforSubFlow != null && !"".equals(beforSubFlow)) rsEntity = exceSubFlow(rsEntity,beforSubFlow);
        
        // 目标执行程序名称
        rsEntity.put("exceMemid", currentMemid);
        String  exceprogram = flowBean.getAPPLY_METHOD();
        IMagazineHandler thExce =(IMagazineHandler)SpringContextUtils.getBean(exceprogram);
        rsEntity.put("flowApplyMethod", exceprogram);
        
        rsEntity = thExce.Toolhandler(rsEntity);
        // 嵌套流程入口 ID  在  exceprogram  之后     和    在 afterSubFlow 之前   执行嵌套流程
        String afterSubFlow     = flowBean.getAfterExceSubFlow();     
        if(afterSubFlow != null && !"".equals(afterSubFlow)) rsEntity = exceSubFlow(rsEntity,afterSubFlow);
        
        // 流程级别    APPLY_METHOD 执行之后  执行   名称
        String afterExce        = flowBean.getAfterAPPLY_METHOD(); 
        rsEntity.put("exceMemid", currentMemid);
        rsEntity.put("flowAfterApplyMethod", afterExce);
        // 整个 程序执行后执行 
        if(afterExce != null && "".equals(afterExce))
            rsEntity = ((IMagazineHandler)SpringContextUtils.getBean(afterExce)).ToolhandlerAfter(rsEntity);
        
        //TODO 需要对结果（rsEntity） 进行处理 和 设置  如果有需要 。
        
        // 目标执行程序结果   0 :失败流程  ， 1: 成功流程
        String status = rsEntity.getString("status");
        exceMemid = "0".equals(status) ? flowBean.getFAILURE_REDIRECT_TO():flowBean.getSUCCESS_REDIRECT_TO();
        
        //执行下一环节
        if(exceMemid != null && !"".equals(exceMemid)){
            rsEntity.element("orderBy","0");
            rsEntity.element("exceMemid", exceMemid);
            rsEntity =  this.SingletonHandler(rsEntity);
        }
        return rsEntity;
    }
    
    /**
     * 执行嵌套 子流程
     * @param rsEntity
     * @return
     */
    private JSONObject  exceSubFlow(JSONObject rsEntity,String SubFlowId)
    {
        // 业务流程实体 
        MatchRuleDetailT SubflowBean =  CacheManager.getSubMatchRuleDetail(SubFlowId);
        if(SubflowBean == null )
        {
            log.debug("缓存中为找到嵌套流程：" + SubFlowId );
            return rsEntity;
        }
        rsEntity.put("exceMemid", SubFlowId);
        // 流程级别   APPLY_METHOD 执行之前  执行  名称
        String beforeExce       = SubflowBean.getBeforeAPPLY_METHOD(); 
        rsEntity.put("flowBeforApplyMethod", beforeExce);
        // 程序执行前 执行  。
        if(beforeExce != null && "".equals(beforeExce))
            rsEntity = ((MagazineHandler)SpringContextUtils.getBean(beforeExce)).ToolhandlerBefor(rsEntity);
        
        // 嵌套流程入口 ID  在  exceprogram  之前     和  在 beforeExce 之后   执行嵌套流程  
        String beforSubFlow     = SubflowBean.getBeforeExceSubFlow();
        if(beforSubFlow != null && !"".equals(beforSubFlow)) rsEntity = exceSubFlow(rsEntity,beforSubFlow);
        
        // 目标执行程序名称
        String  exceprogram = SubflowBean.getAPPLY_METHOD();
        IMagazineHandler thExce =(IMagazineHandler)SpringContextUtils.getBean(exceprogram);
        rsEntity.put("flowApplyMethod", exceprogram);
        
        rsEntity = thExce.Toolhandler(rsEntity);
        
        // 嵌套流程入口 ID  在  exceprogram  之后     和    在 afterSubFlow 之前   执行嵌套流程
        String afterSubFlow     = SubflowBean.getAfterExceSubFlow();     
        if(afterSubFlow != null && !"".equals(afterSubFlow)) rsEntity = exceSubFlow(rsEntity,afterSubFlow);    
        
        // 流程级别    APPLY_METHOD 执行之后  执行   名称
        String afterExce        = SubflowBean.getAfterAPPLY_METHOD(); 
        rsEntity.put("flowAfterApplyMethod", afterExce);
        // 程序执行后执行 
        if(afterExce != null && "".equals(afterExce))
            rsEntity = ((MagazineHandler)SpringContextUtils.getBean(afterExce)).ToolhandlerAfter(rsEntity);

        // 目标执行程序结果   0 :失败流程  ， 1: 成功流程
        String status = rsEntity.getString("status");
        SubFlowId = "0".equals(status) ? SubflowBean.getFAILURE_REDIRECT_TO():SubflowBean.getSUCCESS_REDIRECT_TO();
        // 执行下一个 流程。
        if(SubFlowId != null && !"".equals(SubFlowId)) exceSubFlow(rsEntity, SubFlowId);
        return rsEntity;
    }

    // 线程池管理 
    private static  ExecutorService schedluled = null;
    private JSONObject multipleJson = new JSONObject();
    @Override
    public Object multipleHandler(String areaid , String dataClassCode) throws Exception 
    {
        try
        {
            if(schedluled != null) return "OK";
            schedluled = Executors.newFixedThreadPool(this.threadCount);
            for(int i = 0 ; i < threadCount ; i++)
            {
                JSONObject rsEntity = FactoryJsonInput.JsonInputByAll( areaid ,  dataClassCode, "","");
                FlowHandler fh = new FlowHandler();
                fh.multipleJson = rsEntity;
                schedluled.execute(fh);
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            return "NO";
        }
        return "OK";
    }
    
    @Override
    public void run()
    {
        //TODO 需要从队列里面获取数据 
        while(true)
        {
            if(QueueStandard.getQueueStandarSize() > 0 )
            {
                JSONObject standard = QueueStandard.getQueueStandar();
                log.debug( "线程信息 : " + Thread.currentThread());
                try
                {
                    
                    JSONObject entity = this.multipleJson;
                    entity.put("originalCode", standard.getString("originalCode"));
                    // 原始数据
                    entity.put("originalData", standard.getString("originalData"));
                    // 当前分割后原始数据
                    entity.put("currOrigDatas", standard.getJSONArray("currOrigDatas"));
                    entity.put("orderBy", null);
                    JSONObject rsEntity = this.SingletonHandler(entity);
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
        // 需要将结果放到list中 ，进行一起准备。 如果需要 
    }
    
    /**
     * 并发关闭所有线程 ，
     */
    public static void closeMultiple()
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
    
//    /**
//     * 
//     */
//    public static void CompelCloseMultiple()
//    {
//        
//    }
    
}
