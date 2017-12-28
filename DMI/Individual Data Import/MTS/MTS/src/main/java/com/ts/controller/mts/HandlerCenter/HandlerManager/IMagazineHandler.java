package com.ts.controller.mts.HandlerCenter.HandlerManager;

import net.sf.json.JSONObject;

/**
 * 工具包管理器
 * @author autumn
 *
 */
public interface IMagazineHandler
{

    /**
     * 工具执行
     * @param entity
     * @return
     */
    public JSONObject Toolhandler(JSONObject entity);
    
    public JSONObject ToolhandlerBefor(JSONObject entity);
    
    public JSONObject ToolhandlerAfter(JSONObject entity);
    
    /**
     * 具体业务处理方法
     * @param entity
     * @return
     */
    public abstract JSONObject BusinessHandler(JSONObject entity);
    
    /**
     * 具体业务处理 之前 执行
     * @param beforeEntity
     * @return
     */
    public abstract JSONObject BeforeBusinessHandler(JSONObject beforeEntity);
    
    /**
     * 具体业务处理之后 执行
     * @param afterEntity
     * @return
     */
    public abstract JSONObject AfterTBusinessHandler(JSONObject afterEntity);
    
    
    /**
     * 向队列中 增加标化结果
     * @param entity
     */
    public void AddStandardByRs(JSONObject entity);
    
    /**
     * 向队列中 增加问题单结果
     * @param entity
     */
    public void AddQuestionaire(JSONObject entity);
    
}
