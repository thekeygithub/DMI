package com.ts.controller.mts.HandlerCenter.HandlerManager;

import net.sf.json.JSONObject;

/**
 * 标化结果队列处理
 * @ClassName:IRsHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年11月29日下午2:33:12
 */
public interface IRsHandler {

	/**
	 * 多线程并行处理标化结果队列
	 */
    public Object rsQueueHandler() throws Exception;
    
    /**
     * 单独保存一个标化结果。
     * @param rs
     * @return
     * @throws Exception
     */
    public boolean SingletonRs(JSONObject rs ) throws Exception;
    
}
