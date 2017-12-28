package com.ts.controller.mts.HandlerCenter.test;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;

import net.sf.json.JSONObject;

@Service("tsckSHZT")
public class tsckSHZT extends MagazineHandler
{

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
        entity.put("status", "0");
        System.out.println(Thread.currentThread() + ",tsckSHZT - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",tsckSHZT - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",tsckSHZT - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }


}
