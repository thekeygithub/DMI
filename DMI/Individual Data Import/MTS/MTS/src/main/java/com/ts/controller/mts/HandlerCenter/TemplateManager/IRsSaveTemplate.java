package com.ts.controller.mts.HandlerCenter.TemplateManager;

import net.sf.json.JSONObject;

/**
 * 所有结果保存魔板类
 * @author autumn
 *
 */
public interface IRsSaveTemplate
{
    /**
     * 保存方法 
     * @param rsEntity
     * @return
     */
    public boolean RsSave(JSONObject rsEntity);
    
}
