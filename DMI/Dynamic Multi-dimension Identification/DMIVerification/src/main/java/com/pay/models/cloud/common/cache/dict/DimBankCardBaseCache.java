package com.models.cloud.common.cache.dict;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.dict.entity.DimBankCardBase;
import com.models.cloud.common.dict.service.DimBankCardBaseService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡信息
 * Created by yacheng.ji on 2017/2/10.
 */
@Service("dimBankCardBaseCache")
public class DimBankCardBaseCache extends DictCache {

    private static Logger log = Logger.getLogger(DimBankCardBaseCache.class);

    @Resource
    private DimBankCardBaseService dimBankCardBaseServiceImpl;

    private static Map<String, DimBankCardBase> dimBankCardBaseMap;

    private static Date cacheDate;

    /**
     * 构造方法
     */
    private DimBankCardBaseCache(){
        tableName = "DIM_BANK_CARD_BASE";
    }

    public static Map<String, DimBankCardBase> getDimBankCardBaseMap() {
        if(null == dimBankCardBaseMap || 0 == dimBankCardBaseMap.size()){
            DimBankCardBaseCache cache = new DimBankCardBaseCache();
            cache.initDictCache();
        }
        return dimBankCardBaseMap;
    }

    /**
     * 获取缓存更新时间
     */
    public Date getCacheDate(){
        return cacheDate;
    }

    public void initDictCache() {
        try{
            List<DimBankCardBase> lst = dimBankCardBaseServiceImpl.findDimBankCardBaseList();
            if(null == lst){
                log.info("加载字典表缓存" + tableName +"：表数据为空。");
                return;
            }
            dimBankCardBaseMap = new HashMap<>();
            for(DimBankCardBase conf : lst){
                dimBankCardBaseMap.put(conf.getCardCode(), conf);
            }
            cacheDate = new Date();
        } catch (Exception ex) {
            log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
        } finally {
            log.info("已完成加载字典表缓存 "+ tableName);
        }
    }
}
