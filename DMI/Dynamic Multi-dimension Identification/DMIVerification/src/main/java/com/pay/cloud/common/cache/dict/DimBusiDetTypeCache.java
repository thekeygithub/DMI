package com.pay.cloud.common.cache.dict;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.trade.entity.DimBusiDetType;
import com.pay.cloud.pay.trade.service.DimBusiDetTypeService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务类型字典数据
 * Created by yacheng.ji on 2017/2/24.
 */
@Service("dimBusiDetTypeCache")
public class DimBusiDetTypeCache extends DictCache {

    private static Logger log = Logger.getLogger(DimBusiDetTypeCache.class);

    @Resource
    private DimBusiDetTypeService dimBusiDetTypeServiceImpl;

    private static Map<String, DimBusiDetType> dimBusiDetTypeMap;

    private static Date cacheDate;

    /**
     * 构造方法
     */
    private DimBusiDetTypeCache(){
        tableName = "DIM_BUSI_DET_TYPE";
    }

    public static Map<String, DimBusiDetType> getDimBusiDetTypeMap() {
        if(null == dimBusiDetTypeMap || 0 == dimBusiDetTypeMap.size()){
            DimBusiDetTypeCache cache = new DimBusiDetTypeCache();
            cache.initDictCache();
        }
        return dimBusiDetTypeMap;
    }

    /**
     * 获取缓存更新时间
     */
    public Date getCacheDate(){
        return cacheDate;
    }

    public void initDictCache() {
        try{
            List<DimBusiDetType> lst = dimBusiDetTypeServiceImpl.findDimBusiDetTypeList();
            if(null == lst){
                log.info("加载字典表缓存" + tableName +"：表数据为空。");
                return;
            }
            dimBusiDetTypeMap = new HashMap<>();
            for(DimBusiDetType conf : lst){
                dimBusiDetTypeMap.put(conf.getBusiDetTypeId(), conf);
            }
            cacheDate = new Date();
        } catch (Exception ex) {
            log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
        } finally {
            log.info("已完成加载字典表缓存 "+ tableName);
        }
    }
}
