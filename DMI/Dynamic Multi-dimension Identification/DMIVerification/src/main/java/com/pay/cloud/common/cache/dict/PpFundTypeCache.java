package com.pay.cloud.common.cache.dict;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.entity.PpFundType;
import com.pay.cloud.pay.proplat.service.PpFundTypeService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金平台类型
 * Created by yacheng.ji on 2017/2/23.
 */
@Service("ppFundTypeCache")
public class PpFundTypeCache extends DictCache {

    private static Logger log = Logger.getLogger(PpFundTypeCache.class);

    @Resource
    private PpFundTypeService ppFundTypeServiceImpl;

    private static Map<String, PpFundType> ppFundTypeMap;

    private static Date cacheDate;

    /**
     * 构造方法
     */
    private PpFundTypeCache(){
        tableName = "PP_FUND_TYPE";
    }

    public static Map<String, PpFundType> getPpFundTypeMap() {
        if(null == ppFundTypeMap || 0 == ppFundTypeMap.size()){
            PpFundTypeCache cache = new PpFundTypeCache();
            cache.initDictCache();
        }
        return ppFundTypeMap;
    }

    /**
     * 获取缓存更新时间
     */
    public Date getCacheDate(){
        return cacheDate;
    }

    public void initDictCache() {
        try{
            List<PpFundType> lst = ppFundTypeServiceImpl.findPpFundTypeList();
            if(null == lst){
                log.info("加载字典表缓存" + tableName +"：表数据为空。");
                return;
            }
            ppFundTypeMap = new HashMap<>();
            for(PpFundType conf : lst){
                ppFundTypeMap.put(conf.getPpFundTypeId(), conf);
            }
            cacheDate = new Date();
        } catch (Exception ex) {
            log.error("加载字典表" + tableName + "缓存出错：" + ex.getMessage());
        } finally {
            log.info("已完成加载字典表缓存 "+ tableName);
        }
    }
}
