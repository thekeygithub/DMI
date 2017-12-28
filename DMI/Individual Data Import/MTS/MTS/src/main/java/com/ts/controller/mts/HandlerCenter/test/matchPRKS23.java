package com.ts.controller.mts.HandlerCenter.test;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;

import net.sf.json.JSONObject;

@Service("matchPRKS23")
public class matchPRKS23 extends MagazineHandler
{

     
    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
        
        entity.put("status", "0");
        String s = "-1";
        try
        {
            s = entity.getString("exceMemid");
        } 
        catch(Exception e )
        {
            
        }
        System.out.println(Thread.currentThread() + ",matchPRKS23 ：MID:" + s + " - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",matchPRKS23 - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",matchPRKS23 - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }

}
