package com.ts.controller.mts.HandlerCenter.test;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;

import net.sf.json.JSONObject;
@Service("matchNlpPRKS23")
public class matchNlpPRKS23 extends MagazineHandler
{

    

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
        
        JSONObject rsEntity = new JSONObject();
        rsEntity.put("", entity.get(""));
        rsEntity.put("", "");
        rsEntity.put("", "");
        rsEntity.put("", "");
        
        AddStandardByRs(rsEntity);
        
        AddQuestionaire(rsEntity);
        
        
        
        entity.put("status", "1");
        System.out.println(Thread.currentThread() + ",matchNlpPRKS23 - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",matchNlpPRKS23 - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",matchNlpPRKS23 - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}

