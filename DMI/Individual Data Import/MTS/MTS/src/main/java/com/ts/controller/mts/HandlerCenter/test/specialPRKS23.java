package com.ts.controller.mts.HandlerCenter.test;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;

import net.sf.json.JSONObject;

@Service("specialPRKS23")
public class specialPRKS23 extends MagazineHandler
{

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
        entity.put("status", "0");
        System.out.println(Thread.currentThread() + ",specialPRKS23 - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",specialPRKS23 - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",specialPRKS23 - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }

}
