package com.ts.controller.mts.HandlerCenter.HandlerManager;


import net.sf.json.JSONObject;

/**
 * 流程管理者。
 * @author autumn
 *
 */
public interface IFlowHandler extends Runnable
{
    
    /**
     * 单独流程管理者
     * @param rsEntity
     *          {
     *          originalCode:原始code 
     *          originalData:原始数据 
     *          currOrigDatas: 当然处理数据（增加清洗后数据存储）
     *              [
     *                  {currOrigData:"",currOrigDataClean:"",number:1,status:""},
     *                  {currOrigData:"",currOrigDataClean:"",number:2,status:""}
     *              
     *              ]  当前原始数据 
     *          number        :当前处理编号
     *          area          :区域表
     *          cluster       :聚类
     *          standardType  :标化类型
     *          doub          :怀疑标识
     *          exceMemid     :执行步骤 id 
     *          status        :状态 1 成功 ，0 失败
     *          mainApplyMethod：主流程名称，用于确定返回结果是否入结果队列
     *          flowApplyMethod：当前流程的名称，用于工具的处理
     *          flowBeforApplyMethod:前置方法执行的名称，用于工具的处理
     *          flowAfterApplyMethod：后置方法执行的名称，用于工具的处理
     *          rsDatas       结果数据
     *              [
     *                  {
     *                  nubmer：  1
     *                  subOriginalData:   拆分后的原始数据
     *                  subRsData:         标化结果数据
     *                  subSpec:           特殊处理标识
     *                  }
     *                  {
     *                  nubmer：  1
     *                  subOriginalData:   拆分后的原始数据
     *                  subRsData:         标化结果数据
     *                  subSpec:           特殊处理标识
     *                  }
     *              ]
     *          }
     * @return 
     */
    public JSONObject SingletonHandler(JSONObject rsEntity) throws Exception ; 
    
    /**
     * 多流程管理者 并行处理 
     * @param areaid
     * @param dataClassCode
     * @return
     * @throws Exception
     */
    public Object multipleHandler(String areaid , String dataClassCode) throws Exception;
    
}
